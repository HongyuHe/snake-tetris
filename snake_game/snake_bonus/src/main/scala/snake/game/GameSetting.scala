package snake.game

trait Setting {
  var gameLevel: Int
  var twoPlayerMode: Boolean
  var battleWithAI: Boolean
  var startFlag: Boolean
  var gameSpeed = 0
}

case class GameSetting(var gameLevel: Int = 0,
                       var bombNumber: Int = 3,
                       var appleNumber: Int = 1,
                       var startFlag: Boolean = false,
                       var battleWithAI: Boolean = false,
                       var twoPlayerMode: Boolean = false,
                      ) extends Setting
