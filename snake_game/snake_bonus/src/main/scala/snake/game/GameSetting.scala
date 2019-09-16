package snake.game

trait Setting {
  var level: Int
  var twoPlayerMode: Boolean
  var snakeSpeed = 0
}

case class GameSetting(var level: Int = 0,
                       var twoPlayerMode: Boolean = true) extends Setting
