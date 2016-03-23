package channels

import akka.actor.Actor

class DeliverOrderActor extends Actor {

  override def receive: Receive = {
    case msg =>
      println(msg)
  }
}

case class Order(order: Int)
