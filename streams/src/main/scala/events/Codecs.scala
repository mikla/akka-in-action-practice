package events

import java.time.ZonedDateTime

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

object Codecs {

  implicit val zonedDateTimeEncoder: Encoder[ZonedDateTime] = (a: ZonedDateTime) => a.toString.asJson

}
