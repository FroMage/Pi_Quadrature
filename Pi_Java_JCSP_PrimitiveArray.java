/*
 *  Calculation of Pi using quadrature realized with a fork/join approach with JCSP to partition the problem
 *  and hence harness all processors available to the JVM.
 *
 *  Copyright © 2009–2011 Russel Winder
 */

import org.jcsp.lang.Channel ;
import org.jcsp.lang.CSProcess ;
import org.jcsp.lang.One2OneChannel ;
import org.jcsp.lang.Parallel ;

public class Pi_Java_JCSP_PrimitiveArray {
  private static void execute ( final int numberOfTasks ) {
    final int n = 1000000000 ;
    final double delta = 1.0 / n ;
    final long startTimeNanos = System.nanoTime ( ) ;
    final int sliceSize = n / numberOfTasks ;
    final One2OneChannel<Double>[] channels = Channel.one2oneArray ( numberOfTasks ) ;
    final CSProcess[] processes = new CSProcess[numberOfTasks + 1] ;
    for ( int i = 0 ; i < numberOfTasks ; ++i ) {
      final int taskId = i ;
      processes[taskId] = new CSProcess ( ) {
        @Override public void run ( ) {
          final int start = 1 + taskId * sliceSize ;
          final int end = ( taskId + 1 ) * sliceSize ;
          double sum = 0.0 ;
          for ( int i = start ; i <= end ; ++i ) {
            final double x = ( i - 0.5 ) * delta ;
            sum += 1.0 / ( 1.0 + x * x ) ;
          }
          channels[taskId].out ( ).write ( sum ) ;
        }
      } ;
    }
    processes[numberOfTasks] = new CSProcess ( ) {
      @Override public void run ( ) {
        double sum = 0.0 ;
        for ( One2OneChannel<Double> c : channels ) { sum += c.in ( ).read ( ) ; }
        final double pi = 4.0 * delta * sum ;
        final double elapseTime = ( System.nanoTime ( ) - startTimeNanos ) / 1e9 ;
        System.out.println ( "==== Java JCSP Primitive Array pi = " + pi ) ;
        System.out.println ( "==== Java JCSP Primitive Array iteration count = " + n ) ;
        System.out.println ( "==== Java JCSP Primitive Array elapse = " + elapseTime ) ;
        System.out.println ( "==== Java JCSP Primitive Array processor count = " + Runtime.getRuntime ( ).availableProcessors ( ) ) ;
        System.out.println ( "==== Java JCSP Primitive Array task count = " + numberOfTasks ) ;
      }
    } ;
    ( new Parallel ( processes ) ).run ( ) ;
  }
  public static void main ( final String[] args ) {
    Pi_Java_JCSP_PrimitiveArray.execute ( 1 ) ;
    System.out.println ( ) ;
    Pi_Java_JCSP_PrimitiveArray.execute ( 2 ) ;
    System.out.println ( ) ;
    Pi_Java_JCSP_PrimitiveArray.execute ( 8 ) ;
    System.out.println ( ) ;
    Pi_Java_JCSP_PrimitiveArray.execute ( 32 ) ;
  }
}
