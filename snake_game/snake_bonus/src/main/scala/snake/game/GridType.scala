// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package snake.game

sealed abstract class GridType

case class SnakeHead(direction: Direction) extends GridType

/** 0 is just before SnakeHead, 1.0 is tail */
case class SnakeBody(distanceToHead: Float = 0f) extends GridType

case class Empty() extends GridType

case class Apple() extends GridType
