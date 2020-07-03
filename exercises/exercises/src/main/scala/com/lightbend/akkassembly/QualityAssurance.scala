package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.scaladsl.Flow

class QualityAssurance {

  val inspect : Flow[UnfinishedCar, Car, NotUsed] = Flow[UnfinishedCar]
    .collect {
      case x if x.wheels.size == 4 && x.engine.isDefined && x.color.isDefined =>
        Car(SerialNumber(), x.color.get, x.engine.get, wheels = x.wheels, upgrade = Option.empty)
    }

}
