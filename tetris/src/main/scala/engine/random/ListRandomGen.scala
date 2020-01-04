// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package engine.random

/** "Random" numbers generated from
  * a predefined sequence of numbers.
  *
  * @param numbers Sequence of integers to draw number from.
  */
class ListRandomGen(val numbers : Seq[Int]) extends RandomGenerator {

  var index = 0
  var last = 0

  override def randomInt(upTo: Int): Int = {
    val res = numbers(index) % upTo
    index = (index + 1) % numbers.length
    last = res ; res
  }

}
