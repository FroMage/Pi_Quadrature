/*
 *  A Chapel program to calculate π using quadrature as a sequential algorithm.
 *
 *  Copyright © 2009, 2011–2015  Russel Winder
 */

use Time;
use Output;

proc main() {
  param n = 1000000000;
  const delta = 1.0 / n;
  var timer:Timer;
  timer.start();
  var sum = 0.0;
  for i in 0..n {
    sum += 1.0 / (1.0 + ((i - 0.5) * delta) ** 2);
  }
  const pi = 4.0 * delta * sum;
  timer.stop();
  output("Sequential For Power", pi, n, timer.elapsed());
}
