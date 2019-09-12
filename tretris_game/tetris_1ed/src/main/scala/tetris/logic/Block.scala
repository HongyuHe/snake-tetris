package tetris.logic

import tetris.game._

case class Coordinates (var y: Int = 0, var x: Int = 0) // (row, col)

case class Block (val center: Coordinates = Coordinates(),
                  var blockType: TetrisBlock = Empty,
                  var blockShape: BlockShape = LeftmostShape,
               ) {
  var index = 0
  val BlockShapes: Seq[BlockShape] = Seq(LeftmostShape, MiddleLeftShape, MiddleRightShape, RightmostShape)

  def shapeRightRotate(): Unit = blockShape = BlockShapes(indexWrapper({index += 1; index}))
  def shapeLeftRotate():  Unit = blockShape = BlockShapes(indexWrapper({index -= 1; index}))

  def getBlockTiles: Seq[Coordinates] = {
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
}

object Block {
  def apply(oldBlock: Block): Block = new Block(
    Coordinates(oldBlock.center.y, oldBlock.center.x),
    oldBlock.blockType,
    oldBlock.blockShape
  ) {
    this.index = oldBlock.index
  }
}