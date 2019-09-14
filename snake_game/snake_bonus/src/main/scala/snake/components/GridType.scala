// scalastyle:off

package snake.components

sealed abstract class GridType

case class SnakeHead(id: String = "normal",
                     direction: Direction) extends GridType

/** 0 is just before SnakeHead, 1.0 is tail */
case class SnakeBody(id: String = "normal",
                     distanceToHead: Float = 0f) extends GridType

case class Empty() extends GridType

case class Apple() extends GridType

case class Wall()  extends GridType
