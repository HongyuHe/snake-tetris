// scalastyle:off
package snake.components

import Snake._

class SnakeAI () extends Snake() {
  id = AiSnake()

  def turnAI(applePositionsSet: Set[ApplePosition]): Unit = {
    val applePosition = applePositionsSet.head    // arbitrarily choose the first apple for now
    var marginX = applePosition.x - body.head.x
    var marginY = applePosition.y - body.head.y

    /*
    -> y+
        |
        V x+
    * */

    println(s"\nMargins: ${(marginX, marginY)}")
    (marginX, marginY) match {
      case (mX, _) if mX > 0 => {
//        body.head.`type`.asInstanceOf[SnakeHead].direction = South()
        updateHeadAndBodyTypes(South())


        println("Go South!!!")
        println(body.head.`type`.asInstanceOf[SnakeHead].direction)
        println(id + "Body: " +  " "+ body.toString())

      }
      case (mX, _) if mX < 0 => {
        updateHeadAndBodyTypes(North())
//        body.head.`type`.asInstanceOf[SnakeHead].direction = North()
        println("Go North!!!")
        println(body.head.`type`.asInstanceOf[SnakeHead].direction)
        println(id + "Body: " +  " "+ body.toString())


      }
      case (_, mY) if mY > 0 => {
        updateHeadAndBodyTypes(East())

        //        body.head.`type`.asInstanceOf[SnakeHead].direction = East()
        println("Go East!!!")
        println(body.head.`type`.asInstanceOf[SnakeHead].direction)
        println(id + "Body: " +  " "+ body.toString())

      }
      case (_, mY) if mY < 0 => {
        updateHeadAndBodyTypes(West())
//        body.head.`type`.asInstanceOf[SnakeHead].direction = West()
        println("Go West!!!")
        println(body.head.`type`.asInstanceOf[SnakeHead].direction)
        println(id + "Body: " +  " "+ body.toString())

      }
    }
    println(id + "Body: " +  " "+ body.toString())
  }




}

object SnakeAI {
  def apply(): SnakeAI = new SnakeAI() {
    body += SnakeTrunk(9, 2, SnakeHead()) += SnakeTrunk(9, 1) += SnakeTrunk(9, 0)
    updateHeadAndBodyTypes()

  }
}
