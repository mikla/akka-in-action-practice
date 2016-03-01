import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

class LoggedActor extends Actor with ActorLogging {

  override def preStart(): Unit = {
    context.system.scheduler.schedule(FiniteDuration(1, TimeUnit.SECONDS), FiniteDuration(2, TimeUnit.SECONDS), self, "from scheduler")
  }

  override def receive: Receive = LoggingReceive {
    case msg: String =>
      log.info(msg)
    case _ =>
      log.warning("Unhandled message")
  }

}
