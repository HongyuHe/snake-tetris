package snake.logic

import engine.random.RandomGenerator
import snake.game.{Apple, Direction, East, Empty, North, SnakeBody, SnakeHead, South, West}

import scala.collection.mutable
import scala.collection.immutable

class GameDriver(
                  val nrRows: Int,
                  val nrColumns: Int,
                  val randomGen: RandomGenerator) {

  var snake = Snake()
  var status = GameStatus()
  var grid = Grid(nrRows, nrColumns)

  var snakeDir: Direction = East()
//  var applePos = (0, 0)

//  def this(nrRows: Int,
//           nrColumns: Int,
//           randomGen: RandomGenerator,
//           snake: Snake,
//           status: GameStatus,
//           grid: Grid) {
//
//    this(nrRows, nrColumns, randomGen)
//    //    println(snake.body)
//    //    println("snake.copy(): ")
//    //    println(snake.copy().body)
//
//    this.snake = snake
//    this.grid = grid
//    this.status = status
//  }

  def run(): Unit = {
    placeApple()

    moveSnake()

    updateGameStatus()

    drawSnake(snake.droppedTail)

    checkGameStatus()
  }

  def refresh(): Unit = {
    def refreshGrid(): Unit = {
      //      for (row <- grid.cells.indices) {
      //        for (col <- grid.cells(row).indices) {
      //          if ((row, col) == applePos)
      //            grid.setCellType(row, col, Apple())
      //          drawSnake(refresh = true)
      //        }
      //      }
    }

    snake.updateBodyTypes(snakeDir)
    refreshGrid()
  }

  def deepCopy(): GameDriver = {
    val newGD = new GameDriver(nrRows, nrColumns, randomGen)
    this.snake.copyTo(newGD.snake)

    this.grid.copyTo(newGD.grid)
    newGD.status = this.status.copy()
    newGD
//    val new_status = status.copy()

//    val new_snake = Snake()
//    new_snake.body.clear()
//    snake.body.copyToBuffer(new_snake.body)
//    new_snake.growCounter = snake.growCounter
//    new_snake.droppedTail = snake.droppedTail

//    val new_grid = Grid(nrRows, nrColumns)
//    new_grid.nrFreeSpots = grid.nrFreeSpots
//    new_grid.cells.clear()
//    grid.cells.copyToBuffer(new_grid.cells)
    //    for (row <- grid.cells.indices) {
    //      new_grid.cells(row).clear()
    //      grid.cells(row).copyToBuffer(new_grid.cells(row).asInstanceOf[mutable.Buffer[Object]])
    //    }
//    for (index <- 0 to grid.nrFreeSpots) {
//      new_grid.freeCellTable(index) = grid.freeCellTable(index).copy()
//    }

//    GameDriver(nrRows, nrColumns, randomGen, snake, status, grid)
  }

  def turnSnake(dir: Direction): Unit = {
    val getOppositeDir: Direction =
      snake.body.head.cellType.asInstanceOf[SnakeHead].direction.opposite

    if (!status.hasSnakeTurned && getOppositeDir != dir) {
      snake.updateBodyTypes(headDir = dir)
      snakeDir = dir
      status.hasSnakeTurned = true
    }
  }

  def moveSnake(): Unit = {
    var boundary = 0
    val moveToDir: Int => Unit =
      snake
        .body
        .head
        .cellType
        .asInstanceOf[SnakeHead]
        .direction match {
        case East() => boundary = nrColumns; snake.moveEast
        case West() => boundary = nrColumns; snake.moveWest
        case North() => boundary = nrRows; snake.moveNorth
        case South() => boundary = nrRows; snake.moveSouth
      }

    snake.move(
      moveToDir,
      boundary,
      snake
        .body
        .head
        .cellType
        .asInstanceOf[SnakeHead]
        .direction
    )
  }

  def drawSnake(droppedTail: Option[SnakeTrunk] = None, refresh: Boolean = false): Unit = {
    val snakeHeadIsBehindsTail: Boolean =
      grid.getCellType(snake.body.head) == SnakeBody(1)

    def eraseSnakeTail(): Unit =
      if (!snakeHeadIsBehindsTail)
        grid.setCellType(droppedTail.get, Empty())

    snake.body.foreach { trunk =>
      grid.setCellType(trunk, trunk.cellType)
    }
    if (!refresh && droppedTail.isDefined) eraseSnakeTail()
  }

  def placeApple(): Unit = {
    if (!status.hasApple && !status.isGridFull) {
      grid.updateFreeCellTable()
      if (grid.nrFreeSpots == 0) status.isGridFull = true
      else {
        val index: Int = randomGen.randomInt(grid.nrFreeSpots)
        val cell = grid.freeCellTable(index)
        grid.setCellType(cell, Apple())
//        applePos = grid.findApple()
        status.hasApple = true
      }
    } else if (!status.hasApple && status.isGridFull && status.isSnakeGrowing) {
      status.isGameOver = true
    }
  }

  def updateGameStatus(): Unit = {
    status.hasSnakeTurned = false
    status.isSnakeGrowing = snake.growCounter > 0
    status.isAppleAte = grid.getCellType(snake.body.head) == Apple()
    status.isSnakeCrashed =
      grid.getCellType(snake.body.head) match {
        case SnakeBody(1) => status.isSnakeGrowing
        case SnakeBody(_) => true
        case _ => false
      }
  }

  def checkGameStatus(): Unit = {
    if (status.isAppleAte) {
      status.hasApple = false
      snake.grow() // Magic number!!!
      placeApple()
    } else if (status.isSnakeCrashed) {
      status.isGameOver = true
    }
  }

  def copyTo(that: GameDriver): Unit = {

    //    var buffer = mutable.Buffer[SnakeTrunk]()
    //    var imL = immutable.List()
    //    var mL  = mutable.ListBuffer()

    that.snake.body.clear()
    this.snake.body.copyToBuffer(that.snake.body)
    //    that.snake.body = that.snake.body.last.asInstanceOf[mutable.Buffer[SnakeTrunk]]
    //    for (i <- buffer.asInstanceOf[mutable.Buffer[SnakeTrunk]].indices) {
    //      this.snake.body(i) = buffer.head.asInstanceOf[SnakeTrunk](i)
    //    }
    that.snake.growCounter = this.snake.growCounter
    that.snake.droppedTail = this.snake.droppedTail

    that.grid = this.grid.copy()
    that.grid.cells.clear()
    this.grid.cells.copyToBuffer(that.grid.cells)

    that.status = this.status.copy()
  }
}

object GameDriver {

  def apply(nrRows: Int, nrColumns: Int, randomGen: RandomGenerator): GameDriver =
    new GameDriver(nrRows, nrColumns, randomGen) {
      drawSnake()
      placeApple()
    }

//  def apply(nrRows: Int,
//            nrColumns: Int,
//            randomGen: RandomGenerator,
//            snake: Snake,
//            status: GameStatus,
//            grid: Grid): GameDriver = {
//
//    val newGD = new GameDriver(nrRows, nrColumns, randomGen)
//
////    newGD.snake.body.clear()
//    newGD.snake = Snake(snake)
//
//    grid.cells(0)
//    val cells: mutable.Buffer[mutable.Buffer[Cell]] = grid.cells.transpose.transpose
//    newGD.grid.cells = cells
//    newGD.status = status.copy()
//
//    newGD
//  }
}
