// scalastyle:off

package engine.random

import scala.util.Random

/** Random number generator used during actual gameplay.
  *
  * @param sRandom the Random object to use.
  *                You may seed this however you like.
  */
class ScalaRandomGen(sRandom: scala.util.Random) extends RandomGenerator {

  var last = 0
  def this() = this(new scala.util.Random())

  override def randomInt(upTo: Int): Int = {
    last = sRandom.nextInt(upTo) ; last
  }

}

object ScalaRandomGen {
  def apply(): ScalaRandomGen = new ScalaRandomGen(scala.util.Random)
}
