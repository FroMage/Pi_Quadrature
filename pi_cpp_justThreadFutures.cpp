/*
 *  A C++ program to calculate Pi using quadrature.  This uses Anthony Williams' Just::Threads library which
 *  is an implementation of the threads specification of C++0x.
 *
 *  Copyright © 2009-10 Russel Winder
 */

#include <iostream>
#include <iomanip>
#include <thread>
#include<future>
#include "microsecondTime.h"

long double partialSum ( const long id , const long sliceSize , const long double delta ) {
  const long start = 1 + id * sliceSize ;
  const long end = ( id + 1 ) * sliceSize ;
  long double sum = 0.0 ;
  for ( long i = start ; i <= end ; ++i ) {
    const long double x = ( i - 0.5 ) * delta ;
    sum += 1.0 / ( 1.0 + x * x ) ;
  }
  return sum ;
}

void execute ( const int numberOfThreads ) {
  const long n = 1000000000l ;
  const long double delta = 1.0 / n ;
  const long long startTimeMicros = microsecondTime ( ) ;
  const long sliceSize = n / numberOfThreads ;
  std::shared_future<long double> futures [ numberOfThreads ] ;
  for ( int i = 0 ; i < numberOfThreads ; ++i ) {
    std::packaged_task<long double ( )> task ( std::bind ( partialSum , i , sliceSize , delta ) ) ;
    futures[i] = task.get_future ( ) ;
    std::thread thread ( std::move ( task ) ) ;
    thread.detach ( ) ;
  }
  long double sum = 0.0 ;
  for ( int i = 0 ; i < numberOfThreads ; ++i ) { sum += futures[i].get ( ) ; }
  const long double pi = 4.0 * sum * delta ;
  const long double elapseTime = ( microsecondTime ( ) - startTimeMicros ) / 1e6 ;
  std::cout << "==== C++ Just::Thread futures pi = " << std::setprecision ( 25 ) << pi << std::endl ;
  std::cout << "==== C++ Just::Thread futures iteration count = " << n << std::endl ;
  std::cout << "==== C++ Just::Thread futures elapse = " << elapseTime << std::endl ;
  std::cout << "==== C++ Just::Thread futures threadCount = " <<  numberOfThreads << std::endl ;
  std::cout << "==== C++ Just::Thread futures processor count = "  << std::thread::hardware_concurrency ( ) << std::endl ;
}

int main ( ) {
  execute ( 1 ) ;
  std::cout << std::endl ;
  execute ( 2 ) ;
  std::cout << std::endl ;
  execute ( 8 ) ;
  std::cout << std::endl ;
  execute ( 32 ) ;
  return 0 ;
}
