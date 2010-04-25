#! /usr/bin/env python
# -*- mode:python; coding:utf-8; -*-

#  Calculation of Pi using quadrature. Sequential algorithm.
#
#  Copyright © 2008-10 Russel Winder

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
pi = 4.0 * sum * delta
elapseTime = time.time ( ) - startTime
print "==== Python Sequential While pi =" , pi
print "==== Python Sequential While iteration count =" , n
print "==== Python Sequential While elapse =" , elapseTime
