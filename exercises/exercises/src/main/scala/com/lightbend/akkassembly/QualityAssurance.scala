package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.{ActorAttributes, Supervision}
import akka.stream.scaladsl.Flow
import com.lightbend.akkassembly.QualityAssurance.CardFailedInspection

object QualityAssurance {

  class CardFailedInspection(car: UnfinishedCar) extends IllegalStateException("Car doesn't pass inspection")

}

class QualityAssurance {

  val decider: Supervision.Decider = {
    case _: CardFailedInspection => Supervision.resume
  }

  val inspect: Flow[UnfinishedCar, Car, NotUsed] = Flow[UnfinishedCar]
    .map { x =>
      if (x.wheels.size != 4 ||
        x.engine.isEmpty ||
        x.color.isEmpty) {
        throw new CardFailedInspection(x)
      }

      Car(SerialNumber(), x.color.get, x.engine.get, wheels = x.wheels, upgrade = Option.empty)
    }
    .collect { case x => x }
    .withAttributes {
      ActorAttributes.supervisionStrategy(decider)
    }

}
