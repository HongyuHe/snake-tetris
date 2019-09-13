package snake.game

trait Coordinates {var x: Int; var y: Int}

class Cell(var cellType: GridType = Empty().asInstanceOf[GridType]) {
  def copy(): Cell = new Cell(cellType = this.cellType match {
      case cellType @ SnakeHead(_) => cellType.copy()
      case cellType @ SnakeBody(_) => cellType.copy()
      case Brick() => Brick()
      case Empty() => Empty()
      case Apple() => Apple()
    }
  )
}