package ch3

import akka.actor.{Props, ActorSystem}
import akka.testkit.TestKit
import org.scalatest.{MustMatchers, WordSpecLike}


class Kiosk01Test extends TestKit(ActorSystem("testSystem"))
  with WordSpecLike
  with MustMatchers
  with StopSystemAfterAll {

  "A sending actor" must {

    "send message to an when it has finished" in {
      import Kiosk01Protocol._

      val props = Props(new Kiosk01(testActor))
      val sendingActor = system.actorOf(props, "kiosk1")
      val tickets = Vector(Ticket(1), Ticket(2), Ticket(3))

      val game = Game("Lakers vs Bulls", tickets)

      sendingActor ! game

      expectMsgPF() {
        case Game(_, tkts) =>
          tkts.size must be (game.tickets.size - 1)
      }

    }

  }

}
