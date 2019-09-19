package snake.game

case class GameStatus(var isGameOver: Boolean = false,
                      var isGridFull: Boolean = false,
                      var isSnakeCrashed: Boolean = false,
                      var isSnakeGrowing: Boolean = false,
                      var hasEnoughBombs: Boolean = false,
                      var hasEnoughApples: Boolean = false,
                      var appleEatenByRivalSnake: Boolean = false,
                      var appleEatenByNormalSnake: Boolean = false)