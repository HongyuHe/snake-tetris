package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.game._
import tetris.logic.TetrisLogic._

class TetrisLogic(val randomGen: RandomGenerator,
                  val nrColumns: Int,
                  val nrRows: Int,
                  val initialBoard: Seq[Seq[TetrisBlock]]) {

  def rotateLeft():   Unit = controller letBlock RotateLeft
  def rotateRight():  Unit = controller letBlock RotateRight
  def moveLeft():     Unit = controller letBlock MoveLeft
  def moveRight():    Unit = controller letBlock MoveRight
  def moveDown():     Unit = controller letBlock MoveDown
  def doHardDrop():   Unit = controller letBlock HardDrop

  def isGameOver:  Boolean = controller isGameOver
  def getBlockAt(x: Int, y: Int): TetrisBlock = controller.board(y)(x)

  def this() = this(new ScalaRandomGen(), DefaultWidth, DefaultHeight, makeEmptyBoard(DefaultWidth, DefaultHeight))
  def this(random: RandomGenerator, nrColumns: Int, nrRows: Int) = this(random, nrColumns, nrRows, makeEmptyBoard(nrColumns, nrRows))

  private[this] val controller = GameController(randomGen, nrColumns, nrRows, initialBoard)
}

object TetrisLogic {

  val DefaultWidth: Int = 10
  val DefaultHeight: Int = 20

  def makeEmptyBoard(nrColumns: Int, nrRows: Int): Seq[Seq[TetrisBlock]] = Seq.fill(nrRows, nrColumns)(Empty)

  def apply() = new TetrisLogic(new ScalaRandomGen(), DefaultWidth, DefaultHeight, makeEmptyBoard(DefaultWidth, DefaultHeight))
}