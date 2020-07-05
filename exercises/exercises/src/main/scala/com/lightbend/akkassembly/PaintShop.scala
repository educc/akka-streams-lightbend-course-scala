package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Source}

class PaintShop(colorSet: Set[Color]) {

  val colors: Source[Color, NotUsed] = Source.cycle { () => colorSet.iterator}

  val paint: Flow[UnfinishedCar, UnfinishedCar, NotUsed] = {
    Flow[UnfinishedCar].zip {

      Source.cycle(() => {
        if (colorSet.isEmpty) throw new RuntimeException
        colorSet.iterator
      }).named("paint-stage-cycleSource")
    }.named("paint-stage-zipWith2")
      .map ( it =>
      it._1.copy(color = Option(it._2))
    ).named("paint-stage-map")
  }

}
