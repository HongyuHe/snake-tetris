package snake.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.game.{Direction, Empty, GridType}
import snake.logic.SnakeLogic._

class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {

  private var _gameDriver = GameDriver(nrRows, nrColumns, randomGen)

  def this() = this(new ScalaRandomGen(), DefaultColumns, DefaultRows)

  def isGameOver: Boolean = _gameDriver.status.isGameOver

  def step(): Unit = if (!_gameDriver.status.isGameOver) _gameDriver.run()

  def setReverseTime(reverse: Boolean): Unit = ()

  def changeDir(d: Direction): Unit = _gameDriver.turnSnake(d)

  def getGridTypeAt(x: Int, y: Int): GridType = _gameDriver.grid.getCellType(y, x)

}

object SnakeLogic {

  val DefaultColumns = 25
  val DefaultRows = 25

}