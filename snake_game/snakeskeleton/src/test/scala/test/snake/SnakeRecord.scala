// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package test.snake

import engine.random.RandomGenerator
import snake.game._
import snake.logic.SnakeLogic
import test.{GameLogicInterface, GenericRecord, GridTypeInterface}


sealed abstract class SnakeAction
case class ChangeDir(dir: Direction) extends SnakeAction
case class ReverseGame(enable: Boolean) extends SnakeAction
case object Step extends SnakeAction


case class SnakeGridTypeWrapper(gridType: GridType)  extends GridTypeInterface[SnakeGridTypeWrapper] {
  override def conforms(rhs: SnakeGridTypeWrapper): Boolean = (gridType, rhs.gridType) match {
    case (SnakeBody(_), SnakeBody(_)) => true
    case (l, r) => l == r
  }

  override def toChar: Char = gridType match {
    case Empty() => '.'
    case SnakeHead(West()) => '<'
    case SnakeHead(East()) => '>'
    case SnakeHead(North()) => '^'
    case SnakeHead(South()) => 'v'
    case SnakeBody(_) => 'O'
    case Apple() => 'A'
  }
}


case class SnakeLogicWrapper(logic: SnakeLogic)
  extends GameLogicInterface[SnakeAction, SnakeGridTypeWrapper] {

  override def performAction(action: SnakeAction): Unit = action match {
    case ChangeDir(d) => logic.changeDir(d)
    case Step => logic.step()
    case ReverseGame(enable) => logic.setReverseTime(enable)
  }

  def getGridTypeAt(col: Int , row: Int): SnakeGridTypeWrapper =
    SnakeGridTypeWrapper(logic.getGridTypeAt(col,row))

  override def nrRows: Int = logic.nrRows
  override def nrColumns: Int = logic.nrColumns
  override def isGameOver: Boolean = logic.isGameOver

}

object SnakeRecord extends GenericRecord
    [SnakeAction, SnakeGridTypeWrapper, SnakeLogicWrapper, (Int, Int)]() {


  override def makeGame(r: RandomGenerator, info: (Int, Int)): SnakeLogicWrapper =
    SnakeLogicWrapper(new SnakeLogic(r,info._1, info._2))

  override def gameLogicName: String = "SnakeLogic"

  def charToGridType(char: Char): SnakeGridTypeWrapper =
    SnakeGridTypeWrapper(char match {
      case '.' => Empty()
      case '<' => SnakeHead(West())
      case '>' => SnakeHead(East())
      case '^' => SnakeHead(North())
      case 'v' => SnakeHead(South())
      case 'O' => SnakeBody(0.5f)
      case 'A' => Apple()
      case _ => Empty()
    })


  object SnakeTest {
    def apply(name : String, frames : Seq[TestFrame]): SnakeRecord.Test = {
      val dimensions: (Int, Int) = frames.head.display match {
        case grid: GridDisplay => (grid.nrColumns, grid.nrRows)
        case _ => throw new Error("No grid display in test")
      }

      def addStep(input : FrameInput) : FrameInput = FrameInput(input.randomNumber, input.actions  :+ Step)
      val framesWithStep : Seq[TestFrame] =
        frames.head +: frames.tail.map(frame => SnakeRecord.TestFrame(addStep(frame.input), frame.display))
      Test(name, dimensions,framesWithStep)
    }
  }


}
