// scalastyle:off
package snake.game

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.components._


class GameController(val nrRows: Int,
                     val nrColumns: Int,
                     val randomGen: RandomGenerator,
                     var setting: GameSetting) {

  val grid = Grid(nrRows, nrColumns)
  var status = GameStatus()

  val hostSnake = Snake()
//  val hostSnake = SnakeAI()
  var rivalSnake = Snake(rivalMode = true)
  if (setting.battleWithAI) rivalSnake = SnakeAI()
//  var rivalSnake = SnakeAI()

  var looser: SnakeID = HostSnake()
//  var applePositionSet: Set[Cell]= Set()

  def printGridWithApple (): Unit = {
    for (row <- 0 until nrRows; col <- 0 until nrColumns) {
      if (col == nrColumns-1) print("\n")
      if (grid.cells(row)(col).cellType == Apple()) print(" A")
      else print(" _")
    }
    println("$" * 100)
  }

  def init(): Unit = {
    def setLevel(): Unit = {
      for (_ <- Range(0, setting.gameLevel)) {
        buildWall("vertical")
        buildWall("horizontal")
      }
    }

    drawSnakes()
    updateItems()
    setLevel()
    updateGameStatusFor(hostSnake)
  }

  def updateItems(): Unit = {
    placeItems(Bomb(), setting.bombNumber)
    placeItems(Apple(), setting.appleNumber)
  }
  def drawSnakes(): Unit = {
    drawSnake()
    if (setting.twoPlayerMode) drawSnake(rivalSnake)
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

  def isSnakeBlowUp : Boolean = hostSnake.body.isEmpty || rivalSnake.body.isEmpty

  def isSnakeHitWall: Boolean = grid.getCellType(hostSnake.body.head) == Wall() || grid.getCellType(rivalSnake.body.head) == Wall()
  //////////////////////////////////////////////////////////////////////
  def update(): Unit = {
//    printGridWithApple()
//    println("Grid Hashcode: " + grid.hashCode())

    updateItems()

    moveSnake()
    if (setting.twoPlayerMode) moveSnake(rivalSnake)

    updateGameStatusFor(hostSnake)
    if (setting.twoPlayerMode) updateGameStatusFor(rivalSnake)

    checkGameOver()
    drawSnakes()
    checkAppleAndGrowSnake()
    checkBombAndCutSnake()

    grid.updateApplePositions()
//    println(hostSnake.id + hostSnake.body.toString())
  }

  def turnSnake(snake: Snake = hostSnake, currentDirection: Direction): Unit = {
    if (snake.preDir.opposite != currentDirection)
      snake.updateBodyWithHeadDir(headDir = currentDirection)
  }

  def moveSnake(snake: Snake = hostSnake): Unit = {
    if (snake.id == AiSnake()) {
      grid.updateApplePositions()
      snake.asInstanceOf[SnakeAI].turnAI(grid.applePositionsSet)
    }

    val snakeHeadDir = getSnakeHeadDirection(snake)
//    println("Current direction: " + snakeHeadDir)
    val moveSnakeTo: Direction => Unit = snakeHeadDir match {
      case East()  | West() => snake.move(nrColumns)
      case North() | South() => snake.move(nrRows)
    }
    moveSnakeTo(snakeHeadDir)
  }

  def drawSnake(snake: Snake = hostSnake): Unit = {
    val isSnakeHeadBehindsTail = grid.getCellType(snake.body.head) == SnakeBody(snake.id, 1)
    def eraseSnakeTail(): Unit = if (!isSnakeHeadBehindsTail) grid.setCellType(snake.droppedTail.get, Empty())

    snake.body.foreach(trunk => grid.setCellType(trunk, trunk.cellType))
    if (snake.droppedTail.isDefined) {
      eraseSnakeTail()
    }
    snake.preDir = getSnakeHeadDirection(snake)

//    println(snake.body)
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

  def updateGameStatusFor(snake: Snake): Unit = {
    val typeOfCurrentPosition = grid.getCellType(snake.body.head)
    val isBombHit = typeOfCurrentPosition == Bomb()
    val isAppleEaten = typeOfCurrentPosition == Apple()
    lazy val isMyBody = typeOfCurrentPosition.asInstanceOf[SnakeBody].id == snake.id

    status.isSnakeGrowing = snake.growCounter > 0
    status.hasEnoughBombs  = grid.getItemAmount(Bomb())  == setting.bombNumber
    status.hasEnoughApples = grid.getItemAmount(Apple()) == setting.appleNumber

    snake.id match {
      case RivalSnake() | AiSnake() =>
        status.bombHitByRivalSnake = isBombHit
        status.appleEatenByRivalSnake  = isAppleEaten
      case HostSnake() =>
        status.bombHitByNormalSnake = isBombHit
        status.appleEatenByNormalSnake = isAppleEaten
      case _ =>
    }
    status.isSnakeCrashed = typeOfCurrentPosition match {
      case SnakeBody(_, 1) => status.isSnakeGrowing
      case SnakeBody(_, _) if isMyBody => true // cannot collide with anothe snake
      // BUG: green snake can collide with its self!
      case _ => false
    }
    if (status.isSnakeCrashed) { looser = snake.id }
  }

  def checkAppleAndGrowSnake(): Unit = {
    if (status.appleEatenByRivalSnake || status.appleEatenByNormalSnake) {
      status.hasEnoughApples = false

      if (status.appleEatenByNormalSnake) {
        hostSnake.grow(); status.appleEatenByNormalSnake = false
      }
      if (status.appleEatenByRivalSnake) {
        rivalSnake.grow(); status.appleEatenByRivalSnake = false
      }
      updateItems()
    }
  }

  def checkBombAndCutSnake(): Unit = {
    if (status.bombHitByRivalSnake || status.bombHitByNormalSnake) {
      status.hasEnoughBombs = false

      if (status.bombHitByNormalSnake) {
        hostSnake.cutTail();      status.bombHitByNormalSnake = false
      }
      if (status.bombHitByRivalSnake) {
        rivalSnake.cutTail();  status.bombHitByRivalSnake = false
      }
      checkGameOver()
      updateItems()
      if (!status.isGameOver) drawSnakes()
    }
  }

  def makeDeepCopy: GameController = {
    val that = GameController(nrRows, nrColumns, randomGen, setting)

    that.status = this.status.copy()
    that.setting = this.setting.copy()
    this.grid copyTo that.grid
    this.hostSnake copyTo that.hostSnake
    this.rivalSnake copyTo that.rivalSnake
    that
  }

  private[this] def getSnakeHeadDirection(snake: Snake = hostSnake): Direction = snake.body.head.cellType.asInstanceOf[SnakeHead].direction

  private[this] def checkGameOver(): Unit =
    if (isSnakeBlowUp || isSnakeHitWall || (!status.hasEnoughApples && status.isGridFull && status.isSnakeGrowing) || status.isSnakeCrashed) {
      status.isGameOver = true
      if (hostSnake.body.isEmpty || grid.getCellType(hostSnake.body.head) == Wall())
        looser = HostSnake()
      else looser = RivalSnake()
    }

}

object GameController {
  def apply(nrRows: Int, nrColumns: Int, randomGen: RandomGenerator, setting: GameSetting): GameController =
    new GameController(nrRows, nrColumns, randomGen, setting) { init() }
}
