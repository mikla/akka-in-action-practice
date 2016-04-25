import akka.actor.FSM.Event
import akka.actor._
import scala.concurrent.ExecutionContext.global

object Main extends App {

  val system = ActorSystem("fsm-actors")
  val inventory = system.actorOf(Props[Inventory])

  inventory ! StateRequest()
  inventory ! StateTwo
  inventory ! StateRequest()

  readLine()

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

  }
  when(StateTwo) {
    case Event(_, data) =>
      println("StateTwo got", data)
      stay
  }
  onTransition {
    case StateOne -> StateTwo =>
      println("onTransition, changing state")
  }

  whenUnhandled {
    case Event(e, s) =>
      println("unhandled state {} {}", e, s)
      stay
  }

  initialize()
}

sealed trait State
case object StateOne extends State
case object StateTwo extends State

case class StateData(data: String)

case class StateRequest()
