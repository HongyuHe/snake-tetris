package snake.logic

import snake.game.{Empty, GridType, Apple}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

case class Grid(
                 nrRows: Int,
                 nrColumns: Int) {

  var nrFreeSpots = 0

  var cells: mutable.Buffer[mutable.Buffer[Cell]] =
    mutable.Buffer.fill(nrRows, nrColumns)(new Cell())

  val freeCellTable: mutable.Map[Int, Cell] =
    mutable.Map[Int, Cell]()

  def setCellType(x: Int, y: Int, cellType: GridType): Unit = {
    cells(x)(y).cellType = cellType
  }

  def setCellType(cell: Cell, cellType: GridType): Unit = {
    cell.cellType = cellType
  }

  def setCellType(trunk: SnakeTrunk, cellType: GridType): Unit = {
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
          index += 1
        }
      }
    }
    nrFreeSpots = index
  }

  def findApple(): (Int, Int) = {
    for (row <- cells.indices) {
      val col = cells(row).indexOf(new Cell(Apple()))
      if (col > 0)
        return (row, col)
    }
    (-1, -1) // Magic Number!
  }

  def copy(): Grid = new Grid(nrRows, nrColumns) {
    nrFreeSpots = this.nrFreeSpots
    cells.clear()
    for (row <- cells.indices) {
      cells(row).clear()
      this.cells.copyToBuffer(cells.asInstanceOf[mutable.Buffer[mutable.Buffer[Object]]](row))
    }
    for (index <- 0 to this.nrFreeSpots) {
      try {
        freeCellTable(index) = this.freeCellTable(index).copy()
      } catch {
        case _: Throwable =>
      }
    }
  }

  def copyTo(new_grid: Grid): Unit ={
    new_grid.nrFreeSpots = this.nrFreeSpots
    for (row <- this.cells.indices) {
      new_grid.cells(row).clear()
      this.cells(row).foreach { cell =>
        new_grid.cells(row) += new Cell(cell.cellType) // cell.copy()
      }
    }
    for(index <- 0 to this.nrFreeSpots)
      new_grid.freeCellTable(index) = this.freeCellTable(index).copy()
  }
  
  override def toString: String = cells.toString()
}

object Grid {
  def apply(nrRows: Int, nrColumns: Int): Grid = new Grid(nrRows, nrColumns) {
    updateFreeCellTable()
  }

  def apply(grid: Grid, nrRows: Int, nrColumns: Int): Grid = new Grid(nrRows, nrColumns) {
//    updateFreeCellTable()
    this.nrFreeSpots = grid.nrFreeSpots
    this.cells.clear()
    grid.cells.copyToBuffer(this.cells)

    for (index <- 0 to grid.nrFreeSpots) {
      this.freeCellTable(index) = grid.freeCellTable(index).copy()
    }
  }
}
