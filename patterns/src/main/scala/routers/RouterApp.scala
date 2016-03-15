package routers

import akka.actor._
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory

object RouterApp extends App {

  val config = ConfigFactory.load()
  val system = ActorSystem("system", config)

  val router = system.actorOf(FromConfig.props(Props(new JustActor())), "poolRouter")

  router ! PoisonPill

  (1 to 100).foreach { i =>
    router ! i
  }

}

class JustActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case msg =>
      log.debug("message got: " + msg)
  }
}