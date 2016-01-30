package ch3

import akka.actor.Actor
import ch3.SilentActorProtocol.{GetState, SilentMessage}

class SilentActor extends Actor {

  var internalState = Vector[String]()

  override def receive: Receive = {
    case SilentMessage(msg) =>
      internalState = internalState :+ msg

    case GetState(actor) =>
      actor ! internalState
  }

  def state = internalState

}
