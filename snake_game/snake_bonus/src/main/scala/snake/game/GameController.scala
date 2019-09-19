package snake.game

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.components._

class GameController(val nrRows: Int,
                     val nrColumns: Int,
                     val randomGen: RandomGenerator,
                     var setting: GameSetting) {

  var grid = Grid(nrRows, nrColumns)
  var status = GameStatus()

  val snake = Snake()
  val snakeRival = Snake(rivalMode = true)

  def init(): Unit = {

    def setLevel(): Unit = {
      for (_ <- Range(0, setting.gameLevel)) {
        buildWall("vertical")
        buildWall("horizontal")
      }
    }

    if (setting.twoPlayerMode) drawSnake(snakeRival)
    drawSnake()
//    placeApple()
    updateItems()
    setLevel()
    updateGameStatus()
  }

  def updateItems(): Unit = {
    placeItems(Bomb(), setting.bombNumber)
    placeItems(Apple(), setting.appleNumber)
  }

  def placeItems(item: GridType, number: Int): Unit = {
    val needNewItems = item match {
      case Bomb()  => !status.hasEnoughBombs  && !status.isGridFull
      case Apple() => !status.hasEnoughApples && !status.isGridFull
      case _       => false
    }
    def generateNewItems(): Unit = {
      grid.updateTableOfFreeCells()
      if (grid.nrFreeSpots < number) status.isGridFull = true
      else {
        for (_ <- Range(0, number-grid.getItemAmount(item))) {
          val cell = grid.getFreeCell(randomGen.randomInt(grid.nrFreeSpots))
          grid.setCellType(cell, item)
        }
        //        status.hasEnoughApples = true
      }
    }
    if (needNewItems) { generateNewItems() }
  }

  private[this] def buildWall(wType: String, nrBricks: Int = setting.gameLevel*2): Unit = {
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
//    placeApple()
    updateItems()
    moveSnake()
    if (setting.twoPlayerMode) moveSnake(snakeRival)
    updateGameStatus()
    if (setting.twoPlayerMode) updateGameStatus(snakeRival)
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
    val canPlaceApple = !status.hasEnoughApples && !status.isGridFull
    def generateNewApple(): Unit = {
      grid.updateTableOfFreeCells()
      if (grid.nrFreeSpots == 0) status.isGridFull = true
      else {
        val index: Int = randomGen.randomInt(grid.nrFreeSpots)
        val cell = grid.getFreeCell(index)
        grid.setCellType(cell, Apple())
        status.hasEnoughApples = true
      }
    }
    if (canPlaceApple) generateNewApple()
  }

  def updateGameStatus(snake: Snake = snake): Unit = {
    status.isSnakeGrowing = snake.growCounter > 0
    val isAppleEaten = grid.getCellType(snake.body.head) == Apple()
    snake.id match { // move to snake property later
      case "rival"  => status.appleEatenByRivalSnake  = isAppleEaten
      case "normal" => status.appleEatenByNormalSnake = isAppleEaten
    }
    status.isSnakeCrashed = grid.getCellType(snake.body.head) match {
      case SnakeBody(_, 1) => status.isSnakeGrowing
      case SnakeBody(_, _) => true
      case _ => false
    }
    status.hasEnoughBombs  = grid.getItemAmount(Bomb())  == setting.bombNumber
    status.hasEnoughApples = grid.getItemAmount(Apple()) == setting.appleNumber
  }

  def checkAppleAndGrowSnake(): Unit = {
    if (status.appleEatenByRivalSnake || status.appleEatenByNormalSnake) {
      status.hasEnoughApples = false
      if (status.appleEatenByNormalSnake) {
        snake.grow()
        status.appleEatenByNormalSnake = false
      }
      else if (status.appleEatenByRivalSnake) {
        snakeRival.grow()
        status.appleEatenByRivalSnake = false
      }
//      placeApple()
      updateItems()
    }
  }

  def makeDeepCopy: GameController = {
    val that = GameController(nrRows, nrColumns, randomGen, setting)

    that.status = this.status.copy()
    that.setting = this.setting.copy()
    this.grid copyTo that.grid
    this.snake copyTo that.snake
    this.snakeRival copyTo that.snakeRival
    that
  }

  private[this] def getSnakeHeadDirection(snake: Snake = snake): Direction = snake.body.head.cellType.asInstanceOf[SnakeHead].direction

  private[this] def checkGameOver(): Unit =
    if ((!status.hasEnoughApples && status.isGridFull && status.isSnakeGrowing) || status.isSnakeCrashed || isSnakeHitWall)
      status.isGameOver = true
}

object GameController {
  def apply(nrRows: Int, nrColumns: Int, randomGen: RandomGenerator, setting: GameSetting): GameController =
    new GameController(nrRows, nrColumns, randomGen, setting) { init() }
}
