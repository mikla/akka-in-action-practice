package ch3

import akka.actor.{ActorRef, Actor}
import ch3.Kiosk01Protocol.Game

class Kiosk01(nextKiosk: ActorRef) extends Actor {

  override def receive: Receive = {

    case game@Game(_, tickets) =>
      nextKiosk ! game.copy(tickets = tickets.tail)

  }

}

object Kiosk01Protocol {

  case class Ticket(seat: Int)
  case class Game(name: String, tickets: Seq[Ticket])

}
