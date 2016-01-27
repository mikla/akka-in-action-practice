package ch3

import akka.actor.Actor

class SilentActor extends Actor {

  override def receive: Receive = {
    case msg => ()
  }

}
