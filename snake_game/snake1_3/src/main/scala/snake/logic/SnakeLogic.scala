package snake.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.game.{Direction, Empty, GridType}
import snake.logic.SnakeLogic._

import scala.collection.mutable
import scala.collection.mutable.Stack

class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {

  private var _gameDriver = GameDriver(nrRows, nrColumns, randomGen)
  private val _gameHistory = mutable.Stack[GameDriver]()
  _gameHistory.push(_gameDriver.deepCopy())

  //  var _gameBuffer = GameDriver(nrRows, nrColumns, randomGen)
  private var _isReversing = false
  private var _continue = false

//    def this() = this(new ScalaRandomGen(), 7, 2)
  def this() = this(new ScalaRandomGen(), DefaultColumns, DefaultRows)


  def isGameOver: Boolean = _gameDriver.status.isGameOver

  def changeDir(d: Direction): Unit = _gameDriver.turnSnake(d)

  def step(): Unit = {
    //    var gameBuffer = _gameDriver.deepCopy()
    //    _gameDriver.copyTo(gaimeBuffer)
    //    _gameDriver.copy(grid = )

    if (_isReversing && _gameHistory.nonEmpty)
      _gameDriver = _gameHistory.pop()
    else if (!_gameDriver.status.isGameOver)  {
      _gameDriver.run()
      _gameHistory.push(_gameDriver.deepCopy())
    }



    //    println(s"Org : ${_gameDriver.snake.body.toString}")
    //    println(s"Copy: ${_gameDriver.deepCopy().snake.body.toString}")
    //    println("Stack: ")
    //    _gameHistory.foreach { gD =>
    //      println(gD.grid.freeCellTable)
    //      println(gD.snake.body)
    //      println(gD.status)

    //      println(" ======================================= ")
    //    }
    //    println()
    //    println(_gameDriver.deepCopy().snake.body.toString)
  }

  def setReverseTime(reverse: Boolean): Unit = {
    if (reverse && _gameHistory.nonEmpty)
      _gameDriver = _gameHistory.pop()

    if (!reverse && _isReversing) _continue = true
    _isReversing = reverse
  }

  def getGridTypeAt(x: Int, y: Int): GridType = {
    //    var gameBuffer = GameDriver(nrRows, nrColumns, randomGen)

    //    if (_isReversing && _gameHistory.nonEmpty) {
    ////      println()
    ////      println(_gameHistory.top.grid.cells)
    //      _gameDriver = _gameHistory.pop()
    //      println("--------------Reversing--------------")
    ////      _gameBuffer.refresh()
    ////      _gameHistory.foreach { gD =>
    ////        println(gD.grid.freeCellTable)
    ////        println(gD.snake.body)
    ////        println(gD.status)
    ////        println(" ======================================= ")
    ////      }
    ////      println()
    ////      println(_gameBuffer.grid.cells)
    ////      _gameDriver = _gameBuffer
    ////      return _gameBuffer.grid.getCellType(y, x)
    //    }
    //    if (_continue) {
    //      _continue = false
    //      _gameBuffer.copyTo(_gameDriver)
    //    }
    _gameDriver.grid.getCellType(y, x)
  }
}

/** SnakeLogic companion object */
object SnakeLogic {

  val DefaultColumns = 25
  val DefaultRows = 25

}