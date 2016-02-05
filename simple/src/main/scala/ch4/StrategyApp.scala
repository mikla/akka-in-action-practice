package ch4

import akka.actor.SupervisorStrategy.{Decider, Restart, Stop}
import akka.actor.{OneForOneStrategy, ActorKilledException, ActorInitializationException, SupervisorStrategy}

object StrategyApp extends App {

  val defaultStrategy: SupervisorStrategy = {
    def defaultDecider: Decider = {
      case _: ActorInitializationException => Stop
      case _: ActorKilledException => Stop
      case _: Exception => Restart
    }
    OneForOneStrategy()(defaultDecider)
  }

}
