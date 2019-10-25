// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package tetris.game

import java.awt.event
import java.awt.event.KeyEvent._

import engine.GameBase
import engine.graphics.Color.Red
import engine.graphics.{Color, Point, Rectangle}
import processing.core.{PApplet, PConstants}
import processing.event.KeyEvent
import tetris.logic._
import tetris.game.TetrisGame._

class TetrisGame extends GameBase {

  var gameLogic = TetrisLogic()
  val updateTimer = new UpdateTimer(FramesPerSecond)
  val widthInPixels: Int = WidthCellInPixels * (gameLogic.nrColumns + InfoAreaWidthInCells)
  val heightInPixels: Int = HeightCellInPixels * gameLogic.nrRows
  val screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels, heightInPixels)
//  val infoArea: Rectangle = Rectangle(Point(InfoAreaWidthInCells-1, 0),InfoAreaWidthInCells-1, heightInPixels)

  override def draw(): Unit = {
    updateState()
    drawGrid()
    if (gameLogic.isGameOver) drawGameOverScreen()
  }

  def drawGameOverScreen(): Unit = {
    def drawFog(): Unit = {
      loadPixels()
      for(x <- Range(0, width); y <- Range(0, height)) {
        val loc = x + y * width
        val r = red(pixels(loc))
        val g = green(pixels(loc))
        val b = blue(pixels(loc))
        val distance = PApplet.dist(width/2, height/2, x, y)
        val factor = PApplet.map(distance, 0, 200, 2, 0)
        pixels(loc) = color(r*factor, g*factor, b*factor)
      }
      updatePixels()
    }
    drawFog()
    setFillColor(Color.LightYellow)
    drawTextCentered("GAME OVER!", 40, screenArea.center)
  }

  def drawGrid(): Unit = {
    val widthPerCell = screenArea.width / (gameLogic.nrColumns + InfoAreaWidthInCells)
    val heightPerCell = screenArea.height / gameLogic.nrRows

    for (y <- 0 until gameLogic.nrRows;
         x <- 0 until gameLogic.nrColumns) {
      drawCell(getCell(x, y), gameLogic.getBlockAt(x,y))
      drawCell(getCell(x+InfoAreaWidthInCells, y), InfoBg)
    }
//    setFillColor(Color.Gray)
//    drawRectangle(infoArea)

    def getCell(colIndex: Int, rowIndex: Int): Rectangle = {
      val leftUp = Point(screenArea.left + colIndex * widthPerCell,
        screenArea.top + rowIndex * heightPerCell)
      Rectangle(leftUp, widthPerCell, heightPerCell)
    }

    def drawCell(area: Rectangle, tetrisColor: TetrisBlock): Unit = {
      val color = tetrisBlockToColor(tetrisColor)
      setFillColor(color)
      drawRectangle(area)
    }

    if (!gameLogic.isGameOver) {
      drawNextTetrominos()
      drawHoldedTetromino()
    }

    def drawNextTetrominos(): Unit = {
      setFillColor(Color.LightGreen)
      textSize(26)
      text(s" NEXT", 14*WidthCellInPixels, 33)
      val x = 16
      var y = 4
      for (tetroType <- gameLogic.getNextTetrominos) {
        Tetromino(Coordinates(y, x), tetroType).getAllBlocksOfTetromino.foreach { blockCoor =>
          drawCell(getCell(blockCoor.x, blockCoor.y), tetroType)
        }
        y += 4
      }
    }

    def drawHoldedTetromino(): Unit = {
      setFillColor(Color.LightGreen)
      textSize(26)
      text(s" HOLD", 14*WidthCellInPixels, 16*HeightCellInPixels)

      val tetroType = gameLogic.getHoldedTetro
      Tetromino(Coordinates(18, 16), tetroType).getAllBlocksOfTetromino.foreach { blockCoor =>
        drawCell(getCell(blockCoor.x, blockCoor.y), tetroType)
      }
    }
  }

  /** Method that calls handlers for different key press events.
    * You may add extra functionality for other keys here.
    * See [[event.KeyEvent]] for all defined keycodes.
    *
    * @param event The key press event to handle
    */
  override def keyPressed(event: KeyEvent): Unit = {

    event.getKeyCode match {
      case VK_A     => gameLogic.rotateLeft()
      case VK_S     => gameLogic.rotateRight()
      case VK_UP    => gameLogic.rotateRight()
      case VK_W     => gameLogic.hold()
      case VK_DOWN  => gameLogic.moveDown()
      case VK_LEFT  => gameLogic.moveLeft()
      case VK_RIGHT => gameLogic.moveRight()
      case VK_SPACE => gameLogic.doHardDrop()
      case _        => ()
    }

  }

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    size(widthInPixels, heightInPixels, PConstants.P2D)
  }

  override def setup(): Unit = {
    // Fonts are loaded lazily, so when we call text()
    // for the first time, there is significant lag.
    // This prevents it from happening during gameplay.
    text("", 0, 0)

    // This should be called last, since the game
    // clock is officially ticking at this point
    updateTimer.init()
  }

  def updateState(): Unit = {
    if (updateTimer.timeForNextFrame()) {
      gameLogic.moveDown()
      updateTimer.advanceFrame()
    }
  }

  def tetrisBlockToColor(color: TetrisBlock): Color =
    color match {
      case IBlock => Color.LightBlue
      case OBlock => Color.Yellow
      case LBlock => Color.Orange
      case JBlock => Color.Blue
      case SBlock => Color.Green
      case Empty  => Color.Black
      case InfoBg => Color.Gray
      case TBlock => Color.Purple
      case ZBlock => Color.Red
    }
}

object TetrisGame {

  val FramesPerSecond: Int = 5
  val WidthCellInPixels: Int = 15
  val HeightCellInPixels: Int = WidthCellInPixels
  val InfoAreaWidthInCells: Int = 6

  def main(args:Array[String]): Unit = {
    val manual = new ManualPage
    manual.main(args)

    PApplet.main("tetris.game.TetrisGame")
  }

}