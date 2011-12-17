--  Haskell implementation of Pi by Quadrature.  Parallel version using parMap.
--
--  Copyright © 2009–2011 Russel Winder

module Main where

import Data.Time.Clock ( getCurrentTime , diffUTCTime )
import GHC.Conc ( numCapabilities )
import Control.Parallel.Strategies ( rwhnf , parMap )

piIter :: Int -> Int -> Double -> Double -> Double
piIter  n to delta accumulator
    | n > to = 4.0 * delta * accumulator
    | otherwise = piIter ( n + 1 ) to delta ( accumulator + 1.0 / ( 1.0 + x * x ) )        
    where
      x = ( ( fromIntegral n ) - 0.5 ) * delta

piQuadSlice :: Double -> Int -> Int -> Double
piQuadSlice delta sliceSize index = piIter start end delta 0.0
    where
      start = 1 +  index * sliceSize
      end =  ( index + 1 ) * sliceSize

execute :: Int -> IO ( )
execute numberOfSlices = do
  let n = 1000000000
  let delta = 1.0 / ( fromIntegral n )
  startTime <- getCurrentTime
  let sliceSize = n `div` numberOfSlices
  let pi = sum ( parMap rwhnf ( piQuadSlice delta sliceSize ) [ 0 .. ( numberOfSlices - 1 ) ] )
  --  Don't get the time here since nothing has actually been computed as yet since pi has not been used.
  print ( "==== Haskell ParMap pi = " ++ show pi )
  print ( "==== Haskell ParMap iteration count = " ++ show n )
  endTime <- getCurrentTime
  print ( "==== Haskell ParMap elapse time = " ++ show ( diffUTCTime endTime startTime ) )
  print ( "==== Haskell ParMap slice count = " ++ show numberOfSlices )
  print ( "==== Haskell ParMap processor count = " ++ show numCapabilities )

main :: IO ( )
main = do
  execute 1
  print ""
  execute 2
  print ""
  execute 8
  print ""
  execute 32
