package snake.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.game.{Direction, Empty, GridType}
import snake.logic.SnakeLogic._

/** To implement Snake, complete the ``TODOs`` below.
  *
  * If you need additional files,
  * please also put them in the ``snake`` package.
  */
class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {

  def this() = this(new ScalaRandomGen(), DefaultRows, DefaultColumns)

  // TODO implement me
  def isGameOver: Boolean = false

  // TODO implement me
  def step(): Unit = ()

  // TODO implement me
  def setReverseTime(reverse: Boolean): Unit = ()

  // TODO implement me
  def changeDir(d: Direction): Unit = ()

  // TODO implement me
  def getGridTypeAt(x: Int, y: Int): GridType = Empty()

}

/** SnakeLogic companion object */
object SnakeLogic {

  val DefaultColumns = 25
  val DefaultRows = 25

}


