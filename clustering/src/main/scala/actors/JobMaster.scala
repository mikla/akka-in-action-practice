package actors

import actors.JobMaster.{StartJob, Work}
import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

class JobMaster
  extends Actor
    with ActorLogging
    with CreateWorkerRouter {

  val router = createWorkerRouter

  override def receive: Receive = ???

  def idle: Receive = {
    case StartJob(jobName, text) =>
      val textParts = text.grouped(10).toVector
      val cancel = context.system.scheduler.schedule(0.millis, 1000.millis, router, Work(jobName, self))
      context.become(working(jobName, sender(), cancel))
  }

  def working(str: String, ref: ActorRef, cancellable: Cancellable): Receive = {
    case _ => ()
  }

}

object JobMaster {

  case class StartJob(jobName: String, text: String)
  case class Work(jobName: String, reply: ActorRef)

}