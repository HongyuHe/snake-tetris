package snake.logic

import snake.game.{Empty, GridType}


case class SnakeTrunk(var x: Int = 0, var y: Int = 0,
                      `type`: GridType = Empty().asInstanceOf[GridType] ) extends Cell(`type`) {

  def inTheSamePositionAs(thatOrNone: Option[SnakeTrunk]): Boolean = {
    val that = thatOrNone.getOrElse(SnakeTrunk(-1, -1))
    that.x == this.x && that.y == this.y
  }
}