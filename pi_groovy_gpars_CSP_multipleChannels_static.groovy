#! /usr/bin/env groovy

/*
 *  Calculation of Pi using quadrature realized with a fork/join approach with GPars CSP to partition the
 *  problem and hence harness all processors available to the JVM.
 *
 *  Copyright © 2010–2012 Russel Winder
 */

import org.jcsp.lang.Channel
import org.jcsp.lang.CSProcess

import groovyx.gpars.csp.PAR

void execute ( final int numberOfTasks ) {
  final int n = 1000000000i
  final double delta = 1.0d / n
  final startTimeNanos = System.nanoTime ( )
  final int sliceSize = n / numberOfTasks
  final channels = Channel.one2oneArray ( numberOfTasks )
  final processes = [ ]
  for ( int i in 0i ..< numberOfTasks ) { 
    final int taskId = i
    processes << new CSProcess ( ) {
      @Override public void run ( ) {
        channels[taskId].out ( ).write ( PartialSum.compute ( taskId , sliceSize , delta ) )
      }
    }
  }
  processes << new CSProcess ( ) {
    @Override public void run ( ) {
      double sum = 0.0d
      for ( c in channels ) { sum += (double) c.in ( ).read ( ) }
      final double pi = 4.0d * delta * sum
      final elapseTime = ( System.nanoTime ( ) - startTimeNanos ) / 1e9
      Output.out ( getClass ( ).name , pi , n , elapseTime , numberOfTasks )
    }
  } ;
  ( new PAR ( processes ) ).run ( )
}

execute 1
execute 2
execute 8
execute 32