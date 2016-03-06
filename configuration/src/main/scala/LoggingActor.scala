import akka.actor.Actor
import akka.event.Logging

class LoggingActor extends Actor {
  val log = Logging(context.system, this)

  override def receive: Receive = ???
}
