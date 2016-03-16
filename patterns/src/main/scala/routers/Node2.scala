package routers

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object Node2 extends App {
  val node = "node-2"

  val system = ActorSystem(node, config)
  private lazy val config = ConfigFactory.load(node)
}
