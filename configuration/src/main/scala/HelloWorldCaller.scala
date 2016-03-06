import akka.actor.Actor.Receive
import akka.actor.{ActorLogging, Actor, ActorRef}

import scala.concurrent.duration.FiniteDuration

class HelloWorldCaller(timer: FiniteDuration, actor: ActorRef)
  extends Actor
  with ActorLogging {

  case class TimerTick(msg: String)

  override def preStart() = {
    super.preStart()
    implicit val ec = context.dispatcher
    context.system.scheduler.schedule(timer, timer, self, new TimerTick("everybody"))
  }

  override def receive: Receive = {
    case msg: String => log.info("received {}", msg)
    case tick: TimerTick => actor ! tick.msg
  }

}
