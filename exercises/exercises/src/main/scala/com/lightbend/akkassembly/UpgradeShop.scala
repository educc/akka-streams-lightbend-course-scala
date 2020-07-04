package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.{FlowShape, SourceShape, UniformFanInShape}
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Merge}

class UpgradeShop {


  val installUpgrades: Flow[UnfinishedCar, UnfinishedCar, NotUsed] = Flow.fromGraph(
    GraphDSL.create() {
      implicit builder: GraphDSL.Builder[NotUsed] =>
        import GraphDSL.Implicits._

        val balance = builder.add(Balance[UnfinishedCar](3))
        val merge = builder.add(Merge[UnfinishedCar](3))

        val flowStandard = Flow[UnfinishedCar]
        val flowSport = Flow[UnfinishedCar].map(it => it.copy(upgrade = Option(Upgrade.Sport)))
        val flowDx = Flow[UnfinishedCar].map(it => it.copy(upgrade = Option(Upgrade.DX)))

        balance ~> flowStandard
        balance ~> flowSport
        balance ~> flowDx


        UniformFanInShape(merge.out, balance.inlets(0), balance.inlets(1), balance.inlets(2))
        FlowShape(balance.in, merge.out)
    })

}
