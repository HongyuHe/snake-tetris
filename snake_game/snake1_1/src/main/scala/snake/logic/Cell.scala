package snake.logic

import snake.game.{Empty, GridType}

class Cell(var cellType: GridType = Empty().asInstanceOf[GridType])