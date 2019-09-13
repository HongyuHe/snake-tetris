package snake.logic

import engine.random.RandomGenerator
import snake.game.{Apple, Direction, East, Empty, North, SnakeBody, SnakeHead, South, West}

class GameController (val nrRows: Int,
                      val nrColumns: Int,
                      val randomGen: RandomGenerator) {

  val snake  = Snake()
  var status = GameStatus()
  var grid   = Grid(nrRows, nrColumns)

  private[this] var snakePreviousDirection: Direction = East()

  def updateState(): Unit = {
    placeApple()
    moveSnake()
    updateGameStatus()
    checkGameOver()
    drawSnake(snake.droppedTail)
    checkAppleAndGrowSnake()
  }

  def turnSnake(currentDirection: Direction): Unit =
    if (snakePreviousDirection.opposite != currentDirection)
      snake.updateHeadAndBodyTypes(headDir = currentDirection)

  def moveSnake(): Unit = {
    val snakeHeadDir = getSnakeHeadDirection
    val moveSnakeTo: Direction => Unit = snakeHeadDir match {
      case East()  | West()  => snake.move(nrColumns)
      case North() | South() => snake.move(nrRows)
    }
    moveSnakeTo (snakeHeadDir)
  }

  def drawSnake(snakeDroppedTail: Option[SnakeTrunk] = None): Unit = {
    val isSnakeHeadBehindsTail = grid.getCellType(snake.body.head) == SnakeBody(1)
    def eraseSnakeTail(): Unit = if (!isSnakeHeadBehindsTail) { grid.setCellType(snakeDroppedTail.get, Empty()) }

    snake.body.foreach ( trunk => grid.setCellType(trunk, trunk.cellType) )
    if (snakeDroppedTail.isDefined) { eraseSnakeTail() }
    snakePreviousDirection = getSnakeHeadDirection
  }

  def placeApple(): Unit = {
    val canPlaceApple = !status.hasApple && !status.isGridFull
    def generateNewApple(): Unit = {
      grid.updateTableOfFreeCells()
      if (grid.nrFreeSpots == 0) status.isGridFull = true
      else {
        val index: Int = randomGen.randomInt(grid.nrFreeSpots)
        val cell = grid.getFreeCell(index)
        grid.setCellType(cell, Apple())
        status.hasApple = true
      }
    }
    if (canPlaceApple) generateNewApple()
  }

  def updateGameStatus(): Unit = {
    status.isSnakeGrowing = snake.growCounter > 0
    status.isAppleEaten   = grid.getCellType(snake.body.head) == Apple()
    status.isSnakeCrashed = grid.getCellType(snake.body.head) match {
        case SnakeBody(1) => status.isSnakeGrowing
        case SnakeBody(_) => true
        case _            => false
      }
  }

  def checkAppleAndGrowSnake(): Unit = {
    if (status.isAppleEaten) {
      status.hasApple = false
      snake.grow()
      placeApple()
    }
  }

  private[this] def getSnakeHeadDirection: Direction = snake.body.head.cellType.asInstanceOf[SnakeHead].direction
  private[this] def checkGameOver(): Unit =
    if ((!status.hasApple && status.isGridFull && status.isSnakeGrowing) || status.isSnakeCrashed)
      status.isGameOver = true
}

object GameController {
  def apply(nrRows: Int, nrColumns: Int, randomGen: RandomGenerator): GameController =
    new GameController(nrRows, nrColumns, randomGen) {
      drawSnake()
      placeApple()
    }
}
