/*
 *  A Chapel program to calculate π using quadrature as an explicitly batched, sequential reduce-based
 *  algorithm with a coforall for parallelism.
 *
 *  Copyright © 2009–2015  Russel Winder
 */

use Time;
use Output;

proc execute(numberOfTasks:int) {
  param n = 1000000000;
  const delta = 1.0 / n;
  var timer:Timer;
  timer.start();
  const sliceSize = n / numberOfTasks;
  const eachProcessor = 0..(numberOfTasks - 1);
  var results:[eachProcessor]real;
  proc partialSum(const id:int):real {
    return + reduce for i in (1 + id * sliceSize)..((id + 1) * sliceSize) do 1.0 / (1.0 + ((i - 0.5) * delta) ** 2);
  }
  coforall i in eachProcessor do results[i] = partialSum(i);
  const pi = 4.0 * delta * (+ reduce results);
  timer.stop();
  output_more("Coforall Batched", pi, n,  timer.elapsed(), numberOfTasks);
}

proc main() {
  execute(1);
  execute(2);
  execute(8);
  execute(32);
}
