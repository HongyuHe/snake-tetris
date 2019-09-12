package snake.logic

import engine.random.RandomGenerator
import snake.game.{Apple, Direction, East, Empty, North, SnakeBody, SnakeHead, South, West}

case class GameDriver(
                       nrRows: Int,
                       nrColumns: Int,
                       randomGen: RandomGenerator) {

  var snake = Snake()
  var status = GameStatus()
  val grid = Grid(nrRows, nrColumns)

  def run(): Unit = {
    placeApple()

    moveSnake()

    updateGameStatus()

    drawSnake(snake.droppedTail)

    checkGameStatus()
  }

  def turnSnake(dir: Direction): Unit = {
    val getOppositeDir: Direction =
      snake.body.head.cellType.asInstanceOf[SnakeHead].direction.opposite

    if (!status.hasSnakeTurned && getOppositeDir != dir) {
      snake.updateBodyTypes(headDir = dir)
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

  def drawSnake(droppedTail: Option[SnakeTrunk]): Unit = {
    val snakeHeadIsBehindsTail: Boolean =
      grid.getCellType(snake.body.head) == SnakeBody(1)

    def eraseSnakeTail(): Unit =
      if (!snakeHeadIsBehindsTail)
        grid.setCellType(droppedTail.get, Empty())

    snake.body.foreach { trunk =>
      grid.setCellType(trunk, trunk.cellType)
    }
    if (droppedTail.isDefined) eraseSnakeTail()
  }

  def placeApple(): Unit = {
    if (!status.hasApple && !status.isGridFull) {
      grid.updateFreeCellTable()
      if (grid.nrFreeSpots == 0) status.isGridFull = true
      else {
        val index: Int = randomGen.randomInt(grid.nrFreeSpots)
        val cell = grid.freeCellTable(index)
        grid.setCellType(cell, Apple())
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

}

object GameDriver {
  def apply(nrRows: Int, nrColumns: Int, randomGen: RandomGenerator): GameDriver =
    new GameDriver(nrRows, nrColumns, randomGen) {
      drawSnake(snake.droppedTail)
      placeApple()
    }
}
