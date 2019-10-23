package tetris.logic

abstract class TetrominoAction

case object MoveRight    extends  TetrominoAction
case object MoveLeft     extends  TetrominoAction
case object MoveDown     extends  TetrominoAction
case object HardDrop     extends  TetrominoAction
case object RotateLeft   extends  TetrominoAction
case object RotateRight  extends  TetrominoAction

