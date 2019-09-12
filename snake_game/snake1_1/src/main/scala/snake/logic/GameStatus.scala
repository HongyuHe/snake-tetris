package snake.logic

//import scala.collection.mutable


case class GameStatus (
  var isGameOver: Boolean = false,
  var isGridFull: Boolean = false,
  var hasApple: Boolean = false,
  var isAppleAte: Boolean = false,
  var isSnakeCrashed: Boolean = false,
  var hasSnakeTurned: Boolean = false,
  var isSnakeGrowing: Boolean = false) {

//  override def iterator: Iterator[Boolean] = mutable.MutableList(
//    isGameOver,
//    isGridFull,
//    hasApple,
//    isAppleAte,
//    isSnakeCrashed,
//    hasSnakeTurned,
//    isSnakeGrowing).iterator
}
