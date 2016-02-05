package ch4

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{OneForOneStrategy, ActorSystem, Actor, Props}

class SupervisorActor(childProps: Props) extends Actor {

  override def supervisorStrategy = OneForOneStrategy() {
    case _: Error => Restart
  }

  val childActor = context.actorOf(childProps)

  override def receive: Receive = {
    case m => childActor forward m
  }

}

class ChildActor extends Actor {

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("preRestart child actor")
    super.preRestart(reason, message)
  }

  override def receive: Receive = {
    case "child" =>
      println("child actor received message")
    case "error" =>
      throw new Error("supervisor strategy apply")
  }
}

object SupervisorActor extends App {

  val system = ActorSystem("test-actor")

  val childProps = Props[ChildActor]

  val supervisorActor = system.actorOf(Props(new SupervisorActor(childProps)))

  supervisorActor ! "child"
  supervisorActor ! "error"

  system.shutdown()

}
