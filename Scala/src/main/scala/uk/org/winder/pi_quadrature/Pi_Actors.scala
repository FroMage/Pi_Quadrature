/*
 *  Calculation of Pi using quadrature realized with a scatter/gather approach using an actor system.
 *
 *  Copyright © 2009–2013  Russel Winder
 */

package uk.org.winder.pi_quadrature

import scala.actors.Actor

import Output.out

object Pi_Actors extends App {

  def execute(numberOfWorkerActors:Int) {
    val n = 1000000000
    val delta = 1.0 / n
    val startTimeNanos = System.nanoTime
    val sliceSize = n / numberOfWorkerActors
    val calculators = new Array[Actor](numberOfWorkerActors)
    val accumulator = Actor.actor {
      var sum = 0.0
      calculators.foreach(calculator => Actor.receive { case d => sum += d.asInstanceOf[Double] })
      val pi = 4.0 * delta * sum
      val elapseTime = (System.nanoTime - startTimeNanos) / 1e9
      out("Pi_Actors", pi, n, elapseTime, numberOfWorkerActors)
      sequencer ! 0
    }
    for (index <- calculators.indices) {
      calculators(index) = Actor.actor {
        val start = 1 + index * sliceSize
        val end = (index + 1) * sliceSize
        var sum = 0.0
        for (i <- start to end) {
          val x = (i - 0.5) * delta
          sum += 1.0 / (1.0 + x * x)
        }
        accumulator ! sum
      }
    }
  }

  val sequencer = Actor.actor {
    execute(1)
    Actor.receive { case d => }
    execute(2)
    Actor.receive { case d => }
    execute(8)
    Actor.receive { case d => }
    execute(32)
  }

}
