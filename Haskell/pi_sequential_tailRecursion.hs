--  Haskell implementation of π by Quadrature using a classic tail recursive approach.
--
--  Copyright © 2009–2011, 2013, 2015  Russel Winder

module Main where

import Output (out)

piIter :: Int -> Double -> Double -> Double
piIter 0 delta accumulator = 4.0 * delta * accumulator
piIter n delta accumulator = piIter (n - 1) delta (accumulator + 1.0 / (1.0 + x * x))
    where
      x = ((fromIntegral n) - 0.5) * delta

main :: IO()
main = out "Sequential Tail Recursion" pi n
  where
    n = 100000000 -- 10 times fewer than C++ for speed reasons.
    delta = 1.0 / (fromIntegral n)
    pi = piIter n delta 0.0
