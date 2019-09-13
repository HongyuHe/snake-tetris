package snake.logic

import snake.game.{Apple, Empty, GridType, SnakeBody, SnakeHead}

class Cell(var cellType: GridType = Empty().asInstanceOf[GridType]) {
  def copy(): Cell = new Cell(cellType = this.cellType match {
      case cellType @ SnakeHead(_) => cellType.copy()
      case cellType @ SnakeBody(_) => cellType.copy()
      case Empty() => Empty()
      case Apple() => Apple()
    }
  )
}