import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, RecoveryCompleted}

class Calculator extends PersistentActor with ActorLogging {

  override def persistenceId: String = "my-calculator"

  var state = CalculatorResult()

  override def receiveCommand: Receive = {
    case Add(v) => persist(Added(v))(updateState)
    case Subtract(v) => persist(Subtracted(v))(updateState)
    case Divide(v) => persist(Divided(v))(updateState)
    case Multiply(v) => persist(Multiplied(v))(updateState)
    case PrintResult => println(s"Result is: ${state.result}")
    case GetResult => sender() ! state.result
    case Clear => persist(Reset)(updateState)
  }

  override def receiveRecover: Receive = {
    case event: Event => updateState(event)
    case RecoveryCompleted => log.info("Calculator recovery completed")
  }

  val updateState: Event => Unit = {
    case Reset => state = state.reset
    case Added(v) => state = state.add(v)
    case Subtracted(v) => state = state.subtract(v)
    case Divided(v) => state = state.divide(v)
    case Multiplied(v) => state = state.multiply(v)
  }

}
