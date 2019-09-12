package snake.logic

import snake.game.{Direction, East, Empty, North, SnakeBody, SnakeHead, South, West}

import scala.collection.mutable

case class Snake() {
  var body: mutable.MutableList[Coordinate] = mutable.MutableList[Coordinate]()

  val cutTail: () => Unit = () => body = body.dropRight(1)

  val moveEast: Int => Unit = (nrColumns: Int) => {
    body = Coordinate(body.head.x, (body.head.y+1) % nrColumns) +: body
    updateType(East())
  }
  val moveNorth: Int => Unit = (nrRows: Int) => {
    body = Coordinate(if (body.head.x-1 < 0) nrRows-1 else body.head.x-1, body.head.y) +: body
    updateType(North())
  }
  val moveWest: Int => Unit = (nrColumns: Int) => {
    body = Coordinate(body.head.x, if (body.head.y-1 < 0) nrColumns-1 else body.head.y-1) +: body
    updateType(West())
  }
  val moveSouth: Int => Unit = (nrRows: Int) => {
    body = Coordinate((body.head.x+1) % nrRows, body.head.y) +: body
    updateType(South())
  }

  def init(): Unit = {
    body += Coordinate(0, 2)
    body += Coordinate(0, 1)
    body += Coordinate()
    updateType()
  }

  def moveWithTail(nrRows: Int, nrColumns: Int): Unit = {
    body
      .head
      .cellType
      .asInstanceOf[SnakeHead]
      .direction match {
      case North() => moveNorth(nrRows)
      case East()  => moveEast(nrColumns)
      case West()  => moveWest(nrColumns)
      case South() => moveSouth(nrRows)
    }
  }

  def updateType(dir: Direction = East()): Unit = {

//    body.last.cellType match {
//      case SnakeHead(_) => body.init.drop(1).foreach(_.cellType = SnakeBody())
//      case Empty() | SnakeBody(_) => body.tail.foreach(_.cellType = SnakeBody())
//    }
    body.tail.foreach { trunk =>
      trunk.cellType = SnakeBody()
    }
    body.head.cellType = SnakeHead(dir)

  }
}
