package snake.logic

import snake.game.{Empty, GridType}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Grid(
            nrRows: Int,
            nrColumns: Int) {

  var nrFreeSpots = 0

  private[this] val cells: ArrayBuffer[ArrayBuffer[Cell]] =
    ArrayBuffer.fill(nrRows, nrColumns)(new Cell())

  val freeCellTable: mutable.Map[Int, Cell] =
    mutable.Map[Int, Cell]()

  def setCellType(x: Int, y: Int, cellType: GridType): Unit = {
    cells(x)(y).cellType  = cellType
  }
  def setCellType(cell: Cell, cellType: GridType): Unit = {
    cell.cellType  = cellType
  }
  def setCellType(trunk: SnakeTrunk, cellType:GridType): Unit = {
    cells(trunk.x)(trunk.y).cellType = cellType
  }

  def getCellType(x: Int, y: Int): GridType = cells(x)(y).cellType
  def getCellType(trunk: SnakeTrunk): GridType = cells(trunk.x)(trunk.y).cellType

  def updateFreeCellTable(): Unit = {
    var index = 0

    cells.foreach { cell =>
      cell.foreach { c =>

        if (c.cellType == Empty()) {
          freeCellTable(index) = c
          index +=  1
        }
      }
    }
    nrFreeSpots = index
  }
}

object Grid {
  def apply(nrRows: Int, nrColumns: Int): Grid = new Grid(nrRows, nrColumns) {
    updateFreeCellTable()
  }
}
