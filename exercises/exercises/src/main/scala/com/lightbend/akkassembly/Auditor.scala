package com.lightbend.akkassembly

import akka.{Done, NotUsed}
import akka.event.LoggingAdapter
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class Auditor(implicit mat: Materializer) {

  val count: Sink[Any, Future[Int]] = Sink.fold(0) {
    case (sum , elem) => sum+1
  }

  def log(implicit adapter: LoggingAdapter): Sink[Any, Future[Done]] = Sink.foreach { it =>
    adapter.debug(it.toString)
  }

  def sample(simpleSize: FiniteDuration): Flow[Car, Car, NotUsed] = Flow[Car].takeWithin(simpleSize)

  def audit(source: Source[Car,NotUsed], sampleSize: FiniteDuration): Future[Int] = {
    source
      .via(sample((sampleSize)))
      .runFold(0) {
        case (sum, _) => sum +1
      }
  }
}
