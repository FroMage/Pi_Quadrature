#! /usr/bin/env python3

#  Calculation of Pi using quadrature. Using the multiprocessing package to provide a process pool to enable
#  asynchronous function calls very akin to futures.
#
#  Copyright © 2008–2012 Russel Winder

from multiprocessing import Pool, cpu_count
from output import out
from time import time

import ctypes

def processSlice ( id , sliceSize , delta ) :
    return processSliceModule.processSlice ( id , sliceSize , delta )

def execute ( processCount ) :
    n = 1000000000
    delta = 1.0 / n
    startTime = time ( )
    sliceSize = n // processCount
    pool = Pool ( processes = processCount )
    results = [ pool.apply_async ( processSlice , args = ( i , sliceSize , delta ) ) for i in range ( 0 , processCount ) ]
    #pool.close ( )
    pi = 4.0 * delta * sum ( [ item.get ( ) for item in results ] )
    elapseTime = time ( ) - startTime
    out ( __file__ , pi , n , elapseTime , processCount , cpu_count ( ) )
    
if __name__ == '__main__' :
    processSliceModule = ctypes.cdll.LoadLibrary ( 'processSlice_cpp.so' )
    processSliceModule.processSlice.argtypes = [ ctypes.c_int , ctypes.c_int , ctypes.c_double ]
    processSliceModule.processSlice.restype = ctypes.c_double
    execute ( 1 )
    execute ( 2 )
    execute ( 8 )
    execute ( 32 )
