package events

import java.time.ZonedDateTime

case class Event(host: String,
  service: String,
  state: State,
  time: ZonedDateTime,
  description: String,
  tag: Option[String] = None,
  metric: Option[Double] = None) {

  override def toString: String =
    s"($service,$state,$time,$description)"

}

case class State(state: String)

case class LogReceipt(logId: String, written: Long)
case class ParseError(logId: String, msg: String)