/*
 *  Calculation of Pi using quadrature realized with a fork/join approach with threads.
 *
 *  Copyright © 2009,2011 Russel Winder
 */
public class Pi_CS_ThreadsAnonymousDelegateSyncObject {
  private class Accumulator {
    private double sum = 0.0 ;
    public void add ( double value ) { lock ( this ) { sum += value ; } }
    public double getSum ( ) { return sum ; }
  }
  private static void execute ( int numberOfTasks ) {
    const long n = 1000000000L ;
    const double delta = 1.0 / n ;
    long startTimeHundredsOfNanos = System.DateTime.Now.Ticks ;
    long sliceSize = n / numberOfTasks ;
    System.Threading.Thread[] threads = new System.Threading.Thread [ numberOfTasks ] ;
    Accumulator accumulator = new Accumulator ( ) ;
    for ( int i = 0 ; i < numberOfTasks ; ++i ) {
      long start = 1 + i * sliceSize ;
      long end =  ( i + 1 ) * sliceSize ;
      threads[i] = new System.Threading.Thread ( new System.Threading.ThreadStart ( delegate ( ) {
            double localSum = 0.0 ;
            for ( long j = start ; j <= end ; ++j ) {
              double x = ( j - 0.5 ) * delta ;
              localSum += 1.0 / ( 1.0 + x * x ) ;
            }
            accumulator.add ( localSum ) ;
          }
          ) ) ;
    }
    foreach ( System.Threading.Thread t in threads ) { t.Start ( ) ; }
    foreach ( System.Threading.Thread t in threads ) { t.Join ( ) ; }
    double pi = 4.0 * delta * accumulator.getSum ( ) ;
    double elapseTime = ( System.DateTime.Now.Ticks - startTimeHundredsOfNanos ) / 1e7 ;
    System.Console.WriteLine ( "==== C# Threads Anonymous Delegate Sync Object pi = " + pi ) ;
    System.Console.WriteLine ( "==== C# Threads Anonymous Delegate Sync Object iteration count = " + n ) ;
    System.Console.WriteLine ( "==== C# Threads Anonymous Delegate Sync Object elapse = " + elapseTime ) ;
    System.Console.WriteLine ( "==== C# Threads Anonymous Delegate Sync Object processor count = " + System.Environment.ProcessorCount ) ;
    System.Console.WriteLine ( "==== C# Threads Anonymous Delegate Sync Object thread count = " + numberOfTasks ) ;
  }
  public static void Main ( string[] args ) {
    Pi_CS_ThreadsAnonymousDelegateSyncObject.execute ( 1 ) ;
    System.Console.WriteLine ( ) ;
    Pi_CS_ThreadsAnonymousDelegateSyncObject.execute ( 2 ) ;
    System.Console.WriteLine ( ) ;
    Pi_CS_ThreadsAnonymousDelegateSyncObject.execute ( 8 ) ;
    System.Console.WriteLine ( ) ;
    Pi_CS_ThreadsAnonymousDelegateSyncObject.execute ( 32 ) ;
  }
}
