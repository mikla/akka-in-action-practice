import akka.actor.Actor

class RemoteDeployedActor extends Actor {

  override def receive: Receive = {
    case msg =>
      println("Remote Deployed actor: " + msg)
  }

}
