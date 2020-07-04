package com.lightbend.akkassembly

import akka.NotUsed
import akka.stream.FlowShape
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Merge}

class UpgradeShop {


  val installUpgrades: Flow[UnfinishedCar, UnfinishedCar, NotUsed] = Flow.fromGraph(
    GraphDSL.create() {
      implicit builder: GraphDSL.Builder[NotUsed] =>
        import GraphDSL.Implicits._

        val balance = builder.add(Balance[UnfinishedCar](3))
        val merge = builder.add(Merge[UnfinishedCar](3))

        val flowStandard = Flow[UnfinishedCar].map(_.copy(upgrade = Option.empty))
        val flowSport = Flow[UnfinishedCar].map(it => it.copy(upgrade = Option(Upgrade.Sport)))
        val flowDx = Flow[UnfinishedCar].map(it => it.copy(upgrade = Option(Upgrade.DX)))
        balance ~> flowStandard ~> merge
        balance ~> flowSport ~> merge
        balance ~> flowDx ~> merge

        FlowShape(balance.in, merge.out)
    })

}
