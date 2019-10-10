// scalastyle:off
package snake.components

import scala.math._
import scala.util.Random

/* *
  ——> y+
  |
  V x+
* */

class SnakeAI () extends Snake() {
  var vision: Map[Direction, GridType] = Map()

  id = AiSnake()
  var goStraightToggleFlag = false

  def chooseClosestApple(applePositionsSet: Set[ApplePosition]): ApplePosition = {
    val pythagoreanDistance: (Coordinates, Coordinates) => Double =
      (pointA, pointB) => sqrt( pow(pointA.x - pointB.x, 2) + pow(pointA.y - pointB.y, 2))

    val applePositionList = applePositionsSet.toList
    val appleDistancesList = applePositionList.map(pythagoreanDistance(_, body.head)).zip(applePositionList)

//    println(appleDistancesList)
    appleDistancesList.minBy{_._1}._2
  }

  def overviewGrid(grid: Grid): Unit = {
    vision = Map()
    vision += (West()  -> grid.getCellType(body.head.x, if (body.head.y - 1 < 0) grid.nrColumns - 1 else body.head.y - 1))
    vision += (North() -> grid.getCellType(if (body.head.x - 1 < 0) grid.nrRows - 1 else body.head.x - 1, body.head.y))
    vision += (South() -> grid.getCellType((body.head.x + 1) % grid.nrColumns, body.head.y))
    vision += (East()  -> grid.getCellType(body.head.x, (body.head.y + 1) % grid.nrColumns))

    println(vision)
  }

  def moveStraightTowardsApple(targetApple: ApplePosition): Unit = {
    goStraightToggleFlag = !goStraightToggleFlag

    val marginX = targetApple.x - body.head.x
    val marginY = targetApple.y - body.head.y


    //    println(s"\nMargins: ${(marginX, marginY)}")
    // every time before turning the snake you add a new head with East(). That's why you can't detect the current direction
    //    println(s"Opposite: ${preDir.opposite}")
    (marginX, marginY) match {
      case (mX, _) if mX > 0 &&
        goStraightToggleFlag &&
        preDir.opposite != South() => {
          updateBodyWithHeadDir(South())
      }
      case (mX, _) if mX < 0 &&
        goStraightToggleFlag &&
        preDir.opposite != North() => {
          updateBodyWithHeadDir(North())
      }
      case (_, mY) if mY > 0  &&
        !goStraightToggleFlag &&
        preDir.opposite != East() => {
          updateBodyWithHeadDir(East())
      }
      case (_, mY) if mY < 0  &&
        !goStraightToggleFlag &&
        preDir.opposite != West() => {
          updateBodyWithHeadDir(West())
      }
      case _ =>
    }
  }

  def hasObstacles: Boolean =
    vision
      .view.filterKeys(dir => dir != preDir.opposite)
      .values.exists  (t => t != Empty() && t != Apple() )

  def avoidObstacles(): Unit = {
    val nextStep = vision.view.filter(vis =>
      vis._1 != preDir.opposite && vis._2 == Empty())

    if (nextStep.nonEmpty)
      if (Random.nextBoolean()) // randomly choose an available free cell to prevent deadlock.
        updateBodyWithHeadDir(nextStep.head._1)
      else
        updateBodyWithHeadDir(nextStep.last._1)
//  else "Dead for sure ..."
  }

  def driveAI(grid: Grid): Unit = {

    overviewGrid(grid)
    println("Has obstacles:" + hasObstacles)

    //    val targetApple = applePositionsSet.head    // arbitrarily choose the first apple for now
    grid.updateApplePositions()

    if (hasObstacles) avoidObstacles()
    else moveStraightTowardsApple( chooseClosestApple(grid.applePositionsSet) )

    println(id + "Body: " +  " "+ body.toString())
  }




}

object SnakeAI {
  def apply(): SnakeAI = new SnakeAI() {
    body += SnakeTrunk(9, 2, SnakeHead()) += SnakeTrunk(9, 1) += SnakeTrunk(9, 0)
    updateBodyWithHeadDir()

  }
}
