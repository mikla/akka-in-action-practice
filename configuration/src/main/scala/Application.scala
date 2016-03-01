import akka.actor.{Props, ActorSystem, ActorSelection}
import com.typesafe.config.ConfigFactory

object Application extends App {
	val config = ConfigFactory.load()

  println {
    config.getString("Application.version")
  }

  val myAppConfig = ConfigFactory.load("myapp")

  println {
    myAppConfig.getString("Application.version")
  }

  val configuredActorSystem = ActorSystem("myapp", myAppConfig)
  val loggedActorRef = configuredActorSystem.actorOf(Props[LoggedActor], "logged-actor")
  loggedActorRef ! "message"

}