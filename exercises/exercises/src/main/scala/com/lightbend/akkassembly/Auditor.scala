package com.lightbend.akkassembly

import akka.Done
import akka.event.LoggingAdapter
import akka.stream.scaladsl.Sink

import scala.concurrent.Future

class Auditor {

  val count: Sink[Any, Future[Int]] = Sink.fold(0) {
    case (sum , elem) => sum+1
  }

  def log(implicit adapter: LoggingAdapter): Sink[Any, Future[Done]] = Sink.foreach { it =>
    adapter.debug(it.toString)
  }
}
