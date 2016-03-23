package channels

import akka.actor.{Props, ActorSystem}

object ChannelApp extends App {

  val system = ActorSystem("channel-system")

  val deliverOrderRef = system.actorOf(Props[DeliverOrderActor])

  system.eventStream.subscribe(deliverOrderRef, classOf[Order])

  system.eventStream.publish("1")
  system.eventStream.publish(Order(1))

}
