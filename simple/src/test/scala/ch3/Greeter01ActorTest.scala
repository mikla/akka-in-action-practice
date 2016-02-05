package ch3

import akka.actor.{Props, ActorSystem}
import akka.testkit.{EventFilter, CallingThreadDispatcher, TestKit}
import ch3.Greeter01Protocol.Greeting
import com.typesafe.config.ConfigFactory
import org.scalatest.{MustMatchers, WordSpecLike}

class Greeter01ActorTest
  extends TestKit(Greeter01ActorTest.testSystem)
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  "The greeter" must {

    "say hello" in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props[Greeter01].withDispatcher(dispatcherId)
      val greeter = system.actorOf(props)

      val message = "Hello"

      EventFilter.info(message = message, occurrences = 1).intercept {
        greeter ! Greeting(message)
      }

    }

  }

}

object Greeter01ActorTest {
  val testSystem = {
  val config = ConfigFactory.parseString(
      """
         akka.loggers = [akka.testkit.TestEventListener]
      """)
    ActorSystem("testsystem", config)
  }
}