// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package test.util

import engine.random.RandomGenerator

/** "Random" number generator used during testing.
  *
  * @param curNumber
  */
class TestRandomGen(var curNumber: Int) extends RandomGenerator {

  def last: Int = curNumber

  override def randomInt(upTo: Int): Int = {
    curNumber % upTo
  }

}
