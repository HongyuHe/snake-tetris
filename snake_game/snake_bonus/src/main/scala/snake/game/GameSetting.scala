package snake.game

trait Setting {
  var level: Int
  var twoPlayerMode: Boolean
  var speed: Double
}

case class GameSetting(var level: Int = 2,
                       var twoPlayerMode: Boolean = true,
                       var speed: Double = 0) extends Setting
