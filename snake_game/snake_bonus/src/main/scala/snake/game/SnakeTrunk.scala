package snake.game

case class SnakeTrunk(var x: Int = 0,
                      var y: Int = 0,
                      `type`: GridType = Empty().asInstanceOf[GridType] )
  extends Cell(`type`) with Coordinates {

  override def toString: String = " [" + x + ", " + y + ", " + cellType.toString + "] "
  def inTheSamePositionAs(thatOrNone: Option[SnakeTrunk]): Boolean = {
    val that = thatOrNone.getOrElse(SnakeTrunk(-1, -1))
    that.x == this.x && that.y == this.y
  }
}
