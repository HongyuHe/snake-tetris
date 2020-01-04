// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package test.tetris

import engine.random.RandomGenerator
import test.{GameLogicInterface, GenericRecord, GridTypeInterface}
import tetris.game._
import tetris.logic.TetrisLogic

sealed abstract class TetrisAction
case object RotateLeft    extends TetrisAction
case object RotateRight   extends TetrisAction
case object Down          extends TetrisAction
case object Left          extends TetrisAction
case object Right         extends TetrisAction
case object Drop          extends TetrisAction

case class TetrisGridTypeWrapper(gridType : TetrisBlock)  extends GridTypeInterface[TetrisGridTypeWrapper]{
  override def conforms(rhs : TetrisGridTypeWrapper) : Boolean = gridType == rhs.gridType

  override def toChar: Char = gridType match {
    case IBlock  => 'I'
    case JBlock  => 'J'
    case LBlock  => 'L'
    case OBlock  => 'O'
    case SBlock  => 'S'
    case TBlock  => 'T'
    case ZBlock  => 'Z'
    case Empty   => '.'
  }
}


case class TetrisLogicWrapper
  (override val randomGen : RandomGenerator,
   override val nrColumns: Int, override val nrRows : Int,
   override val initialBoard : Seq[Seq[TetrisBlock]])
  extends TetrisLogic(randomGen,nrColumns ,nrRows,initialBoard) with GameLogicInterface[TetrisAction, TetrisGridTypeWrapper]{
  override def performAction(action: TetrisAction): Unit = action match {
    case RotateLeft => rotateLeft()
    case RotateRight => rotateRight()
    case Down => moveDown()
    case Left => moveLeft()
    case Right => moveRight()
    case Drop => doHardDrop()
  }

  override def getGridTypeAt(col: Int , row: Int): TetrisGridTypeWrapper = TetrisGridTypeWrapper(getBlockAt(col,row))
}


object TetrisRecord  extends GenericRecord
[TetrisAction, TetrisGridTypeWrapper, TetrisLogicWrapper, (Int,Int, Seq[Seq[TetrisBlock]])]() {
  def charToGridType(char: Char) : TetrisGridTypeWrapper = TetrisGridTypeWrapper(char match {
    case 'I' => IBlock
    case 'J' => JBlock
    case 'L' => LBlock
    case 'O' => OBlock
    case 'S' => SBlock
    case 'T' => TBlock
    case 'Z' => ZBlock
    case '.' => Empty
  })


  def emptyBoard( nrColumns : Int, nrRows : Int) : Seq[Seq[TetrisBlock]] = {
    val emptyLine = Seq.fill(nrColumns)(Empty)
    Seq.fill(nrRows)(emptyLine)
  }

  override def makeGame(random: RandomGenerator, initialInfo: (Int, Int, Seq[Seq[TetrisBlock]])): TetrisLogicWrapper =
    new TetrisLogicWrapper(random,initialInfo._1, initialInfo._2, initialInfo._3)

  override def gameLogicName: String = "TetrisLogic"


  object TetrisTest {
    def apply(name: String, frames: Seq[TestFrame]): Test = {
      val dimensions: (Int, Int) = frames.head.display match {
        case grid: GridDisplay => (grid.nrColumns, grid.nrRows)
        case _ => throw new Error("No grid display in test")
      }
      Test(name,(dimensions._1,dimensions._2, emptyBoard(dimensions._1,dimensions._2)),frames)
    }

    def parseInitialField(s : String) : Seq[Seq[TetrisBlock]] =
      gridString(s).grid.map(_.map(_.gridType))


    def apply(name: String, initial : String, frames: Seq[TestFrame]): Test = {
      val dimensions: (Int, Int) = frames.head.display match {
        case grid: GridDisplay => (grid.nrColumns, grid.nrRows)
        case x => throw new Error("No grid display in " ++ name ++ " got instead " ++ x.toString)
      }
      Test(name,(dimensions._1,dimensions._2, parseInitialField(initial)),frames)
    }
  }
}
