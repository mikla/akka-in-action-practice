import akka.actor._

import scala.concurrent.duration._

object Main extends App {

  val system = ActorSystem("fsm-actors")
  val inventory = system.actorOf(Props[Inventory])

  inventory ! StateRequest()
  inventory ! "ala"
  inventory ! StateTwo
  inventory ! StateRequest()

  readLine()

  inventory ! PoisonPill

  system.shutdown()
}

class Inventory()
  extends Actor
    with FSM[State, StateData]
    with ActorLogging {

  startWith(StateOne, StateData("state1-data"))

  when(StateOne) {
    case Event(request: StateRequest, data: StateData) =>
      println("Requesting state", data)
      stay
    case Event(StateTwo, data: StateData) =>
      println("Changing state", data)
      goto(StateTwo) using data.copy(data = "state2-data") // provide new state data
    case Event("ala", data) =>
      println("got ala")
      stay
  }

  when(StateTwo, stateTimeout = 10.seconds) {
    case Event(StateRequest, data) =>
      println("StateTwo got", data)
      stay
    case Event(StateTimeout, _) =>
      println("TimedOut. Going to StateOne")
      goto(StateOne)
  }

  whenUnhandled {
    case Event(e, s) =>
      println("unhandled state {} {}", e, s)
      stay
  }

  onTransition {
    case StateOne -> StateTwo =>
      println("onTransition, changing state")
  }

  onTermination {
    case StopEvent(_, _, _) =>
      println("Terminated")
  }

  initialize()
}

sealed trait State
case object StateOne extends State
case object StateTwo extends State

case class StateData(data: String)

case class StateRequest()
