package snake.logic

import snake.game._

case class SnakeTrunk(var x: Int = 0,
                      var y: Int = 0,
                      ct: GridType = Empty().asInstanceOf[GridType]
                     ) extends Cell(ct) {

  def copy(ct: GridType): SnakeTrunk = new SnakeTrunk(x, y) {
    val copyCT: GridType = ct match {
      case ct @ SnakeHead(_) => ct.copy()
      case ct @ SnakeBody(_) => ct.copy()
      case Empty()           => Empty()
      case Apple()           => Apple()
    }
  }

  def inTheSamePositionAs(that: SnakeTrunk): Boolean =
    that.x == this.x && that.y == this.y

  def inTheSamePositionAs(thatOrNone: Option[SnakeTrunk]): Boolean = {
    val that = thatOrNone.getOrElse(SnakeTrunk(-1, -1))
    that.x == this.x && that.y == this.y
  }

  override def toString: String = " [" + x + ", " + y + ", " + cellType.toString + "] "
}
