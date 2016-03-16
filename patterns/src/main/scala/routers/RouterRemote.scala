package routers

import akka.actor.{Props, ActorSystem, AddressFromURIString}
import akka.remote.routing.RemoteRouterConfig
import akka.routing.RoundRobinPool
import com.typesafe.config.ConfigFactory

object RouterRemote extends App {

  val config = ConfigFactory.load()
  val system = ActorSystem("system", config)

  val addresses = Seq(
    AddressFromURIString("akka.tcp://node-1@0.0.0.0:2551"),
    AddressFromURIString("akka.tcp://node-2@0.0.0.0:2552")
  )

  val routerRemote = system.actorOf(
    RemoteRouterConfig(RoundRobinPool(5), addresses).props(
      Props(new JustActor())
    ), "poolRouter-remote"
  )

  (1 to 100).foreach { i =>
    routerRemote ! i
  }

}
