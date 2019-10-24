package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.game._
import tetris.logic.TetrisLogic._

class TetrisLogic(val randomGen: RandomGenerator,
                  val nrColumns: Int,
                  val nrRows: Int,
                  val initialBoard: Seq[Seq[TetrisBlock]]) {

  def rotateRight():  Unit = controller invokeTetrominoAction RotateRight
  def rotateLeft():   Unit = controller invokeTetrominoAction RotateLeft
  def moveRight():    Unit = controller invokeTetrominoAction MoveRight
  def moveLeft():     Unit = controller invokeTetrominoAction MoveLeft
  def moveDown():     Unit = controller invokeTetrominoAction MoveDown
  def doHardDrop():   Unit = controller invokeTetrominoAction HardDrop
  def hold():         Unit = controller invokeTetrominoAction Hold

  def isGameOver:  Boolean = controller isGameOver
  def getBlockAt(x: Int, y: Int): TetrisBlock = controller.board(y)(x)
  def getNextTetrominos: Vector[TetrisBlock] = controller.getNextTetroQueue
  def getHoldedTetro: TetrisBlock = controller.getHoldedTetromino

  def this() = this(new ScalaRandomGen(), DefaultWidth, DefaultHeight, makeEmptyBoard(DefaultWidth, DefaultHeight))
  def this(random: RandomGenerator, nrColumns: Int, nrRows: Int) = this(random, nrColumns, nrRows, makeEmptyBoard(nrColumns, nrRows))

  private[this] val controller = GameController(randomGen, nrColumns, nrRows, initialBoard)
}

object TetrisLogic {
  val DefaultWidth: Int = 14
  val DefaultHeight: Int = 20

  def makeEmptyBoard(nrColumns: Int, nrRows: Int): Seq[Seq[TetrisBlock]] = Seq.fill(nrRows, nrColumns)(Empty)
  def apply() = new TetrisLogic(new ScalaRandomGen(), DefaultWidth, DefaultHeight, makeEmptyBoard(DefaultWidth, DefaultHeight))
}