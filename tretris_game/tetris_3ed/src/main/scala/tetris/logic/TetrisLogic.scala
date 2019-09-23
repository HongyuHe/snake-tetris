package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.game._
import tetris.logic.TetrisLogic._

class TetrisLogic(val randomGen: RandomGenerator,
                  val nrColumns: Int,
                  val nrRows: Int,
                  val initialBoard: Seq[Seq[TetrisBlock]]) {

  private val gameController = GameController(randomGen, nrColumns, nrRows, initialBoard)

  def this(random: RandomGenerator, nrColumns: Int, nrRows: Int) =
    this(random, nrColumns, nrRows, makeEmptyBoard(nrColumns, nrRows))

  def this() =
    this(new ScalaRandomGen(), DefaultWidth, DefaultHeight, makeEmptyBoard(DefaultWidth, DefaultHeight))

  def rotateLeft():   Unit = gameController letBlock RotateLeft
  def rotateRight():  Unit = gameController letBlock RotateRight
  def moveLeft():     Unit = gameController letBlock MoveLeft
  def moveRight():    Unit = gameController letBlock MoveRight
  def moveDown():     Unit = gameController letBlock MoveDown
  def doHardDrop():   Unit = gameController letBlock HardDrop
  def isGameOver:  Boolean = gameController isGameOver

  def getBlockAt(x: Int, y: Int): TetrisBlock = gameController.board(y)(x)

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