import akka.actor._
import akka.remote.RemoteScope
import com.typesafe.config._

object Frontend extends App {

  val system = ActorSystem("frontend", config)

  val simpleRef = system.actorSelection("akka.tcp://backend@0.0.0.0:2551/user/simple")
  val remoteActor = system.actorOf(Props[RemoteDeployedActor], "remoteDeployedActor")

  val remoteActorAddress = "akka.tcp://backend@0.0.0.0:2551"
  val progamRemoteActor = system.actorOf(Props[RemoteDeployedActor].withDeploy(
    Deploy(scope = RemoteScope(AddressFromURIString(remoteActorAddress)))
  ), "programRemoteDeploy")

  simpleRef ! "message"
  remoteActor ! "message"
  progamRemoteActor ! "program message"

  private lazy val config = ConfigFactory.load("frontend")

}

