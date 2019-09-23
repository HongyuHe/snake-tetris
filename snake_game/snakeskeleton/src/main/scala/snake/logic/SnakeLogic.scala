package snake.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import snake.game._
import snake.logic.SnakeLogic._

/** To implement Snake, complete the ``TODOs`` below.
  *
  * If you need additional files,
  * please also put them in the ``snake`` package.
  */
class SnakeLogic(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int) {

  var grid : Array[Array[GridType]] = Array.ofDim[GridType](nrColumns, nrRows)
  var lastDirection : Direction = East()
  var currDirection : Direction = East()
  var appleLocation : (Int,Int) = (-1,-1)
  var snakeLocation = new Array[(Int,Int)](3)
  var gameOver = false
  startGame()

  def startGame(): Unit = {
    emptyGrid()

    grid(0)(0) = SnakeBody() // Place the first snake pieces
    snakeLocation(2) = (0,0)
    grid(1)(0) = SnakeBody()
    snakeLocation(1) = (1,0)
    grid(2)(0) = SnakeHead(East())
    snakeLocation(0) = (2,0)

    generateApple() // Generate the first apple
/* Review: use magic number in `appleLocation != (-1,-1)`
 *
 * @Offense: [Use of “magic number”]
 * @Points Deducted: -0.5
 * @Possible improvement: val GridIsFull = (-1, -1)
 */
    if(appleLocation != (-1,-1)) // (-1,-1) means the grid is full
      grid(appleLocation._1)(appleLocation._2) = Apple()
  }

  def this() = this(new ScalaRandomGen(), DefaultRows, DefaultColumns)

  def generateApple(): Unit = {
    var emptySpots = new Array[(Int, Int)](nrColumns*nrRows)
    var emptyCount = 0

    for(i<-0 until nrRows; j<-0 until nrColumns){ //Add all empty spots on the grid to an array
      if(grid(j)(i) == Empty()) {
        emptySpots(emptyCount) = (j, i)
        emptyCount += 1
      }
    }

    if(emptyCount == 0) appleLocation = (-1,-1) // (-1,-1) means the grid is full
    else appleLocation = emptySpots(randomGen.randomInt(emptyCount)) // Pick a random empty spot
  }

  def isGameOver: Boolean = {
    gameOver
  }

  def step(): Unit = {
    if(gameOver) //Check if player is dead
      return

    emptyGrid()

    moveSnakeForward()
    checkSnakeCollision() // Check if snake collided with itself
    placeSnakeOnGrid()

    checkAppleCollision() // Check if snake hit an apple
    if(appleLocation != (-1,-1)) // (-1,-1) means the grid is full
      grid(appleLocation._1)(appleLocation._2) = Apple()
  }

  def emptyGrid(): Unit = {
    for(i<-0 until nrColumns; j<-0 until nrRows){
      grid(i)(j) = Empty()
    }
  }

  def placeSnakeOnGrid(): Unit = {
    grid(snakeLocation(0)._1)(snakeLocation(0)._2) = SnakeHead(currDirection) // Place snake on grid
    for(i <- 1 until snakeLocation.length){
      if(snakeLocation(i) != null)
        grid(snakeLocation(i)._1)(snakeLocation(i)._2) = SnakeBody()
    }
  }

  def moveSnakeForward() : Unit = {
    var location = snakeLocation(0)
    moveSnakeArrayBack()
    currDirection match {
      case East() => snakeLocation(0) = ((location._1+1)%nrColumns,location._2)
      case West() => snakeLocation(0) = (((location._1-1)%nrColumns+nrColumns)%nrColumns,location._2)
      case North() => snakeLocation(0) = (location._1,(((location._2-1)%nrRows)+nrRows)%nrRows)
      case South() => snakeLocation(0) = (location._1,(location._2+1)%nrRows)
    }
    lastDirection = currDirection
  }

/* Review: method `checkSnakeCollision()` has a nested control structure (level == 3)
 *
 * @Offense: [Nested control structure]
 * @Points Deducted: -0.5
 * @Possible improvement: change logic or use high order function.
 */
  def checkSnakeCollision() : Unit = {
    var headLocation = snakeLocation(0)
    for(i <- 1 until snakeLocation.length){
      if(snakeLocation(i) != null) {
        if (headLocation._1 == snakeLocation(i)._1 && headLocation._2 == snakeLocation(i)._2) {
          gameOver = true
        }
      }
    }
  }

  def checkAppleCollision(): Unit = {
    if(snakeLocation(0)._1 == appleLocation._1 && snakeLocation(0)._2 == appleLocation._2){
      var newSnakeLocation = new Array[(Int,Int)](snakeLocation.length+3)
      for(i <- 0 until snakeLocation.length){
        newSnakeLocation(i) = snakeLocation(i)
      }
      snakeLocation = newSnakeLocation
      generateApple()
    }
  }

  def moveSnakeArrayBack(): Unit ={ // Shift the whole array back 1 position to make space for new snakehead
    for(i <- (snakeLocation.length-2) to 0 by -1){
      snakeLocation(i+1) = snakeLocation(i)
    }
  }

  def setReverseTime(reverse: Boolean): Unit = ()

/* Review: `changeDir()` re-implements the `opposite()` method
 *
 * @Offense: [Re-implementation instead of using library]
 * @Points Deducted: -0.1
 * @Possible improvement: use the library
 */
  def changeDir(d: Direction): Unit = { // Change direction if new direction is not the opposite of where the snake is going now
    d match {
       case East() => if(lastDirection != West()) currDirection = East();
       case North() => if(lastDirection != South()) currDirection = North();
       case West() => if(lastDirection != East()) currDirection = West();
       case South() => if(lastDirection != North()) currDirection = South();
    }
  }

  def getGridTypeAt(x: Int, y: Int): GridType = {
    grid(x)(y)
  }

}

/** SnakeLogic companion object */
object SnakeLogic {

  val DefaultColumns = 10
  val DefaultRows = 10

}


