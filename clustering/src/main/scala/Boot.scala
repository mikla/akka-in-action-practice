object Boot {

  import akka.actor._
  import akka.cluster._
  import com.typesafe.config._
  val seedConfig = ConfigFactory.load("seed")
  val seedSystem = ActorSystem("words", seedConfig)

  seedSystem.actorOf(Props(new ClusterDomainEventListener()))

}
