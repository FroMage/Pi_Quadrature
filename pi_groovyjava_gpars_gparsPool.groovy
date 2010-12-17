#! /usr/bin/env groovy

/*
 *  Calculation of Pi using quadrature realized with a fork/join approach with GPars Parallelizer to
 *  partition the problem and hence harness all processors available to the JVM.
 *
 *  Copyright © 2010 Russel Winder
 */

@Grab ( 'org.codehaus.gpars:gpars:0.11' )

import groovyx.gpars.GParsPool

void execute ( final int numberOfTasks ) {
  GParsPool.withPool {
    final long n = 1000000000l
    final double delta = 1.0d / n
    final startTimeNanos = System.nanoTime ( )
    final long sliceSize = n / numberOfTasks
    final items = [ ] ; for ( i in 0 ..< numberOfTasks ) { items << i }
    final pi = 4.0d * delta * items.collectParallel { taskId ->
      ( new ProcessSlice ( taskId , sliceSize , delta ) ).compute ( )
    }.sumParallel ( )
    final double elapseTime = ( System.nanoTime ( ) - startTimeNanos ) / 1e9
    println ( '==== Groovy/Java GPars GParsPool pi = ' + pi )
    println ( '==== Groovy/Java GPars GParsPool iteration count = ' + n )
    println ( '==== Groovy/Java GPars GParsPool elapse = ' + elapseTime )
    println ( '==== Groovy/Java GPars GParsPool processor count = ' + Runtime.getRuntime ( ).availableProcessors ( ) )
    println ( '==== Groovy/Java GPars GParsPool task count = ' + numberOfTasks )
  }
}

execute ( 1 )
println ( )
execute ( 2 )
println ( )
execute ( 8 )
println ( )
execute ( 32 )
