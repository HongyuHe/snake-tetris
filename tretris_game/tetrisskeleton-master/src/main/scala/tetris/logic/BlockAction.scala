package tetris.logic

abstract class BlockAction

case object MoveRight    extends  BlockAction
case object MoveLeft     extends  BlockAction
case object MoveDown     extends  BlockAction
case object HardDrop     extends  BlockAction
case object RotateLeft   extends  BlockAction
case object RotateRight  extends  BlockAction

