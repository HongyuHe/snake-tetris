package tetris.logic

import tetris.game._
import tetris.logic.Tetromino._

case class Coordinates (var y: Int = 0, var x: Int = 0) // (row, col)

case class Tetromino(center: Coordinates = Coordinates(),
                     var blockType: TetrisBlock = Empty,
                     var orientation: TetrominoOrientation = Leftmost) {
  var orientationIndex = 0

  def setRightRotateOrientation(): Unit =
    orientation = TetroOrientations(indexWrapper({orientationIndex += 1; orientationIndex}))
  def setLeftRotateOrientation():  Unit =
    orientation = TetroOrientations(indexWrapper({orientationIndex -= 1; orientationIndex}))

  def getAllBlocksOfTetromino: Seq[Coordinates] =
    if (blockType != Empty) orientation.getCoorsFromCenter(blockType, center) else Seq()

  private[this] def indexWrapper(i: Int): Int = {
    val modulo = i % TetroOrientations.length
    if (i >= 0 || modulo == 0) math.abs(modulo) else modulo + TetroOrientations.length
  }
}

object Tetromino {
  val TetroOrientations: Seq[TetrominoOrientation] = Seq(Leftmost, MiddleLeft, MiddleRight, Rightmost)

  def makeDeepCopy(orginalTero: Tetromino): Tetromino = new Tetromino(
    Coordinates(orginalTero.center.y, orginalTero.center.x),
    orginalTero.blockType,
    orginalTero.orientation
  ) { this.orientationIndex = orginalTero.orientationIndex }
}