package scatter_gather

import akka.actor.{Actor, ActorRef}

class GetSpeed(pipe: ActorRef) extends Actor {

  override def receive: Receive = {
    case msg: PhotoMessage =>
      pipe ! msg.copy(speed = Some(1))
  }

}
