package ch4

import akka.actor._

class ActorWatcher(watch: ActorRef) extends Actor {

  context.watch(watch)

  override def receive: Receive = {
    case Terminated(ref) =>
      println(s"Actor { $ref } terminated")
  }

}

class WatchedActor extends Actor {

  override def receive: Receive = {
    case "terminate" =>
      throw new IllegalStateException("force termination")
  }

}

object ActorWatcher extends App {

  val system = ActorSystem("test-system")

  val watchedActor = system.actorOf(Props[WatchedActor])
  val watcherActor = system.actorOf(Props(new ActorWatcher(watchedActor)))

  system.stop(watchedActor)

  system.shutdown()

}
