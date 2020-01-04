package tetris.logic
import tetris.game._

trait TetrominoOrientation {
  type Eccentricity = Seq[(Int, Int)]
  val  eccentricities: Map[TetrisBlock, Eccentricity]

  def getCoorsFromCenter(tetrisBlock: TetrisBlock, center: Coordinates): Seq[Coordinates] =
    eccentricities(tetrisBlock).map(ecc => Coordinates(center.y + ecc._1, center.x + ecc._2))
}

case object Leftmost extends TetrominoOrientation {
  val eccentricities: Map[TetrisBlock, Eccentricity] = Map (
    IBlock -> Seq((0, 0), (0, +1), (0, +2), (0,  -1)),
    JBlock -> Seq((0, 0), (0, +1), (0, -1), (-1, -1)),
    LBlock -> Seq((0, 0), (0, +1), (0, -1), (-1, +1)),
    OBlock -> Seq((0, 0), (0, +1), (-1, 0), (-1, +1)),
    SBlock -> Seq((0, 0), (0, -1), (-1, 0), (-1, +1)),
    TBlock -> Seq((0, 0), (0, +1), (0, -1), (-1,  0)),
    ZBlock -> Seq((0, 0), (0, +1), (-1, 0), (-1, -1)),
  )
}
case object Rightmost extends TetrominoOrientation {
  val eccentricities: Map[TetrisBlock, Eccentricity] = Map (
    IBlock -> Seq((0, 0), (+1,  0), (+2,  0), (-1,  0)),
    JBlock -> Seq((0, 0), (+1, -1), (-1,  0), (+1,  0)),
    LBlock -> Seq((0, 0), (-1, -1), (-1,  0), (+1,  0)),
    OBlock -> Seq((0, 0), (-1,  0), ( 0, -1), (-1, -1)),
    SBlock -> Seq((0, 0), (-1, -1), ( 0, -1), (+1,  0)),
    TBlock -> Seq((0, 0), (-1,  0), ( 0, -1), (+1,  0)),
    ZBlock -> Seq((0, 0), ( 0, -1), (-1,  0), (+1, -1)),
  )
}
case object MiddleLeft extends TetrominoOrientation {
  val eccentricities: Map[TetrisBlock, Eccentricity] = Map (
    IBlock -> Seq((0, 1), (+1, +1), (+2, +1), (-1, +1)),
    JBlock -> Seq((0, 0), (+1,  0), (-1,  0), (-1, +1)),
    LBlock -> Seq((0, 0), (+1,  0), (-1,  0), (+1, +1)),
    OBlock -> Seq((0, 0), (+1,  0), ( 0, +1), (+1, +1)),
    SBlock -> Seq((0, 0), (-1,  0), ( 0, +1), (+1, +1)),
    TBlock -> Seq((0, 0), (-1,  0), ( 0, +1), (+1,  0)),
    ZBlock -> Seq((0, 0), (+1,  0), ( 0, +1), (-1, +1)),
  )
}
case object MiddleRight extends TetrominoOrientation {
  val eccentricities: Map[TetrisBlock, Eccentricity] = Map (
    IBlock -> Seq((1, 0), (+1, +1), (+1, +2), (+1, -1)),
    JBlock -> Seq((0, 0), ( 0, +1), ( 0, -1), (+1, +1)),
    LBlock -> Seq((0, 0), ( 0, +1), ( 0, -1), (+1, -1)),
    OBlock -> Seq((0, 0), ( 0, -1), (+1,  0), (+1, -1)),
    SBlock -> Seq((0, 0), ( 0, +1), (+1,  0), (+1, -1)),
    TBlock -> Seq((0, 0), ( 0, +1), ( 0, -1), (+1,  0)),
    ZBlock -> Seq((0, 0), ( 0, -1), (+1,  0), (+1, +1)),
  )
}