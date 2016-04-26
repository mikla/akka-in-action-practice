import akka.actor.{Actor, ActorSystem, Props}
import akka.agent.Agent

import scala.concurrent.ExecutionContext.Implicits.global

object Agents extends App {

  val system = ActorSystem("system")

  val stateAgent: Agent[Int] = Agent(0)

  val counter1Actor = system.actorOf(Props(new Counter1(stateAgent)))
  val counter2Actor = system.actorOf(Props(new Counter2(stateAgent)))

  counter1Actor ! "+"
  counter2Actor ! "+"
  counter2Actor ! "+"
  counter2Actor ! "+"
  counter2Actor ! "+"
  counter2Actor ! "+"
  counter2Actor ! "+"
  counter1Actor ! "+"
  counter1Actor ! "+"
  counter1Actor ! "+"

  readLine()

  counter1Actor ! "get" // should be 10

}

class Counter1(state: Agent[Int]) extends Actor {
  override def receive: Actor.Receive = {
    case "+" => state send (_ + 1)
    case "get" => println(state.get())
  }
}

class Counter2(state: Agent[Int]) extends Actor {
  override def receive: Actor.Receive = {
    case "+" => state send (_ + 1)
    case "get" => println(state.get())
  }
}
