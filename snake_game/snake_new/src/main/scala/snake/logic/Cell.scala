package snake.logic

import snake.game._

class Cell(var cellType: GridType = Empty().asInstanceOf[GridType]) {
  override def toString: String = cellType.toString

  def copy(): Cell = new Cell(cellType = this.cellType match {
    case ct @ SnakeHead(_) => ct.copy()
    case ct @ SnakeBody(_) => ct.copy()
    case Empty()           => Empty()
    case Apple()           => Apple()
  })
}