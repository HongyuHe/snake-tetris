package snake.game

import snake.game.SnakeLogic._

import scala.collection.mutable
import snake.components.{Direction, GridType, SnakeID}
import engine.random.{RandomGenerator, ScalaRandomGen}

class SnakeLogic(val randomGen: RandomGenerator,
                 val setting: GameSetting,
                 val nrColumns: Int,
                 val nrRows: Int) {

//  def this() = this(new ScalaRandomGen(), setting,  DefaultColumns, DefaultRows)
  def getloser:    SnakeID = gameController.loser
  def getGameResult: GameResult = gameController.getResult
  def isGameOver:   Boolean = gameController.status.isGameOver
  def terminateGame(): Unit = gameController.status.isGameOver = true
  def changeDir(d: Direction): Unit = gameController.turnSnake(gameController.hostSnake, d)
  def changeRivalDir(d: Direction): Unit = gameController.turnSnake(gameController.rivalSnake, d)
  def getGridTypeAt(x: Int, y: Int): GridType = gameController.grid.getCellType(y, x)

  def step(): Unit = {
//    println("Grid Hashcode: " + gameController.grid.hashCode())
    if (isReversing  && gameHistory.nonEmpty)  reverseGameHistory()
    if (!isReversing && !gameController.status.isGameOver) { gameController.update(); saveGameHistory() }
  }

  def setReverseTime(reverse: Boolean): Unit = {
    if (!reverse && isReversing)          saveGameHistory()
    if (reverse  && gameHistory.nonEmpty) reverseGameHistory()
    isReversing = reverse
  }

  private[this] var isReversing = false
  private[this] val gameHistory = mutable.Stack[GameController]()
  private[this] var gameController = GameController(nrRows, nrColumns, randomGen, setting)

  private[this] def saveGameHistory()():    Unit = gameHistory.push(gameController.makeDeepCopy)
  private[this] def reverseGameHistory():   Unit = gameController = gameHistory.pop()

  saveGameHistory() // save game history before game starts
}

object SnakeLogic {
// 1000 x 1000
  val DefaultColumns = 30
  val DefaultRows = 30

  def apply(setting: GameSetting): SnakeLogic =
    new SnakeLogic(
      new ScalaRandomGen(), setting,  DefaultColumns, DefaultRows)

}