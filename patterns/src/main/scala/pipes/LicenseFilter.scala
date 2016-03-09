package pipes

import akka.actor.{ActorRef, Actor}
import akka.actor.Actor.Receive

class LicenseFilter(pipe: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: Photo =>
      if (!msg.license.isEmpty)
        pipe ! msg
  }
}
