import akka.actor.{FSM, Actor}

object Main extends App {
	
}

class Inventory() extends Actor with FSM[State, StateData] {
  startWith(StateOne, StateData("state1-data"))

  when(StateOne) {
    case Event(request: StateRequest, data: StateData) =>
      stay
    case Event(StateTwo, data: StateData) =>
      goto(StateTwo) using data // provide new state data

  }
  whenUnhandled {
    case Event(e, s) =>
      println("unhandled state {} {}", e, s)
      stay
  }
}

sealed trait State
case object StateOne extends State
case object StateTwo extends State

case class StateData(data: String)

case class StateRequest()
