package snake.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.game.{Direction, Empty, GridType, SnakeBody, SnakeHead}
import snake.logic.SnakeLogic._

import scala.collection.mutable.ArrayBuffer
import snake.game._

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake.logic`` package.
 */
class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {
  /* --------------------------------------------------------------------- */
  private val _grid = Grid(nrRows, nrColumns, randomGen)

  /* --------------------------------------------------------------------- */


  def this() = this(new ScalaRandomGen(), DefaultRows, DefaultColumns)


  // TODO implement me
  def isGameOver: Boolean = _grid.isGameOver

  // This will be executed by SnakeGame for each update
  def step(): Unit = {
    _grid.placeApple()
    _grid.moveSnake()
    //    Util.printGrid(_grid)
  }

  // This method will be executed by `SnakeGame` when the `VK_R` key is pressed.
  def setReverseTime(reverse: Boolean): Unit = ()

  // This method will be executed by `SnakeGame` when direction key is pressed.
  def changeDir(d: Direction): Unit = {
    _grid.turnSnake(d)
  }

  // The `SnakeGame` draw graphics based on this method.
  // Update the type of each cell in the x by y matrix.
  def getGridTypeAt(x: Int, y: Int): GridType = {
    _grid.cells(y)(x).cellType
  }


  // Sum up: the only thing need to do is to
  // correctly update the `nrColumns` by `nrRows` (imaginary) matrix.
  _grid.init()  // Init game before the `step()` was first called
  _grid.placeApple()

}

/** SnakeLogic companion object */
object SnakeLogic {

  val DefaultColumns = 25
  val DefaultRows = 25

}


