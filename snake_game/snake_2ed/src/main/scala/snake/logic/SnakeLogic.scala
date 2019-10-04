package snake.logic

import snake.logic.SnakeLogic._
import scala.collection.mutable
import snake.game.{Direction, GridType}
import engine.random.{RandomGenerator, ScalaRandomGen}

class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {

  def this() = this(new ScalaRandomGen(), DefaultColumns, DefaultRows)

  def isGameOver: Boolean = frameController.status.isGameOver
  def changeDir(d: Direction): Unit = frameController.turnSnake(d)
  def getGridTypeAt(x: Int, y: Int): GridType = frameController.grid.getCellType(y, x)

  def step(): Unit = {
    if (isReversing  && frameHistory.nonEmpty)  retrieveFrame()
    if (!isReversing && !frameController.status.isGameOver) { frameController.update(); saveFrame() }
  }

  def setReverseTime(reverse: Boolean): Unit = {
    if (!reverse && isReversing)           saveFrame()
    if (reverse  && frameHistory.nonEmpty) retrieveFrame()
    isReversing = reverse
  }

  private[this] var isReversing = false
  private[this] val frameHistory = mutable.Stack[FrameController]()
  private[this] var frameController = FrameController(nrRows, nrColumns, randomGen)

  private[this] def saveFrame()():    Unit = frameHistory.push(frameController.makeDeepCopy)
  private[this] def retrieveFrame():  Unit = frameController = frameHistory.pop()

  saveFrame() // save game the first frame before game starts
}

object SnakeLogic {
  val DefaultColumns = 25
  val DefaultRows = 25
}