package ch3

import akka.actor.{Props, ActorSystem}
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}

/**
  * Created by User on 31.01.2016.
  */
class EchoActorTest extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  "EchoActor" must {
    "Reply with the same message it receives without asking" in {
      val echo = system.actorOf(Props[EchoActor], "echo2")
      echo.tell("some message", testActor)
      expectMsg("some message")
    }
  }

}
