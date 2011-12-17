#! /usr/bin/env python3

#  Calculation of Pi using quadrature. Sequential algorithm.  For loop with a range.
#
#  Copyright © 2008–2011 Russel Winder

import time

n = 10000000 # 100 times fewer due to speed issues
delta = 1.0 / n
startTime = time.time ( )
sum = 0.0
#  In Python 2 use of range is a very bad move in this sort of situation since it creates a list and with
#  numbers this big you really don't want to do that.  Use xrange or a while loop to avoid the memory usage
#  problems.  In Python 3 there is no xrange as range is an iterator which is exactly what we want.
for i in range ( 1 , n + 1 ) :
  x = ( i - 0.5 ) * delta
  sum += 1.0 / ( 1.0 + x * x )
pi = 4.0 * delta * sum
elapseTime = time.time ( ) - startTime
print ( "==== Python Sequential For/Range pi = " + str ( pi ) )
print ( "==== Python Sequential For/Range iteration count = " + str ( n ) )
print ( "==== Python Sequential For/Range elapse = " + str ( elapseTime ) )
