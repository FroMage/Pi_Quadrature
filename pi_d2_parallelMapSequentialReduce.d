/*
 *  A D program to calculate Pi using quadrature as a parallel map algorithm.
 *
 *  Copyright © 2010–2012 Russel Winder
 */

import std.algorithm ;
import std.datetime ;
import std.parallelism ;
import std.stdio ;
import std.typecons ;

alias  Tuple ! ( int , int , double ) ParameterType ;

double partialSum ( immutable ParameterType data ) { 
  immutable start = 1 + data[0] * data[1] ;
  immutable end = ( data[0] + 1 ) * data[1] ;
  auto sum = 0.0 ;
  foreach ( i ; start .. end + 1 ) {
    immutable x = ( i - 0.5 ) * data[2] ;
    sum += 1.0 / ( 1.0 + x * x ) ;
  }
  return sum ;
}

void execute ( immutable int numberOfTasks ) {
  immutable n = 1000000000 ;
  immutable delta = 1.0 / n ;
  StopWatch stopWatch ;
  stopWatch.start ( ) ;
  immutable sliceSize = n / numberOfTasks ;
  auto inputData = new ParameterType [ numberOfTasks ] ;
  foreach ( i ; 0 .. numberOfTasks ) { inputData[i] = tuple ( i , sliceSize , delta ) ; }
  //  It is important that this is an eager map and not a lazy map.
  immutable pi = 4.0 * delta * reduce ! ( ( a , b ) => a + b ) ( 0.0 , taskPool.amap ! ( partialSum ) ( inputData ) ) ;
  stopWatch.stop ( ) ;
  immutable elapseTime = stopWatch.peek ( ).hnsecs * 100e-9 ;
  writefln ( "==== D Parallel Map Sequential Reduce pi = %.18f" , pi ) ;
  writefln ( "==== D Parallel Map Sequential Reduce iteration count = %d" , n ) ;
  writefln ( "==== D Parallel Map Sequential Reduce elapse = %f" , elapseTime ) ;
  writefln ( "==== D Parallel Map Sequential Reduce task count = %d" , numberOfTasks ) ;
}

int main ( immutable string[] args ) {
  execute ( 1 ) ;
  writeln ( ) ;
  execute ( 2 ) ;
  writeln ( ) ;
  execute ( 8 ) ;
  writeln ( ) ;
  execute ( 32 ) ;
  return 0 ;
}
