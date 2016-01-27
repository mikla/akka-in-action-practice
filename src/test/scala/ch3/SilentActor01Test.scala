package ch3

import akka.actor.ActorSystem
import akka.testkit.{TestFSMRef, TestActorRef, TestKit}
import org.scalatest.{MustMatchers, WordSpecLike}

class SilentActor01Test extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  "A silent actor" must {

    "change state when it received message single-threaded" in {
      import SilentActorProtocol._

      val fsm = TestFSMRef(new SilentActor)
      val typed: TestActorRef[SilentActor] = fsm

      val silentActor = TestActorRef[SilentActor]
      silentActor ! SilentMessage("whisper")

      println(silentActor.underlying.currentMessage)
    }

    "change state when it received message multi-threaded" in {
      fail(???.toString)
    }

  }

}
