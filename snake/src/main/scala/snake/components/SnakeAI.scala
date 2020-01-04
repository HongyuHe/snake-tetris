// scalastyle:off
package snake.components

import scala.collection.immutable.ListMap
import scala.math._
import scala.util.Random

/* *
  ——> y+
  |
  V x+
* */

class SnakeAI () extends Snake() {
  var vision: Map[Direction, List[GridType]] = Map()

  id = AiSnake()
  var goStraightToggleFlag = false

  def chooseClosestApple(applePositionsSet: Set[ApplePosition]): ApplePosition = {
    val pythagoreanDistance: (Coordinates, Coordinates) => Double =
      (pointA, pointB) => sqrt( pow(pointA.x - pointB.x, 2) + pow(pointA.y - pointB.y, 2))

    val applePositionList = applePositionsSet.toList
    val appleDistancesList = applePositionList.map(pythagoreanDistance(_, body.head)).zip(applePositionList)

//    println(appleDistancesList)
    appleDistancesList.minBy(_._1)._2
  }

  def overviewGrid(grid: Grid): Unit = {
    val VISION_RADIUS = 7
    val westPath =
      for (i <- 1 to VISION_RADIUS) yield grid.getCellType(grid.westDirIndexWrapper(body.head.x, body.head.y-i))
//      List(grid.getCellType(grid.westDirIndexWrapper(body.head.x, body.head.y-1)),
//                         grid.getCellType(grid.westDirIndexWrapper(body.head.x, body.head.y-2)))
    val eastPath =
      for (i <- 1 to VISION_RADIUS) yield grid.getCellType(grid.eastDirIndexWrapper(body.head.x, body.head.y+i))
//List(grid.getCellType(grid.eastDirIndexWrapper(body.head.x, body.head.y+1)),
//                         grid.getCellType(grid.eastDirIndexWrapper(body.head.x, body.head.y+2)))
    val northPath =
      for (i <- 1 to VISION_RADIUS) yield grid.getCellType(grid.northDirIndexWrapper(body.head.x-i, body.head.y))
//List(grid.getCellType(grid.northDirIndexWrapper(body.head.x-1, body.head.y)),
//                         grid.getCellType(grid.northDirIndexWrapper(body.head.x-2, body.head.y)))
    val southPath =
      for (i <- 1 to VISION_RADIUS) yield grid.getCellType(grid.southDirIndexWrapper(body.head.x+i, body.head.y))
//List(grid.getCellType(grid.southDirIndexWrapper(body.head.x+1, body.head.y)),
//                         grid.getCellType(grid.southDirIndexWrapper(body.head.x+2, body.head.y)))
    vision = Map()
    vision += (West()  -> westPath.toList)
    vision += (North() -> northPath.toList)
    vision += (South() -> southPath.toList)
    vision += (East()  -> eastPath.toList)

    //    vision = Map()
//    vision += (West()  -> grid.getCellType(body.head.x, if (body.head.y - 1 < 0) grid.nrColumns - 1 else body.head.y - 1))
//    vision += (North() -> grid.getCellType(if (body.head.x - 1 < 0) grid.nrRows - 1 else body.head.x - 1, body.head.y))
//    vision += (South() -> grid.getCellType((body.head.x + 1) % grid.nrColumns, body.head.y))
//    vision += (East()  -> grid.getCellType(body.head.x, (body.head.y + 1) % grid.nrColumns))

//    println("Vision: " + vision)
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
      .view
      .filterKeys(dir => dir != preDir.opposite)
      .values.exists (path => path.head != Empty() && path.head != Apple() )

  def avoidObstacles(): Unit = {
    def makeDecision(): Option[Direction] = {
      var pathsAvailableSteps: Map[Direction, Int] = Map()
      vision.view.filter( vis =>
        vis._1 != preDir.opposite).foreach( path =>
          pathsAvailableSteps +=
            (path._1 ->
              path._2
                .takeWhile(t => t == Empty() || t == Apple())
                .length))

      val prioritisedPaths = pathsAvailableSteps.toSeq.sortBy(- _._2)
      val bestPath =
        if (prioritisedPaths.head._2 == prioritisedPaths(1)._2)
          prioritisedPaths(Random.nextInt(2)) // make random decision to prevent deadlock.
        else
          prioritisedPaths.head

      println("Available steps:" + pathsAvailableSteps)
      if (bestPath._2 != 0) Option(bestPath._1)
      else None
    }

    val nextStep = makeDecision().getOrElse(None)
//      vision.view.filter(vis =>
//      vis._1 != preDir.opposite && vis._2 == Empty())
    println("Decision: " + nextStep + "\n")

    if (nextStep != None)
      updateBodyWithHeadDir(nextStep.asInstanceOf[Direction])
//  else "Dead for sure ..."

//    if (Random.nextBoolean()) // randomly choose an available free cell to prevent deadlock.
//      else
//        updateBodyWithHeadDir(nextStep.last._1)
  }

  def driveAI(grid: Grid): Unit = {

    overviewGrid(grid)
    println("Has obstacles:" + hasObstacles)

    //    val targetApple = applePositionsSet.head    // arbitrarily choose the first apple for now
    grid.updateApplePositions()

    if (hasObstacles) { avoidObstacles() }
    else moveStraightTowardsApple(
      chooseClosestApple(grid.applePositionsSet)
    )

//    println(id + "Body: " +  " "+ body.toString())
  }




}

object SnakeAI {
  def apply(): SnakeAI = new SnakeAI() {
    body += SnakeTrunk(9, 2, SnakeHead()) += SnakeTrunk(9, 1) += SnakeTrunk(9, 0)
    updateBodyWithHeadDir()

  }
}
