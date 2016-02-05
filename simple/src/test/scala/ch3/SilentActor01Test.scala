package ch3

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestActorRef, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}

class SilentActor01Test extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  "A silent actor" must {

    "change state when it received message single-threaded" in {
      import SilentActorProtocol._

      val silentActor = TestActorRef[SilentActor]
      silentActor ! SilentMessage("whisper")

      silentActor.underlyingActor.state must contain ("whisper")
    }

    "change state when it received message multi-threaded" in {
      import SilentActorProtocol._
      val silentActor = system.actorOf(Props[SilentActor], "s3")
      silentActor ! SilentMessage("whisper1")
      silentActor ! SilentMessage("whisper2")
      silentActor ! GetState(testActor)

      expectMsg(Vector("whisper1", "whisper2"))
    }

  }

}
