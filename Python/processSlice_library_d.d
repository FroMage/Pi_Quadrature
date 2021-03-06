/*
 *  A D function to calculate a slice of the overall calculation of π using quadrature.
 *
 *  Copyright © 2014–2015  Russel Winder
 */

import core.runtime: Runtime;

import std.algorithm: reduce;
import std.range: iota;

extern(C)
double processSlice(const int id, const int sliceSize, const double delta) {
  // This code does not need the D runtime to be initialized, but for consistency…
  Runtime.initialize();
  const sum = reduce!((double t, int i){
      immutable x = (i - 0.5) * delta;
      return t + 1.0 / (1.0 + x * x);
    })
    (0.0, iota(1 + id * sliceSize,  (id + 1) * sliceSize));
  Runtime.terminate();
  return sum;
}
