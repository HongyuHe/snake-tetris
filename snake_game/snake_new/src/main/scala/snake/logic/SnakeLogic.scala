package snake.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.game.{Direction, East, Empty, GridType, SnakeHead, South}
import snake.logic.SnakeLogic._

import scala.collection.mutable

class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {

  private var _isReversing = false
  private var _continue = false

  private var gameDriver_ = GameDriver(nrRows, nrColumns, randomGen)
  private val gameHistory_ = mutable.Stack[GameDriver]()

  gameHistory_.push(gameDriver_.deepCopy())

//    def this() = this(new ScalaRandomGen(), 7, 2)
  def this() = this(new ScalaRandomGen(), DefaultColumns, DefaultRows)

  def isGameOver: Boolean = gameDriver_.status.isGameOver

  def changeDir(d: Direction): Unit = gameDriver_.turnSnake(d)


  def step(): Unit = {

    if (_isReversing && gameHistory_.nonEmpty)
      gameDriver_ = gameHistory_.pop()
    else if (!_isReversing && !gameDriver_.status.isGameOver)  {
      gameDriver_.run()
      gameHistory_.push(gameDriver_.deepCopy())
    }
  }

  def setReverseTime(reverse: Boolean): Unit = {
    if (reverse && gameHistory_.nonEmpty)
      gameDriver_ = gameHistory_.pop()

    if (!reverse && _isReversing)
      gameHistory_.push(gameDriver_.deepCopy())
    _isReversing = reverse
  }

  def getGridTypeAt(x: Int, y: Int): GridType = {
//    if (!gameDriver_.status.isGameOver && !_isReversing)
//      gameHistory_.push(gameDriver_.deepCopy())

    gameDriver_.grid.getCellType(y, x)
  }
}

/** SnakeLogic companion object */
object SnakeLogic {

  val DefaultColumns = 25
  val DefaultRows = 25

}