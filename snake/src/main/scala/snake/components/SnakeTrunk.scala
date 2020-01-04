package snake.components

case class SnakeTrunk(var x: Int = 0,
                      var y: Int = 0,
                      trunkType: GridType = SnakeHead().asInstanceOf[GridType] )
  extends Cell(trunkType) with Coordinates {

  override def toString: String = " [ (" + x + ", " + y + "), " + cellType.toString + " ] "
  def inTheSamePositionAs(thatOrNone: Option[SnakeTrunk]): Boolean = {
    val that = thatOrNone.getOrElse(SnakeTrunk(-1, -1))
    that.x == this.x && that.y == this.y
  }
}
