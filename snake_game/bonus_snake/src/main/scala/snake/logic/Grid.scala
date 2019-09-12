package snake.logic

import engine.random.RandomGenerator
import snake.game.{Direction, West, _}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class Cell(var cellType: GridType = Empty().asInstanceOf[GridType])

case class Coordinate(var x: Int = 0, var y: Int = 0) extends Cell {
  def isInSamePosition(that: Coordinate): Boolean =
    that.x == this.x && that.y == this.y
}

case class Grid(
                 nrRows: Int,
                 nrColumns: Int,
                 val randomGen: RandomGenerator) {

  private val MinimumBoundary = 3
  val cells: ArrayBuffer[ArrayBuffer[Cell]] = ArrayBuffer.fill(nrRows, nrColumns)(new Cell())
  val freeCellTable: mutable.Map[Int, Cell] = mutable.Map[Int, Cell]()
  val snake = Snake()
//  var apple = Coordinate()
  var growCounter = 0

  var nrFreeSpots = 0
  var hasApple = false // this can be replaced by checking apple.cellType == Empty()
  var isTurned = false
  var isGameOver = false
  var isFull = false
  //  val occupiedCells: mutable.Set[Coordinate] = mutable.Set()


  def init(): Unit = {
    snake.init()
    snake.body.foreach { trunk =>
      cells(trunk.x)(trunk.y).cellType = trunk.cellType
    }

    updateFreeCellTable()
  }

  def setCellType(x: Int, y: Int, cellType: GridType): Unit = {
    cells(x)(y).cellType  = cellType
  }
  def setCellType(cell: Cell, cellType: GridType): Unit = {
    cell.cellType  = cellType
  }

  def updateFreeCellTable(): Unit = {
    var index = 0

    cells.foreach { cell =>
      cell.foreach { c =>

        if (c.cellType == Empty()) {
          freeCellTable(index) = c
          index +=  1
        }
      }
    }
    nrFreeSpots = index
  }

  def turnSnake(dir: Direction): Unit = {
    def getOppositeDir: Direction =
      snake.body.head.cellType.asInstanceOf[SnakeHead].direction.opposite

    if (!isTurned && getOppositeDir != dir)  {
      setCellType(snake.body.head, SnakeHead(dir))
      isTurned = true
    }
  }

  def moveSnake(): Unit = {
    if (isGameOver) return

    snake.moveWithTail(nrRows, nrColumns)

    // change to match later
    // check the apple and collid before gride is overrided by drawSnake()
    val eatAppleFlag = encounterApple(snake.body.head)
    // collide itself
    val collideFlag = collideSnake(snake.body.head)

    drawSnake()

    //  ready to grow in next frame
    if (eatAppleFlag) {
//      println("** [LOG]: Encounter apple")
      hasApple = false
      growCounter += 3 // magic number !!!
      placeApple()

//      if (isFull) isGameOver = true // this should later use the return value of the placeApple();

    } else if (collideFlag) {
//      println("** [LOG]: Collide\n")
      isGameOver = true
    }
    isTurned = false
  }

  def drawSnake(): Unit = {

    snake.body.foreach { trunk =>
      cells(trunk.x)(trunk.y).cellType = trunk.cellType
    }
    if (growCounter > 0) {
      growCounter -= 1
    } else {

      // Cut tail
      val tail = snake.body.last
      val head = snake.body.head
      if (snake.body.head.isInSamePosition(tail)) {
        // In this scenario, the grid head position will be wrongly overwrite as snakeBody
        // because the tail's coor is the same as the coor of the head. I therefore need to rewrite it.
        cells(head.x)(head.y).cellType = head.cellType
      } else {
        cells(tail.x)(tail.y).cellType = Empty()

      }

      snake.cutTail()
    }

  }

  def encounterApple(head: Coordinate): Boolean =
    cells(head.x)(head.y).cellType == Apple()

  def collideSnake(head: Coordinate): Boolean = {
  // because the last trunk will be cut after this, the real death is head crash into the last trunk in this situation

    if (cells(head.x)(head.y).cellType == SnakeBody()) {
      if (head.isInSamePosition(snake.body.last)) false
      else true
    } else false
  }

  def placeApple(): Unit = {
    if (!hasApple && !isFull) {
      updateFreeCellTable()

      if (nrFreeSpots == 0) {
        // Deal with none empty space
        isFull = true
      } else {
        val index: Int = randomGen.randomInt(nrFreeSpots)
        val cell = freeCellTable(index)
        setCellType(cell, Apple())
        hasApple = true
      }
    } else if (!hasApple && isFull && growCounter > 0) {
      // previous frame does not have the apple and
      // the grid is full of snake when the snake still need to grow,then snake must collide itself.
      isGameOver = true
    }
  }
}
/* --------------------------------------------------------------------- */
