#! /usr/bin/env groovy

/*
 *  Calculation of Pi using quadrature realized with a basic sequential algorithm and enforcing primitive
 *  types throughout.
 *
 *  Copyright © 2008–2012 Russel Winder
 */

final int n = 100000000i // 10 times fewer than Java due to speed issues.
final double delta = 1.0d / n
final startTime = System.nanoTime ( )
final double pi = 4.0d * delta * ( 1i .. n ).sum { i -> 1.0d / ( 1.0d + ( ( i - 0.5d ) * delta ) ** 2i ) }
final elapseTime = ( System.nanoTime ( ) - startTime ) / 1e9
Output.out ( 'Groovy Sequential Primitives Sum Power' , pi , n , elapseTime )
