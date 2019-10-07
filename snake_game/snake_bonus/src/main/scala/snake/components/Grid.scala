package snake.components

import scala.collection.mutable

case class ApplePosition(var x: Int = 0, var y: Int = 0) extends Coordinates

class Grid(val nrRows: Int, val nrColumns: Int) {
  var nrFreeSpots = 0
  var applePositionsSet: Set[ApplePosition] = Set()

  private val hashTableOfFreeCell: mutable.Map[Int, Cell] = mutable.Map[Int, Cell]()
  val cells: mutable.Buffer[mutable.Buffer[Cell]] = mutable.Buffer.fill(nrRows, nrColumns)(new Cell())

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
        }
    } }
    nrFreeSpots = cellIndex
  }

  def updateApplePositions(): Unit = {
    applePositionsSet = Set()
    for (x <- 0 until nrRows; y <- 0 until nrColumns) {
      if (cells(x)(y).cellType == Apple()) {
        applePositionsSet = applePositionsSet + ApplePosition(x, y)
      }
    }
    println("Apple Positions: " + applePositionsSet.toString())
  }

  def printGridWithApple (): Unit = {
    for (row <- 0 until nrRows; col <- 0 until nrColumns) {
      if (col == nrColumns-1) print("\n")
      if (cells(row)(col).cellType == Apple()) print(" A")
      else print(" _")
    }
    println("$" * 100)
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

    for (row <- this.cells.indices; index <- 0 to this.nrFreeSpots) {
      that.cells(row).clear()
      that.hashTableOfFreeCell(index) = this.hashTableOfFreeCell(index).copy()
      this.cells(row).foreach { cell => that.cells(row) += new Cell(cell.cellType) }
    }
  }
}

object Grid {
  def apply(nrRows: Int, nrColumns: Int): Grid = new Grid(nrRows, nrColumns) { updateTableOfFreeCells() }
}
