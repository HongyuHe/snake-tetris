package snake.logic

import snake.logic.SnakeLogic._
import snake.game.{Direction, GridType}
import engine.random.{RandomGenerator, ScalaRandomGen}

class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {

  def this() = this(new ScalaRandomGen(), DefaultColumns, DefaultRows)

  def step(): Unit = gameController.updateState()
  def setReverseTime(reverse: Boolean): Unit = ()
  def isGameOver: Boolean = gameController.status.isGameOver
  def changeDir(d: Direction): Unit = gameController.turnSnake(d)
  def getGridTypeAt(x: Int, y: Int): GridType = gameController.grid.getCellType(y, x)

  private[this] val gameController = GameController(nrRows, nrColumns, randomGen)
}

object SnakeLogic {
  val DefaultColumns = 25
  val DefaultRows = 25
}