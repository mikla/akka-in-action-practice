package backoff

import java.util.concurrent.TimeUnit

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import monix.eval.Task
import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object StreamBackOffApp extends App {

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  var x = 0

  val source: Task[Source[Int, NotUsed]] = {
    Task(
      Source(1 to 2).scan(0) { (acc, next) =>
        println(s"inside source $next")
        if (x == 0) throw new Exception("Source exception")
        else acc + next
      }).map { s =>
      if (x == 2) throw new Exception("Task exception")
      else s
    }
  }

  def start: Task[(UniqueKillSwitch, Future[Int])] = {
    println("starting...")
    val lastElementSink = Sink.last[Int]
    source.map(_
      .viaMat(KillSwitches.single)(Keep.right)
      .toMat(lastElementSink)(Keep.both)
      .run()
    )
  }

  var initalDelay: FiniteDuration = 0.seconds

  def superwiseWithBackoff2(
    task: Task[(UniqueKillSwitch, Future[Int])],
    minBackOff: FiniteDuration,
    maxBackOff: FiniteDuration) = {

    def delay =
      if (initalDelay.toMillis < maxBackOff.toMillis) initalDelay + minBackOff
      else maxBackOff

    def backoff(start: FiniteDuration): Unit = {
      initalDelay = start
      task.map {
        case (killSwitch, src) =>
          src.
            map(_ => initalDelay = 0.second)
            .andThen {
              case Failure(t) =>
                println(s"Scheduling restart in ${delay}")
                system.scheduler.scheduleOnce(delay) {
                  backoff(delay)
                }
              case Success(s) =>
                println(s"Projection stream ended.")
            }
          killSwitch
      }.map { _ =>
        println("stated")
      }.runAsync
        .recover {
          case ex: Throwable =>
            println("Task failed")
            println(s"Scheduling restart in ${delay}")
            system.scheduler.scheduleOnce(delay) {
              backoff(delay)
            }
        }
    }

    backoff(0.seconds)
  }

  println(500.millis.toMillis)

  superwiseWithBackoff2(start, 3.seconds, 20.second)

  system.scheduler.scheduleOnce(15.seconds) {
    x = 1
  }

  system.scheduler.scheduleOnce(30.seconds) {
    x = 0
  }

  system.scheduler.scheduleOnce(45.seconds) {
    x = 1
  }

}
