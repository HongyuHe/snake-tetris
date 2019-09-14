package snake.logic

import snake.logic.SnakeLogic._
import scala.collection.mutable
import snake.game.{Direction, GridType}
import engine.random.{RandomGenerator, ScalaRandomGen}

class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {

  def this() = this(new ScalaRandomGen(), DefaultColumns, DefaultRows)

  def isGameOver: Boolean = gameController.status.isGameOver
  def changeDir(d: Direction): Unit = gameController.turnSnake(d)
  def getGridTypeAt(x: Int, y: Int): GridType = gameController.grid.getCellType(y, x)

  def step(): Unit = {
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
  private[this] var gameController = GameController(nrRows, nrColumns, randomGen)

  private[this] def saveGameHistory()():    Unit = gameHistory.push(gameController.makeDeepCopy)
  private[this] def reverseGameHistory():   Unit = gameController = gameHistory.pop()

  saveGameHistory() // save game history before game starts
}

object SnakeLogic {

  val DefaultColumns = 25
  val DefaultRows = 25

}