package snake.logic

import scala.collection.mutable.ArrayBuffer
import snake.game._

case object Util {
  def printGrid(grid: Grid): Unit = {
    println("*"*50)
    println("[TEST] Print Grid:")

    grid.cells.foreach { cell =>
      cell.foreach { c =>
        print(c.cellType)
      }
      println()
    }
    println("*"*50)
  }
}
