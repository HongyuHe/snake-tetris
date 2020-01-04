package snake.components

class Cell(var cellType: GridType = Empty().asInstanceOf[GridType]) {
  def copy(): Cell = new Cell(cellType = this.cellType match {
      case cellType @ SnakeHead(_, _) => cellType.copy()
      case cellType @ SnakeBody(_, _) => cellType.copy()
      case Wall() => Wall()
      case Empty() => Empty()
      case Apple() => Apple()
      case Bomb() => Bomb()
    }
  )
}