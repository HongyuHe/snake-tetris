package snake.components

import scala.collection.mutable

class Grid(nrRows: Int, nrColumns: Int) {
  var nrFreeSpots = 0

  private val hashTableOfFreeCell: mutable.Map[Int, Cell] = mutable.Map[Int, Cell]()
  private val cells: mutable.Buffer[mutable.Buffer[Cell]] = mutable.Buffer.fill(nrRows, nrColumns)(new Cell())

  def getFreeCell(cellIndex: Int): Cell = hashTableOfFreeCell(cellIndex)

  def getCellType(x: Int, y: Int):    GridType = cells(x)(y).cellType
  def getCellType(trunk: SnakeTrunk): GridType = cells(trunk.x)(trunk.y).cellType

  def setCellType(cell: Cell, cellType: GridType):        Unit = cell.cellType = cellType
  def setCellType(x: Int, y: Int, cellType: GridType):    Unit = cells(x)(y).cellType = cellType
  def setCellType(trunk: SnakeTrunk, cellType: GridType): Unit = cells(trunk.x)(trunk.y).cellType = cellType

  def updateTableOfFreeCells(): Unit = {
    var cellIndex = 0
    cells.foreach { rows => rows.foreach { cell =>
        if (cell.cellType == Empty()) {
          hashTableOfFreeCell(cellIndex) = cell
          cellIndex += 1
        } } }
    nrFreeSpots = cellIndex
  }

  def getItemAmount(item: GridType): Int = {
    var counter = 0
    cells.foreach { rows => rows.foreach { cell =>
      if (cell.cellType == item) {
        counter += 1
      } } }
    counter
  }

  def copyTo(that: Grid): Unit = {
    that.nrFreeSpots = this.nrFreeSpots

    for (row <- this.cells.indices) {
      that.cells(row).clear()
      this.cells(row).foreach { cell => that.cells(row) += new Cell(cell.cellType) }
    }
    for (index <- 0 to this.nrFreeSpots)
      that.hashTableOfFreeCell(index) = this.hashTableOfFreeCell(index).copy()
  }
}

object Grid {
  def apply(nrRows: Int, nrColumns: Int): Grid = new Grid(nrRows, nrColumns) { updateTableOfFreeCells() }
}
