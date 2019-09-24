package tetris.logic

import tetris.game._
import tetris.logic.Block._

case class Coordinates (var y: Int = 0, var x: Int = 0) // (row, col)

case class Block (center: Coordinates = Coordinates(),
              var blockType: TetrisBlock = Empty,
              var blockShape: BlockShape = LeftmostShape) {
  def shapeRightRotate(): Unit = blockShape = BlockShapes(indexWrapper({index += 1; index}))
  def shapeLeftRotate():  Unit = blockShape = BlockShapes(indexWrapper({index -= 1; index}))

  def getTilesOfTheBlock: Seq[Coordinates] = {
    if (blockType != Empty) blockShape match {
      case shape @ LeftmostShape    => shape.getShapeCoors(blockType, center)
      case shape @ MiddleLeftShape  => shape.getShapeCoors(blockType, center)
      case shape @ MiddleRightShape => shape.getShapeCoors(blockType, center)
      case shape @ RightmostShape   => shape.getShapeCoors(blockType, center)
    } else Seq()
  }

  def indexWrapper(i: Int): Int = {
    val modulo = i % BlockShapes.length
    if (i >= 0 || modulo == 0) math.abs(modulo) else modulo + BlockShapes.length
  }

  private var index = 0
}

object Block {
  val BlockShapes: Seq[BlockShape] = Seq(LeftmostShape, MiddleLeftShape, MiddleRightShape, RightmostShape)

  def makeDeepCopy(orginalBlock: Block): Block = new Block(
    Coordinates(orginalBlock.center.y, orginalBlock.center.x),
    orginalBlock.blockType,
    orginalBlock.blockShape
  ) { this.index = orginalBlock.index }
}