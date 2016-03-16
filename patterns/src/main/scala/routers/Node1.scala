package routers

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory

object Node1 extends App {
  val node = "node-1"

  val system = ActorSystem(node, config)
  private lazy val config = ConfigFactory.load(node)

}
