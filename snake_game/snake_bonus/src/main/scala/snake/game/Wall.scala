package snake.game

import engine.random._

//case class WallBrick(var x: Int, var y: Int) extends Coordinates

//case class Wall() {
//  def generateVerticalWall(nrRows: Int, nrColumns: Int, nrBricks: Int): Vector[WallBrick] = {
//    val seedBrick = WallBrick(ScalaRandomGen().randomInt(nrRows), ScalaRandomGen().randomInt(nrColumns))
//    var vWall = Vector[WallBrick](seedBrick)
//    for (i <- 1 to nrBricks) {
//      vWall = vWall :+ WallBrick((seedBrick.x+i) % nrRows, seedBrick.y)
//    }
//    vWall
//  }
//}
