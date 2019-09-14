package snake.game

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.components._

class GameController(val nrRows: Int,
                     val nrColumns: Int,
                     val randomGen: RandomGenerator) {

  var grid = Grid(nrRows, nrColumns)
  var status = GameStatus()
  var setting = GameSetting()

  val snake = Snake()
  val snakeRival = Snake(rivalMode = true)

  def init(): Unit = {

    def setLevel(): Unit = {
      for (i <- 0 until setting.level) {
        placeWall("vertical")
        placeWall("horizontal")
      }
    }

    if (setting.twoPlayerMode) drawSnake(snakeRival)
    drawSnake()
    placeApple()
    setLevel()
  }
  private[this] def placeWall(wType: String, nrBricks: Int = setting.level*2): Unit = {
    grid.updateTableOfFreeCells()
    val index: Int = randomGen.randomInt(grid.nrFreeSpots)
    val offset = wType match {
      case "vertical"   => nrColumns
      case "horizontal" => 1
    }

    for (i <- 0 until nrBricks*offset by offset) {
      grid.setCellType(
        grid.getFreeCell((index + i) % grid.nrFreeSpots),
        Wall()
      )
    }
  }

  def isSnakeHitWall: Boolean = grid.getCellType(snake.body.head) == Wall() || grid.getCellType(snakeRival.body.head) == Wall()

  //////////////////////////////////////////////////////////////////////
  def update(): Unit = {
    placeApple()
    moveSnake()
    moveSnake(snakeRival)
    updateGameStatus()
    updateGameStatus(snakeRival)
    checkGameOver()
    drawSnake()
    if (setting.twoPlayerMode) drawSnake(snakeRival)
    checkAppleAndGrowSnake()
  }

  def turnSnake(snake: Snake = snake, currentDirection: Direction): Unit = {
    if (snake.preDir.opposite != currentDirection)
      snake.updateHeadAndBodyTypes(headDir = currentDirection)
  }

  def moveSnake(snake: Snake = snake): Unit = {
    val snakeHeadDir = getSnakeHeadDirection(snake)
    val moveSnakeTo: Direction => Unit = snakeHeadDir match {
      case East()  | West() => snake.move(nrColumns)
      case North() | South() => snake.move(nrRows)
    }
    moveSnakeTo(snakeHeadDir)
  }

  def drawSnake(snake: Snake = snake): Unit = {
    val isSnakeHeadBehindsTail = grid.getCellType(snake.body.head) == SnakeBody(snake.id, 1)
    def eraseSnakeTail(): Unit = if (!isSnakeHeadBehindsTail) grid.setCellType(snake.droppedTail.get, Empty())

    snake.body.foreach(trunk => grid.setCellType(trunk, trunk.cellType))
    if (snake.droppedTail.isDefined) {
      eraseSnakeTail()
    }
    snake.preDir = getSnakeHeadDirection(snake)
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

  def updateGameStatus(snake: Snake = snake): Unit = {
    status.isSnakeGrowing = snake.growCounter > 0
    val isAppleEaten = grid.getCellType(snake.body.head) == Apple()
    snake.id match {
      case "rival"  => status.appleEatenByRivalSnake  = isAppleEaten
      case "normal" => status.appleEatenByNormalSnake = isAppleEaten
    }

    status.isSnakeCrashed = grid.getCellType(snake.body.head) match {
      case SnakeBody(_,1) => status.isSnakeGrowing
      case SnakeBody(_, _) => true
      case _ => false
    }
  }

  def checkAppleAndGrowSnake(): Unit = {
    if (status.appleEatenByRivalSnake || status.appleEatenByNormalSnake) {
      status.hasApple = false
      if (status.appleEatenByNormalSnake) {
        snake.grow()
        status.appleEatenByNormalSnake = false
      }
      else if (status.appleEatenByRivalSnake) {
        snakeRival.grow()
        status.appleEatenByRivalSnake = false
      }
      placeApple()
    }
  }

  def makeDeepCopy: GameController = {
    val that = GameController(nrRows, nrColumns, randomGen)

    that.status = this.status.copy()
    this.grid copyTo that.grid
    this.snake copyTo that.snake
    this.snakeRival copyTo that.snakeRival
    that
  }

  private[this] def getSnakeHeadDirection(snake: Snake = snake): Direction = snake.body.head.cellType.asInstanceOf[SnakeHead].direction

  private[this] def checkGameOver(): Unit =
    if ((!status.hasApple && status.isGridFull && status.isSnakeGrowing) || status.isSnakeCrashed || isSnakeHitWall)
      status.isGameOver = true
}

object GameController {
  def apply(nrRows: Int, nrColumns: Int, randomGen: RandomGenerator): GameController =
    new GameController(nrRows, nrColumns, randomGen) { init() }
}
