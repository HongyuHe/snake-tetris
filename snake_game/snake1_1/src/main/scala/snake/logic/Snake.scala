package snake.logic

import snake.game.{Direction, East, SnakeBody, SnakeHead}

import scala.collection.mutable

class Snake {
  val GrowOneTime = 3

  var growCounter = 0
  var droppedTail: Option[SnakeTrunk] = None
  var body: mutable.MutableList[SnakeTrunk] = mutable.MutableList[SnakeTrunk]()

  val grow: () => Unit = () => growCounter += GrowOneTime
  val cutTail: () => Unit = () => body = body.dropRight(1)
  lazy val isHeadBehindTail: Boolean = body.head inTheSamePositionAs droppedTail

  val moveEast:  Int => Unit = (boundary: Int) => body = SnakeTrunk(body.head.x, (body.head.y+1) % boundary) +: body
  val moveWest:  Int => Unit = (boundary: Int) => body = SnakeTrunk(body.head.x, if (body.head.y-1 < 0) boundary-1 else body.head.y-1) +: body
  val moveNorth: Int => Unit = (boundary: Int) => body = SnakeTrunk(if (body.head.x-1 < 0) boundary-1 else body.head.x-1, body.head.y) +: body
  val moveSouth: Int => Unit = (boundary: Int) => body = SnakeTrunk((body.head.x+1) % boundary, body.head.y) +: body

  // return dropped tail or None (if snake is growing)
  def move(moveToDir: Int => Unit, boundary: Int, headDir: Direction): Unit = {
    moveToDir(boundary)

    if (growCounter > 0) {
      growCounter -= 1
      droppedTail = None
    } else {
      val tail = body.last
      cutTail()
      droppedTail = Some(tail)
    }
    updateBodyTypes(headDir)

  }

  def updateBodyTypes(headDir: Direction = East()): Unit = {
    var interval = 1f / (body.length-1)
    var dis = 0f
    body.tail.foreach { trunk =>
      dis += interval
      trunk.cellType = SnakeBody(dis)
    }
    body.last.cellType = SnakeBody(1)
    body.head.cellType = SnakeHead(headDir)

  }
}

object Snake {
  def apply(): Snake = new Snake() {
    body += SnakeTrunk(0, 2)
    body += SnakeTrunk(0, 1)
    body += SnakeTrunk()
    updateBodyTypes()
  }
}
