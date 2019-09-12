package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.game._
import tetris.logic.TetrisLogic._

/** To implement Tetris, complete the ``TODOs`` below.
  *
  * If you need additional files,
  * please also put them in the ``tetris.logic`` package.
  */
class TetrisLogic(val randomGen: RandomGenerator,
                  val nrColumns: Int,
                  val nrRows: Int,
                  val initialBoard: Seq[Seq[TetrisBlock]]) {

  def this(random: RandomGenerator, nrColumns: Int, nrRows: Int) =
    this(random, nrColumns, nrRows, makeEmptyBoard(nrColumns, nrRows))

  def this() =
    this(new ScalaRandomGen(), DefaultWidth, DefaultHeight, makeEmptyBoard(DefaultWidth, DefaultHeight))

  // TODO implement me
  def rotateLeft(): Unit = ()

  // TODO implement me
  def rotateRight(): Unit = ()

  // TODO implement me
  def moveLeft(): Unit = ()

  // TODO implement me
  def moveRight(): Unit = ()

  // TODO implement me
  def moveDown(): Unit = ()

  // TODO implement me
  def doHardDrop(): Unit = ()

  // TODO implement me
  def isGameOver: Boolean = false

  // TODO implement me
  def getBlockAt(x: Int, y: Int): TetrisBlock = Empty
}

object TetrisLogic {

  def makeEmptyBoard(nrColumns: Int, nrRows: Int): Seq[Seq[TetrisBlock]] = {
    val emptyLine = Seq.fill(nrColumns)(Empty)
    Seq.fill(nrRows)(emptyLine)
  }

  val DefaultWidth: Int = 10
  val DefaultHeight: Int = 20


  def apply() = new TetrisLogic(new ScalaRandomGen(),
                                DefaultWidth,
                                DefaultHeight,
                                makeEmptyBoard(DefaultWidth, DefaultHeight))

}