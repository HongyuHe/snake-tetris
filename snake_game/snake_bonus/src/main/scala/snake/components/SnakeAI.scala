package snake.components

import Snake._

case class SnakeAI () extends Snake() {
  id = AiSnake()

  def turnAI(applePositionsSet: Set[ApplePosition]): Unit = {
    val applePosition = applePositionsSet.head    // arbitrarily choose the first apple for now
    var marginX = applePosition.x - body.head.x
    var marginY = applePosition.y - body.head.y
    println(s"\n${(marginX, marginY)}\n")
    (marginX, marginY) match {
      case (mX, _) if mX > 0 => updateHeadAndBodyTypes(headDir = East())
      case (mX, _) if mX < 0 => updateHeadAndBodyTypes(headDir = West())
      case (_, mY) if mY > 0 => updateHeadAndBodyTypes(headDir = South())
      case (_, mY) if mY < 0 => updateHeadAndBodyTypes(headDir = North())
    }
  }
  println(body)
}

object SnakeAI {
  def apply(): SnakeAI = new SnakeAI() {
    body += SnakeTrunk(9, 2) += SnakeTrunk(9, 1) += SnakeTrunk(9, 0)
    updateHeadAndBodyTypes()

  }
}
