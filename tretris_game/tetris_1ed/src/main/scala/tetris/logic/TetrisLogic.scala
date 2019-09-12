package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.game._
import tetris.logic.TetrisLogic._

class TetrisLogic(val randomGen: RandomGenerator,
                  val nrColumns: Int,
                  val nrRows: Int,
                  val initialBoard: Seq[Seq[TetrisBlock]]) {

  private val gameDiver = GameDriver(randomGen, nrColumns, nrRows, initialBoard)

  def this(random: RandomGenerator, nrColumns: Int, nrRows: Int) =
    this(random, nrColumns, nrRows, makeEmptyBoard(nrColumns, nrRows))

  def this() =
    this(new ScalaRandomGen(), DefaultWidth, DefaultHeight, makeEmptyBoard(DefaultWidth, DefaultHeight))

//  println("Same board or not? " + (gameDiver.board == initialBoard))
//  println(initialBoard)
//  def this() =
//    this(new ScalaRandomGen(), DefaultWidth, DefaultHeight, makeEmptyBoard(5, 5))

  def rotateLeft():  Unit = gameDiver.rotateBlock("left")

  def rotateRight(): Unit = gameDiver.rotateBlock("right")

  def moveLeft(): Unit = gameDiver.moveBlockLeft()
  def moveRight(): Unit = gameDiver.moveBlockRight()

  def moveDown(): Unit = gameDiver.moveBlockDown()

  def doHardDrop(): Unit = gameDiver.hardDropBlock()

  def isGameOver: Boolean = gameDiver.isGameOver

  def getBlockAt(x: Int, y: Int): TetrisBlock = {
//    gameDiver.drawBoard()
    gameDiver.board(y)(x)
  }
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
                                makeEmptyBoard(DefaultWidth, DefaultHeight)) {
  }

}