/*
 *  A C++ program to calculate Pi using quadrature as anMPI-based algorithm.
 *
 *  Copyright © 2009--2011 Russel Winder
 */

#include <iostream>
#include <iomanip>
#include <boost/mpi.hpp>
#include "microsecondTime.h"

int main ( int ac , char * * av ) { // MPI requires writeable access to these parameters :-(
  const int n = 1000000000 ;
  const double delta = 1.0 / n ;
  const long long startTimeMicros = microsecondTime ( ) ;
  boost::mpi::environment environment ( ac , av ) ;
  boost::mpi::communicator world ;
  const int nProcessors = world.size ( ) ;
  const int myId = world.rank ( ) ;
  const int sliceSize = n / nProcessors ;
  const int start = 1 + myId * sliceSize ;
  const int end = ( myId + 1 ) * sliceSize ;
  double localSum = 0.0 ;
  for ( int i = start ; i <= end ; ++i ) {
    const double x = ( i - 0.5 ) * delta ;
    localSum += 1.0 / ( 1.0 + x * x ) ;
  }
  double sum ;
  boost::mpi::reduce ( world , localSum , sum , std::plus<double> ( ) , 0 ) ;
  if ( myId == 0 ) {
    const double pi = 4.0 * sum * delta ;
    const double elapseTime = ( microsecondTime ( ) - startTimeMicros ) / 1e6 ;
    std::cout << "==== C++ Boost MPI pi = " << std::setprecision ( 18 ) << pi << std::endl ;
    std::cout << "==== C++ Boost MPI iteration count = " << n << std::endl ;
    std::cout << "==== C++ Boost MPI elapse = " << elapseTime << std::endl ;
    std::cout << "==== C++ Boost MPI processor count = " << nProcessors << std::endl ; 
  }
  return 0 ;
}
