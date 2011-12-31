#! /usr/bin/env python3

#  Calculation of Pi using quadrature. Sequential algorithm.  While loop.
#
#  Copyright © 2008–2011 Russel Winder

import time

n = 10000000 # 100 times fewer due to speed issues
delta = 1.0 / n
startTime = time.time ( )
sum = 0.0
i = 1
while i < n + 1 :
  x = ( i - 0.5 ) * delta
  sum += 1.0 / ( 1.0 + x * x )
  i += 1
pi = 4.0 * delta * sum
elapseTime = time.time ( ) - startTime
print ( "==== Python Sequential While Multiply pi = " + str ( pi ) )
print ( "==== Python Sequential While Multiply iteration count = " + str ( n ) )
print ( "==== Python Sequential While Multiply elapse = " + str ( elapseTime ) )