#! /usr/bin/env groovy 

/*
 *  Calculation of Pi using quadrature realized with a parallel algorithm based on using Futures.
 *
 *  Copyright © 2009–2012 Russel Winder
 */

import java.util.concurrent.Callable
import java.util.concurrent.ScheduledThreadPoolExecutor

def execute ( final int numberOfTasks ) {
  final int n = 1000000000i
  final double delta = 1.0d / n
  final startTimeNanos = System.nanoTime ( )
  final int sliceSize = n / numberOfTasks
  final executor = new ScheduledThreadPoolExecutor ( numberOfTasks )
  final futures = ( 0i ..< numberOfTasks ).collect { taskId ->
    executor.submit ( new Callable<Double> ( ) {
                        @Override public Double call ( ) { PartialSum.compute ( taskId , sliceSize , delta ) }
                      } )
  }
  final double pi = 4.0d * delta * futures.sum { f -> f.get ( ) }
  final elapseTime = ( System.nanoTime ( ) - startTimeNanos ) / 1e9
  executor.shutdown ( )
  Output.out ( getClass ( ).name , pi , n , elapseTime , numberOfTasks )
}

execute 1
execute 2
execute 8
execute 32