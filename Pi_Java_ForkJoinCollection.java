/*
 *  Calculation of Pi using quadrature realized with a fork/join approach.  This uses a collection to store
 *  the futures.
 *
 *  Copyright © 2009–2012 Russel Winder
 */

import java.util.ArrayList ;

import java.util.concurrent.Callable ;
import java.util.concurrent.ExecutionException ;
import java.util.concurrent.ForkJoinPool ;
import java.util.concurrent.Future ;

public class Pi_Java_ForkJoinCollection {
  private static void execute ( final int numberOfTasks ) {
    final int n = 1000000000 ;
    final double delta = 1.0 / n ;
    final long startTimeNanos = System.nanoTime ( ) ;
    final int sliceSize = n / numberOfTasks ;
    final ArrayList<Callable<Double>> callables = new ArrayList<Callable<Double>> ( ) ;
    for ( int i = 0 ; i < numberOfTasks ; ++i ) {
      final int taskId = i ;
      callables.add ( new Callable<Double> ( ) {
          @Override public Double call ( ) {
            final int start = 1 + taskId * sliceSize ;
            final int end = ( taskId + 1 ) * sliceSize ;
            double sum = 0.0 ;
            for ( int i = start ; i <= end ; ++i ) {
              final double x = ( i - 0.5 ) * delta ;
              sum += 1.0 / ( 1.0 + x * x ) ;
            }
            return sum ;
          }
        } ) ;
    }
    final ForkJoinPool pool = new ForkJoinPool ( numberOfTasks ) ;
    double sum = 0.0 ;
    for ( Future<Double> f : pool.invokeAll ( callables ) ) {
      try { sum += f.get ( ) ; }
      catch ( final InterruptedException ie ) { throw new RuntimeException ( ie ) ; } 
      catch ( final ExecutionException ee ) { throw new RuntimeException ( ee ) ; } 
    }
    pool.shutdown ( ) ;
    final double pi = 4.0 * delta * sum ;
    final double elapseTime = ( System.nanoTime ( ) - startTimeNanos ) / 1e9 ;
    JOutput.out ( "Java ForkJoin Collection" , pi , n , elapseTime , numberOfTasks ) ;
  }
  public static void main ( final String[] args ) {
    Pi_Java_ForkJoinCollection.execute ( 1 ) ;
    Pi_Java_ForkJoinCollection.execute ( 2 ) ;
    Pi_Java_ForkJoinCollection.execute ( 8 ) ;
    Pi_Java_ForkJoinCollection.execute ( 32 ) ;
  }
}
