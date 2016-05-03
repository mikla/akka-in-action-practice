import akka.actor.{ActorSystem, Props}
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
  val loggedActorRef = configuredActorSystem.actorOf(Props[LoggingReceiveActor], "logged-actor")
  loggedActorRef ! "message"

}

