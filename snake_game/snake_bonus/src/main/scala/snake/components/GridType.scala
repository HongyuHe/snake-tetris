// scalastyle:off

package snake.components

sealed abstract class GridType

case class SnakeHead(id: SnakeID = HostSnake(), direction: Direction = East()) extends GridType

/** 0 is just before SnakeHead, 1.0 is tail */
case class SnakeBody(id: SnakeID = HostSnake(), distanceToHead: Float = 0f) extends GridType

case class Empty() extends GridType

case class Apple() extends GridType

case class Wall() extends GridType

case class Bomb() extends GridType