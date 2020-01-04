// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package engine.random

trait RandomGenerator {

  /** Gets a random integer from the range ``[0, upTo)``.
    *
    * @param upTo numbers from 0 up to
    *             and <b>not</b> including `upTo`
    * @return a random Int from from the given range
    */
  def randomInt(upTo: Int) : Int

}
