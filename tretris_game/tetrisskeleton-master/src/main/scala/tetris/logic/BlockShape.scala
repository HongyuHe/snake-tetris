package tetris.logic
import tetris.game._

trait BlockShape {
  type Offsets = Seq[(Int, Int)]
  val ShapeCoors: Map[TetrisBlock, Offsets]
  def getCoorsFromCenter(tetrisBlock: TetrisBlock, center: Coordinates): Seq[Coordinates] =
    ShapeCoors(tetrisBlock).map(offset => Coordinates(center.y + offset._1, center.x + offset._2))
}

case object LeftmostShape extends BlockShape {
  val ShapeCoors: Map[TetrisBlock, Offsets] = Map (
    IBlock -> Seq((0, 0), (0, +1), (0, +2), (0,  -1)),
    JBlock -> Seq((0, 0), (0, +1), (0, -1), (-1, -1)),
    LBlock -> Seq((0, 0), (0, +1), (0, -1), (-1, +1)),
    OBlock -> Seq((0, 0), (0, +1), (-1, 0), (-1, +1)),
    SBlock -> Seq((0, 0), (0, -1), (-1, 0), (-1, +1)),
    TBlock -> Seq((0, 0), (0, +1), (0, -1), (-1,  0)),
    ZBlock -> Seq((0, 0), (0, +1), (-1, 0), (-1, -1)),
  )
}
case object RightmostShape extends BlockShape {
  val ShapeCoors: Map[TetrisBlock, Offsets] = Map (
    IBlock -> Seq((0, 0), (+1,  0), (+2,  0), (-1,  0)),
    JBlock -> Seq((0, 0), (+1, -1), (-1,  0), (+1,  0)),
    LBlock -> Seq((0, 0), (-1, -1), (-1,  0), (+1,  0)),
    OBlock -> Seq((0, 0), (-1,  0), ( 0, -1), (-1, -1)),
    SBlock -> Seq((0, 0), (-1, -1), ( 0, -1), (+1,  0)),
    TBlock -> Seq((0, 0), (-1,  0), ( 0, -1), (+1,  0)),
    ZBlock -> Seq((0, 0), ( 0, -1), (-1,  0), (+1, -1)),
  )
}
case object MiddleLeftShape extends BlockShape {
  val ShapeCoors: Map[TetrisBlock, Offsets] = Map (
    IBlock -> Seq((0, 1), (+1, +1), (+2, +1), (-1, +1)),
    JBlock -> Seq((0, 0), (+1,  0), (-1,  0), (-1, +1)),
    LBlock -> Seq((0, 0), (+1,  0), (-1,  0), (+1, +1)),
    OBlock -> Seq((0, 0), (+1,  0), ( 0, +1), (+1, +1)),
    SBlock -> Seq((0, 0), (-1,  0), ( 0, +1), (+1, +1)),
    TBlock -> Seq((0, 0), (-1,  0), ( 0, +1), (+1,  0)),
    ZBlock -> Seq((0, 0), (+1,  0), ( 0, +1), (-1, +1)),
  )
}
case object MiddleRightShape extends BlockShape {
  val ShapeCoors: Map[TetrisBlock, Offsets] = Map (
    IBlock -> Seq((1, 0), (+1, +1), (+1, +2), (+1, -1)),
    JBlock -> Seq((0, 0), ( 0, +1), ( 0, -1), (+1, +1)),
    LBlock -> Seq((0, 0), ( 0, +1), ( 0, -1), (+1, -1)),
    OBlock -> Seq((0, 0), ( 0, -1), (+1,  0), (+1, -1)),
    SBlock -> Seq((0, 0), ( 0, +1), (+1,  0), (+1, -1)),
    TBlock -> Seq((0, 0), ( 0, +1), ( 0, -1), (+1,  0)),
    ZBlock -> Seq((0, 0), ( 0, -1), (+1,  0), (+1, +1)),
  )
}