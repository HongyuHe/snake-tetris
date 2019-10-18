package tetris.logic

import tetris.game._
import tetris.logic.Block._

case class Coordinates (var y: Int = 0, var x: Int = 0) // (row, col)

case class Block (center: Coordinates = Coordinates(),
                  var blockType: TetrisBlock = Empty,
                  var blockShape: BlockShape = LeftmostShape) {
  var shapeIndex = 0

  def setRightRotateShape(): Unit = blockShape = BlockShapes(indexWrapper({shapeIndex += 1; shapeIndex}))
  def setLeftRotateShape():  Unit = blockShape = BlockShapes(indexWrapper({shapeIndex -= 1; shapeIndex}))

  def getAllTilesOfTheBlock: Seq[Coordinates] = if (blockType != Empty) blockShape.getCoorsFromCenter(blockType, center) else Seq()

  private[this] def indexWrapper(i: Int): Int = {
    val modulo = i % BlockShapes.length
    if (i >= 0 || modulo == 0) math.abs(modulo) else modulo + BlockShapes.length
  }
}

object Block {
  val BlockShapes: Seq[BlockShape] = Seq(LeftmostShape, MiddleLeftShape, MiddleRightShape, RightmostShape)

  def makeDeepCopy(orginalBlock: Block): Block = new Block(
    Coordinates(orginalBlock.center.y, orginalBlock.center.x),
    orginalBlock.blockType,
    orginalBlock.blockShape
  ) { this.shapeIndex = orginalBlock.shapeIndex }
}