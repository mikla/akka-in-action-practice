import com.typesafe.config.ConfigFactory

object Application extends App {
	val config = ConfigFactory.load()

  println {
    config.getString("Application.version")
  }

}