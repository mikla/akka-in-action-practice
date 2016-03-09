package scatter_gather

import java.util.Date

import akka.actor.{Actor, ActorRef}

class GetTime(pipe: ActorRef) extends Actor {

  override def receive: Receive = {
    case msg: PhotoMessage =>
      pipe ! msg.copy(creationTime = Some(new Date()))
  }

}
