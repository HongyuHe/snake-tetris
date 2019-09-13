package snake.logic

import scala.collection.mutable
import snake.logic.Snake._
import snake.game.{Direction, East, Empty, GridType, North, SnakeBody, SnakeHead, South, West}

class Snake {

  var growCounter = 0
  var droppedTail: Option[SnakeTrunk] = None                                        // drop tail if snake is not growing
  val grow: () => Unit = () => growCounter += GrowPerTime
  var body: mutable.Buffer[SnakeTrunk] = mutable.Buffer[SnakeTrunk]()
  lazy val isHeadBehindTail: Boolean = body.head inTheSamePositionAs droppedTail

  def move(gridBoundaryOfCurrentDir: Int) (dir: Direction): Unit = {
    val moveToEast : Int => Unit = (boundary: Int) => body = SnakeTrunk(body.head.x, (body.head.y + 1) % boundary) +: body
    val moveToSouth: Int => Unit = (boundary: Int) => body = SnakeTrunk((body.head.x + 1) % boundary, body.head.y) +: body
    val moveToWest : Int => Unit = (boundary: Int) => body = SnakeTrunk(body.head.x, if (body.head.y - 1 < 0) boundary - 1 else body.head.y - 1) +: body
    val moveToNorth: Int => Unit = (boundary: Int) => body = SnakeTrunk(if (body.head.x - 1 < 0) boundary - 1 else body.head.x - 1, body.head.y) +: body

    dir match {
      case East()  => moveToEast  (gridBoundaryOfCurrentDir)
      case West()  => moveToWest  (gridBoundaryOfCurrentDir)
      case North() => moveToNorth (gridBoundaryOfCurrentDir)
      case South() => moveToSouth (gridBoundaryOfCurrentDir)
    }
    cutTail()
    updateHeadAndBodyTypes(dir)
  }

  def updateHeadAndBodyTypes(headDir: Direction = East()): Unit = {
    var interval = 1f / (body.length - 1)
    var distance = 0f
    body.tail.foreach { trunk =>
      distance += interval
      trunk.cellType = SnakeBody(distance)
    }
    body.last.cellType = SnakeBody(1)
    body.head.cellType = SnakeHead(headDir)
  }

  def copyTo(that: Snake): Unit = {
    that.body.clear()
    for (i <- body.indices) {
      that.body += SnakeTrunk(this.body(i).x, this.body(i).y, this.body(i).cellType)
    }
    that.growCounter = this.growCounter
    that.droppedTail = this.droppedTail
  }

  private[this] def cutTail(): Unit = {
    if (growCounter > 0) {
      growCounter -= CutPerTime
      droppedTail = None
    } else {
      val tail = body.last
      body = body.dropRight(1)
      droppedTail = Some(tail)
    }
  }
}

object Snake {
  val CutPerTime  = 1
  val GrowPerTime = 3

  def apply(): Snake = new Snake() {
    body += SnakeTrunk(0, 2) += SnakeTrunk(0, 1) += SnakeTrunk()
    updateHeadAndBodyTypes()
  }
}
