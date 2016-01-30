package ch3

import akka.actor.{Actor, ActorLogging, ActorRef}
import ch3.Greeter01Protocol.Greeting

class Greeter01 extends Actor with ActorLogging {

  override def receive: Receive = {
    case Greeting(msg) =>
      log.info(msg)

  }

}

object Greeter01Protocol {
  case class Greeting(msg: String)
}