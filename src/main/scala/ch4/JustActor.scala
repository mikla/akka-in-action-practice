package ch4

import akka.actor.{PoisonPill, Props, ActorSystem, Actor}

class JustActor(param: String) extends Actor {

  println(s"Calling constructor $param")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    // stops all child actors and call postStop()
    println("preRestart")
    super.preRestart(reason, message)
  }

  override def preStart() = {
    println("preStart..")
  }

  override def receive: Receive = {
    case "restart" =>
      throw new IllegalStateException("force restart")
    case _ =>
      println("something received")
  }

  override def postStop() = {
    println("postStop...")
  }

  override def postRestart(reason: Throwable): Unit = {
    println("postRestart...")
    super.postRestart(reason)
  }

}

object JustActor extends App {

  val system = ActorSystem("testSystem")

  val justActorRef = system.actorOf(Props(new JustActor("param")))

  justActorRef ! "restart"


  Thread.sleep(2000)

  justActorRef ! PoisonPill // or system.stop(justActorRef)

  system.shutdown()

}
