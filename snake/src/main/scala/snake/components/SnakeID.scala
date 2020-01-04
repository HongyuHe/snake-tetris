package snake.components

abstract class SnakeID

case class AiSnake()    extends SnakeID
case class HostSnake()  extends SnakeID
case class RivalSnake() extends SnakeID
