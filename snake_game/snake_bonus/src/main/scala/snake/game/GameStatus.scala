package snake.game

case class GameStatus(var hasApple:       Boolean = false,
                      var isGameOver:     Boolean = false,
                      var isGridFull:     Boolean = false,
                      var isSnakeCrashed: Boolean = false,
                      var isSnakeGrowing: Boolean = false,
                      var appleEatenByNormalSnake: Boolean = false,
                      var appleEatenByRivalSnake: Boolean = false,
                     )