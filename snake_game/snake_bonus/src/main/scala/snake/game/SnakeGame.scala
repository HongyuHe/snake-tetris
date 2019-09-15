// scalastyle:off

package snake.game

import java.awt.event

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import snake.components._
import snake.game.SnakeGame._
//import processing.core.{PApplet, PConstants}
import java.awt.event.KeyEvent._

import engine.GameBase
import engine.graphics.Color._
import engine.graphics.{Color, Point, Rectangle}
import processing.core._
import processing.event.KeyEvent

class SnakeGame extends GameBase {
  var startFlag = false
  var gameLogic = new SnakeLogic()
  val updateTimer = new UpdateTimer(FramesPerSecond)
  val widthInPixels: Int = WidthCellInPixels * gameLogic.nrColumns
  val heightInPixels: Int = HeightCellInPixels * gameLogic.nrRows
  val screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels, heightInPixels)

  // this function is wrongly named draw by processing (is called on each update next to drawing)
  override def draw(): Unit = {
    //    if (!startFlag) {
    //      val solver = new Solver
    //      val ui = new StartUI(solver)
    //      ui.visible = true
    //      startFlag = true
    //    }
    updateState()
    drawGrid()
    if (gameLogic.isGameOver) drawGameOverScreen()
  }

  def drawGameOverScreen(): Unit = {
    setFillColor(Red)
    drawTextCentered("GAME OVER!", 20, screenArea.center)
  }

  def drawGrid(): Unit = {

    val widthPerCell = screenArea.width / gameLogic.nrColumns
    val heightPerCell = screenArea.height / gameLogic.nrRows

    def getCell(colIndex: Int, rowIndex: Int): Rectangle = {
      val leftUp = Point(screenArea.left + colIndex * widthPerCell,
        screenArea.top + rowIndex * heightPerCell)
      Rectangle(leftUp, widthPerCell, heightPerCell)
    }

    def getTriangleForDirection(dir: Direction, area: Rectangle) = {
      dir match {
        case West() => area.trianglePointingLeft
        case North() => area.trianglePointingUp
        case East() => area.trianglePointingRight
        case South() => area.trianglePointingDown
      }
    }

    def drawCell(area: Rectangle, cell: GridType): Unit = {
      cell match {
        case SnakeHead(id, direction) if id == "normal" =>
          setFillColor(Color.LawnGreen)
          drawTriangle(getTriangleForDirection(direction, area))
        case SnakeBody(id, p) if id == "normal" =>
          val color = Color.LawnGreen.interpolate(p, Color.DarkGreen)
          setFillColor(color)
          drawRectangle(area)
        case SnakeHead(id, direction) if id == "rival" =>
          setFillColor(Color.Yellow)
          drawTriangle(getTriangleForDirection(direction, area))
        case SnakeBody(id, p) if id == "rival" =>
          val color = Color.Yellow.interpolate(p, Color.Orange)
          setFillColor(color)
          drawRectangle(area)
        case Apple() =>
          setFillColor(Color.Red)
          drawEllipse(area)
        case Wall() =>
          setFillColor(Color.Gray)
          drawRectangle(area)
        case Empty() => ()
      }
    }

    setFillColor(White)
    drawRectangle(screenArea)

    for (y <- 0 until gameLogic.nrRows;
         x <- 0 until gameLogic.nrColumns) {
      drawCell(getCell(x, y), gameLogic.getGridTypeAt(x, y))
    }

  }

  /** Method that calls handlers for different key press events.
    * You may add extra functionality for other keys here.
    * See [[event.KeyEvent]] for all defined keycodes.
    *
    * @param event The key press event to handle
    */
  override def keyPressed(event: KeyEvent): Unit = {

    def changeDir(dir: Direction): Unit = gameLogic.changeDir(dir)

    def changeRivalDir(dir: Direction): Unit = gameLogic.changeRivalDir(dir)

    event.getKeyCode match {
      case VK_R => gameLogic.setReverseTime(true)
      case VK_W => changeRivalDir(North())
      case VK_S => changeRivalDir(South())
      case VK_A => changeRivalDir(West())
      case VK_D => changeRivalDir(East())
      case VK_UP => changeDir(North())
      case VK_DOWN => changeDir(South())
      case VK_LEFT => changeDir(West())
      case VK_RIGHT => changeDir(East())
      case _ => ()
    }
  }

  override def keyReleased(event: KeyEvent): Unit = {
    event.getKeyCode match {
      case VK_R => gameLogic.setReverseTime(false)
      case _ => ()
    }
  }

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    size(widthInPixels, heightInPixels, PConstants.P3D)
  }

  override def setup(): Unit = {


    // Fonts are loaded lazily, so when we call text()
    // for the first time, there is significant lag.
    // This prevents it from happening during gameplay.
    text("", 0, 0)
    // This should be called last, since the game
    // clock is officially ticking at this point
    updateTimer.init()
    noLoop() // stop draw() from the beginning
  }


  def updateState(): Unit = {
    if (updateTimer.timeForNextFrame()) {
      gameLogic.step()
      updateTimer.advanceFrame()
    }
  }

}


object SnakeGame {
  val FramesPerSecond: Int = 5 // -> this can change snake's speed
  val WidthCellInPixels: Int = 15 // -> pixel size
  val HeightCellInPixels: Int = WidthCellInPixels

  def main(args: Array[String]): Unit = {
    //    val solver = new Solver
    //    val ui = new StartUI()
    //    ui.visible = true
    //    startFlag = true

    var ui = new UI
    ui.main(args)
    println("Two player? " + ui.twoPlayerMode)
    // This is needed for Processing, using the name
    // of the class in a string is not very beautiful...
//            PApplet.main("snake.game.FxUI")
//    PApplet.main("snake.game.SnakeGame")
  }

}


