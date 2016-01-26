package ch2

import java.util.UUID

import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration
import scala.concurrent.duration.FiniteDuration
import scala.util.Success

object TestActorApp extends App {

  import akka.pattern.ask

  implicit val timeout: Timeout = FiniteDuration(1, duration.MINUTES)

  implicit val system = ActorSystem("testActorApp")

  val firstActor = system.actorOf(Props(new PingPongActor), "firstActor")

  firstActor ! "create"
  firstActor ! "ping"

  firstActor ! "create"
  firstActor ! "ping"

  firstActor.ask("create") onComplete {
    case Success(r) =>
      println(r)
    case _ => sys.error("err")
  }

  readLine()

}

class PingPongActor extends Actor {

  override def preStart(): Unit = {
    println("Instance of FirstActor creating...")
  }

  override def receive: Receive = {
    case "create" =>
      context.actorOf(Props(new PingPongChildActor), s"actor${UUID.randomUUID()}")
      sender() ! "created"

    case "ping" =>
      context.children.foreach(_ ! "ping")

    case "pong" =>
      println("pong received")
  }

}

class PingPongChildActor extends Actor {

  override def preStart(): Unit = {
    println("Instance of SecondActor creating...")
  }

  override def receive: Receive = {
    case "ping" =>
      sender() ! "pong"
  }

}
