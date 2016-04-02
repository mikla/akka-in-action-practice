import akka.actor.{Actor, ActorSystem, Props}

object Test extends App {
  val system = ActorSystem("test")

  val root = system.actorOf(Props[Root], "root")
  root ! "create"

  readLine()
}

class Root extends Actor {

  override def receive: Receive = {
    case "create" =>
      val parent = context.actorOf(Props[Parent], "parent")
      parent ! "create"
    case "reply" =>
      println("got reply from child")
  }

}

class Parent extends Actor {
  override def receive: Actor.Receive = {
    case "create" =>
      val child = context.actorOf(Props[Child], "child")
      child ! "reply"
  }
}

class Child extends Actor {
  override def receive: Actor.Receive = {
    case "reply" =>
      val parentOfParent = context.actorSelection("../..")
      parentOfParent ! "reply"
  }
}

