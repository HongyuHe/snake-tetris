package tetris.logic
import tetris.game._

trait BlockShape {
  def getShapeCoors(tetrisBlock: TetrisBlock, center: Coordinates): Seq[Coordinates]
}

case object LeftmostShape     extends BlockShape {
  def getShapeCoors(tetrisBlock: TetrisBlock, center: Coordinates): Seq[Coordinates] = {
    tetrisBlock match {
      case IBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y, center.x+2), Coordinates(center.y,   center.x-1))
      case JBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y, center.x-1), Coordinates(center.y-1, center.x-1))
      case LBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y, center.x-1), Coordinates(center.y-1, center.x+1))
      case OBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y-1, center.x), Coordinates(center.y-1, center.x+1))
      case SBlock => Seq(center, Coordinates(center.y, center.x-1), Coordinates(center.y-1, center.x), Coordinates(center.y-1, center.x+1))
      case TBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y, center.x-1), Coordinates(center.y-1, center.x  ))
      case ZBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y-1, center.x), Coordinates(center.y-1, center.x-1))
    }
  }
}
case object MiddleLeftShape   extends BlockShape {
  def getShapeCoors(tetrisBlock: TetrisBlock, center: Coordinates): Seq[Coordinates] = {
    tetrisBlock match {
      case IBlock => Seq(Coordinates(center.y, center.x+1), Coordinates(center.y+1, center.x+1), Coordinates(center.y+2, center.x+1), Coordinates(center.y-1, center.x+1))
      case JBlock => Seq(center, Coordinates(center.y+1, center.x), Coordinates(center.y-1, center.x), Coordinates(center.y-1, center.x+1))
      case LBlock => Seq(center, Coordinates(center.y+1, center.x), Coordinates(center.y-1, center.x), Coordinates(center.y+1, center.x+1))
      case OBlock => Seq(center, Coordinates(center.y+1, center.x), Coordinates(center.y, center.x+1), Coordinates(center.y+1, center.x+1))
      case SBlock => Seq(center, Coordinates(center.y-1, center.x), Coordinates(center.y, center.x+1), Coordinates(center.y+1, center.x+1))
      case TBlock => Seq(center, Coordinates(center.y-1, center.x), Coordinates(center.y, center.x+1), Coordinates(center.y+1, center.x  ))
      case ZBlock => Seq(center, Coordinates(center.y+1, center.x), Coordinates(center.y, center.x+1), Coordinates(center.y-1, center.x+1))
    }
  }
}
case object MiddleRightShape  extends BlockShape {
  def getShapeCoors(tetrisBlock: TetrisBlock, center: Coordinates): Seq[Coordinates] = {
    tetrisBlock match {
      case IBlock => Seq(Coordinates(center.y+1, center.x), Coordinates(center.y+1, center.x+1), Coordinates(center.y+1, center.x+2), Coordinates(center.y+1, center.x-1))
      case JBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y, center.x-1), Coordinates(center.y+1, center.x+1))
      case LBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y, center.x-1), Coordinates(center.y+1, center.x-1))
      case OBlock => Seq(center, Coordinates(center.y, center.x-1), Coordinates(center.y+1, center.x), Coordinates(center.y+1, center.x-1))
      case SBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y+1, center.x), Coordinates(center.y+1, center.x-1))
      case TBlock => Seq(center, Coordinates(center.y, center.x+1), Coordinates(center.y, center.x-1), Coordinates(center.y+1, center.x  ))
      case ZBlock => Seq(center, Coordinates(center.y, center.x-1), Coordinates(center.y+1, center.x), Coordinates(center.y+1, center.x+1))
    }
  }
}
case object RightmostShape    extends BlockShape {
  def getShapeCoors(tetrisBlock: TetrisBlock, center: Coordinates): Seq[Coordinates] = {
    tetrisBlock match {
      case IBlock => Seq(center, Coordinates(center.y+1, center.x), Coordinates(center.y+2, center.x), Coordinates(center.y-1, center.x))
      case JBlock => Seq(center, Coordinates(center.y+1, center.x-1), Coordinates(center.y-1, center.x), Coordinates(center.y+1, center.x))
      case LBlock => Seq(center, Coordinates(center.y-1, center.x-1), Coordinates(center.y-1, center.x), Coordinates(center.y+1, center.x))
      case OBlock => Seq(center, Coordinates(center.y-1, center.x), Coordinates(center.y, center.x-1), Coordinates(center.y-1, center.x-1))
      case SBlock => Seq(center, Coordinates(center.y-1, center.x-1), Coordinates(center.y, center.x-1), Coordinates(center.y+1, center.x))
      case TBlock => Seq(center, Coordinates(center.y-1, center.x), Coordinates(center.y, center.x-1), Coordinates(center.y+1, center.x  ))
      case ZBlock => Seq(center, Coordinates(center.y, center.x-1), Coordinates(center.y-1, center.x), Coordinates(center.y+1, center.x-1))
    }
  }
}

