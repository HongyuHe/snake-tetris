package snake.components

import snake.components.Snake._
import scala.collection.mutable

class Snake {
  var id: SnakeID = HostSnake()
  var growCounter = 0
  var preDir: Direction = East()
  var droppedTail: Option[SnakeTrunk] = None                                      // drop tail if snake is not growing
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
    updateBodyWithHeadDir(dir)
  }

  def updateBodyWithHeadDir(headDir: Direction = East()): Unit = {
    var interval = 1f / (body.length - 1)
    var distance = 0f
    body.tail.foreach { trunk =>
      trunk.cellType = SnakeBody(id, distance)
      distance += interval
    }
    body.last.cellType = SnakeBody(id,1)
    body.head.cellType = SnakeHead(id, headDir)
  }

  def copyTo(that: Snake): Unit = {
    that.body.clear()
    for (i <- body.indices) {
      that.body += SnakeTrunk(this.body(i).x, this.body(i).y, this.body(i).cellType)
    }
    that.growCounter = this.growCounter
    that.droppedTail = this.droppedTail
  }

 def cutTail(): Unit = {
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

  def apply(rivalMode: Boolean = false): Snake = new Snake() {
    if (rivalMode) {
      id = RivalSnake()
      body += SnakeTrunk(4, 2) += SnakeTrunk(4, 1) += SnakeTrunk(4, 0)
    }
    else body += SnakeTrunk(0, 2) += SnakeTrunk(0, 1) += SnakeTrunk()
    updateBodyWithHeadDir()
  }
}
