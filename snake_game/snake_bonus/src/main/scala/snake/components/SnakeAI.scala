// scalastyle:off
package snake.components

import scala.math._


/* *
  ——> y+
  |
  V x+
* */

class SnakeAI () extends Snake() {
  id = AiSnake()
  var goStraightToggleFlag = false

  def chooseApple(applePositionsSet: Set[ApplePosition]): ApplePosition = {
    val pythagoreanDistance: (Coordinates, Coordinates) => Double =
      (pointA, pointB) => sqrt( pow(pointA.x - pointB.x, 2) + pow(pointA.y - pointB.y, 2))

    val applePositionList = applePositionsSet.toList
    val appleDistancesList = applePositionList.map(pythagoreanDistance(_, body.head)).zip(applePositionList)

    println(appleDistancesList)
    appleDistancesList.minBy{_._1}._2
  }

  def turnAI(applePositionsSet: Set[ApplePosition]): Unit = {
    goStraightToggleFlag = !goStraightToggleFlag

    val targetApple = chooseApple(applePositionsSet)

//    val targetApple = applePositionsSet.head    // arbitrarily choose the first apple for now
    val marginX = targetApple.x - body.head.x
    val marginY = targetApple.y - body.head.y


    println(s"\nMargins: ${(marginX, marginY)}")
    // every time before turning the snake you add a new head with East(). That's why you can't detect the current direction
//    println(s"Opposite: ${preDir.opposite}")
    (marginX, marginY) match {
      case (mX, _) if mX > 0 &&
        goStraightToggleFlag &&
        preDir.opposite != South() => {
//        body.head.`type`.asInstanceOf[SnakeHead].direction = South()
        updateBodyWithHeadDir(South())


//        println("Go South!!!")
//        println(body.head.`type`.asInstanceOf[SnakeHead].direction)
//        println(id + "Body: " +  " "+ body.toString())

      }
      case (mX, _) if mX < 0 &&
        goStraightToggleFlag &&
        preDir.opposite != North() => {
        updateBodyWithHeadDir(North())
//        body.head.`type`.asInstanceOf[SnakeHead].direction = North()
//        println("Go North!!!")
//        println(body.head.`type`.asInstanceOf[SnakeHead].direction)
//        println(id + "Body: " +  " "+ body.toString())


      }
      case (_, mY) if mY > 0  &&
        !goStraightToggleFlag &&
        preDir.opposite != East() => {
        updateBodyWithHeadDir(East())

        //        body.head.`type`.asInstanceOf[SnakeHead].direction = East()
//        println("Go East!!!")
//        println(body.head.`type`.asInstanceOf[SnakeHead].direction)
//        println(id + "Body: " +  " "+ body.toString())

      }
      case (_, mY) if mY < 0  &&
        !goStraightToggleFlag &&
        preDir.opposite != West() => {
        updateBodyWithHeadDir(West())
//        body.head.`type`.asInstanceOf[SnakeHead].direction = West()
//        println("Go West!!!")
//        println(body.head.`type`.asInstanceOf[SnakeHead].direction)
//        println(id + "Body: " +  " "+ body.toString())

      }
      case _ =>
    }
    println(id + "Body: " +  " "+ body.toString())
  }




}

object SnakeAI {
  def apply(): SnakeAI = new SnakeAI() {
    body += SnakeTrunk(9, 2, SnakeHead()) += SnakeTrunk(9, 1) += SnakeTrunk(9, 0)
    updateBodyWithHeadDir()

  }
}
