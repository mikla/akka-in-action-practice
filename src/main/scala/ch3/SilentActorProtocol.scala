package ch3

import akka.actor.ActorRef

object SilentActorProtocol {

  case class SilentMessage(msg: String)
  case class GetState(actor: ActorRef)

}
