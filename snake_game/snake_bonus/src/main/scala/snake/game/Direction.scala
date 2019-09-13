// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package snake.game

/** Given a direction `d` : you can decide
  * which one it is as follows:
  *
  * {{{
  * d match {
  *   case East() => ...
  *   case North() => ...
  *   case West() => ...
  *   case South() => ...
  * }
  * }}}
  */
sealed abstract class Direction {
  def opposite : Direction
}

case class East()   extends Direction  { def opposite = West()  }
case class North()  extends Direction  { def opposite = South() }
case class West()   extends Direction  { def opposite = East()  }
case class South()  extends Direction  { def opposite = North() }
