// scalastyle:off

package snake.game

import java.awt.event
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

  var gameLogic = SnakeLogic(gameSetting)
  val updateTimer = new UpdateTimer(framesPerSecond)
  var startTime: Int = 0
  var timeOut: Boolean = false

  lazy val bgImage: PImage = loadImage("src/resources/snake_bg_small.jpg")
  lazy val appleImage: PImage = loadImage("src/resources/apple.png")
  lazy val bombImage: PImage = loadImage("src/resources/bomb.png")
  lazy val wallImage: PImage = loadImage("src/resources/wall.jpg")

  lazy val eastHeadImage: PImage = loadImage("src/resources/snake_head_east.png")
  lazy val northHeadImage: PImage = loadImage("src/resources/snake_head_north.png")
  lazy val southHeadImage: PImage = loadImage("src/resources/snake_head_south.png")
  lazy val westHeadImage: PImage = loadImage("src/resources/snake_head_west.png")

  val widthInPixels: Int = WidthCellInPixels * gameLogic.nrColumns
  val heightInPixels: Int = HeightCellInPixels * (gameLogic.nrRows + TimerSpaceHieghtInPixels)
  val screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels, heightInPixels)

  override def draw(): Unit = {
    val isTimeOut = (currentTime() - startTime) / 1000 >= timeLimitInSecond
    if (isTimeOut && !gameLogic.isGameOver) {
      timeOut = true
      gameLogic.terminateGame()
    }
    loadPixels()
    updateState()
    drawGrid()
    if (!gameLogic.isGameOver) drawTimer()
    if (gameLogic.isGameOver || timeOut) drawGameOverScreen()

//    println("Timer: " + (TimeLimitInSecond - (currentTime()-startTime)/1000))
  }

  def drawGameOverScreen(): Unit = {
    def drawFog(): Unit = {
      bgImage.loadPixels()
      for(x <- Range(0, width); y <- Range(0, height)) {
        val loc = x + y * width
        val r = red(bgImage.pixels(loc))
        val g = green(bgImage.pixels(loc))
        val b = blue(bgImage.pixels(loc))
        val distance = PApplet.dist(width/2, height/2, x, y)
        val factor = PApplet.map(distance, 0, 200, 2, 0)
        pixels(loc) = color(r*factor, g*factor, b*factor)
      }
      updatePixels()
    }

    val result = gameLogic.getGameResult
    val loserOrWinner = (if (timeOut) result.winner else gameLogic.getloser) match {
      case HostSnake()  => "Green  snake"
      case RivalSnake() => "Blue  snake"
      case AiSnake()    => "AI"
    }

    setFillColor(Red)
    val finalMsg = (gameSetting.twoPlayerMode, timeOut) match {
      case (true, true) => s"$loserOrWinner Win !\n[ Winner Score: ${result.score} ]"
      case (false, true) => s"Score: ${result.score} !!!"
      case (true, false) => s"$loserOrWinner Die !\n[ Winner Score: ${result.score} ]"
      case (false, false) =>s"Game Over / Score: ${result.score} !!!"
    }
    drawTextCentered(finalMsg, 50, screenArea.center)

    drawFog()
//    noLoop()
  }

  def drawSnakeHead(direction: Direction, area: Rectangle): Unit = {
    val headImage: PImage = direction match {
      case West()  => westHeadImage
      case North() => northHeadImage
      case East()  => eastHeadImage
      case South() => southHeadImage
    }
    imageMode(3)
    image(headImage, area.centerX, area.centerY, area.width*1.3f, area.height*1.3f)
  }
  def drawSnakeBody(r: Rectangle, dis: Float): Unit = {
    if (dis == 1) {
      quad(r.centerLeft.x,r.centerLeft.y, r.centerUp.x, r.centerUp.y,
        r.centerRight.x, r.centerRight.y, r.centerDown.x, r.centerDown.y)
      return
    }
    val factor = 0.5f
    ellipse(r.leftUp.x, r.leftUp.y, r.width*factor, r.height*factor)
    ellipse(r.leftDown.x, r.leftDown.y, r.width*factor, r.height*factor)
    ellipse(r.rightUp.x, r.rightUp.y, r.width*factor, r.height*factor)
    ellipse(r.rightDown.x, r.rightDown.y, r.width*factor, r.height*factor)
    ellipse(r.centerUp.x, r.centerUp.y, r.width*factor, r.height*factor)
    ellipse(r.centerDown.x, r.centerDown.y, r.width*factor, r.height*factor)
    ellipse(r.centerLeft.x, r.centerLeft.y, r.width*factor, r.height*factor)
    ellipse(r.centerRight.x, r.centerRight.y, r.width*factor, r.height*factor)
    ellipse(r.centerX, r.centerY, r.width, r.height)
  }

  def drawTimer(): Unit = {
    val timerArea: Rectangle = Rectangle(Point(0, widthInPixels), widthInPixels, heightInPixels-widthInPixels)

    if (timeOut) return
    setFillColor(Black)
    drawRectangle(timerArea)
    setFillColor(LightYellow)
    textSize(40)
    text(s"Timer: ${timeLimitInSecond - (currentTime()-startTime)/1000}", 275, 815)
  }

  def drawGrid(): Unit = {
    val widthPerCell = screenArea.width / gameLogic.nrColumns
    val heightPerCell = screenArea.height / (gameLogic.nrRows + TimerSpaceHieghtInPixels)

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
        case SnakeHead(id, direction) if id == HostSnake() =>
          tint(255, 255)
          drawSnakeHead(direction, area)
//          imageMode(3)
//          image(eastHeadImage, area.centerX, area.centerY, area.width*1.5f, area.height*1.5f)

          setFillColor(Color.LawnGreen)
//          drawTriangle(getTriangleForDirection(direction, area))
        case SnakeBody(id, p) if id == HostSnake() =>
          val color = Color.LawnGreen.interpolate(p, Color.DarkGreen)
          setFillColor(color)
          drawSnakeBody(area, p)

        case SnakeHead(id, direction) if id == RivalSnake() =>
          tint(0, 200, 255)
          drawSnakeHead(direction, area)
        case SnakeBody(id, p) if id == RivalSnake() =>
          val color = Color.SkyBlue.interpolate(p, Color.DarkBlue)
          setFillColor(color)
//          drawRectangle(area)
          drawSnakeBody(area, p)

        case SnakeHead(id, direction) if id == AiSnake() =>
          setFillColor(Color.SkyBlue)
          drawTriangle(getTriangleForDirection(direction, area))
        case SnakeBody(id, p) if id == AiSnake() =>
          val color = Color.SkyBlue.interpolate(p, Color.DarkBlue)
          setFillColor(color)
          drawRectangle(area)

        case Apple() =>
          imageMode(3)
          tint(255, 255)
          image(appleImage, area.centerX, area.centerY, area.width*1.2f, area.height*1.2f)
        case Wall() =>
          tint(255, 255)
          imageMode(3)
          image(wallImage, area.centerX, area.centerY, area.width*1.2f, area.height*1.2f)
        case Bomb() =>
          tint(255, 255)
          imageMode(3)
          image(bombImage, area.centerX, area.centerY, area.width*1.2f, area.height*1.2f)
        case Empty() => ()
      }
    }

    background(bgImage)

    for (y <- 0 until gameLogic.nrRows;
         x <- 0 until gameLogic.nrColumns) {
      drawCell(getCell(x, y), gameLogic.getGridTypeAt(x, y))
    }
  }

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
    startTime = currentTime()
//    noLoop() // stop draw() from the beginning
  }


  def updateState(): Unit = {
    if (updateTimer.timeForNextFrame()) {
      gameLogic.step()
      updateTimer.advanceFrame()
    }
  }
}


object SnakeGame {
//  25*30 × (25+4)*30 => 750 × 850
  val WidthCellInPixels: Int = 25 // -> pixel size
  val HeightCellInPixels: Int = WidthCellInPixels
  var timeLimitInSecond: Int = 16
  val TimerSpaceHieghtInPixels: Int = 4
  var framesPerSecond: Int = 5 // -> this can change snake's speed
  var gameSetting = GameSetting()

  def main(args: Array[String]): Unit = {

    val menu = new MenuPage
    menu.main(args)
    println("Level: " + menu.gameLevel)
    println("Sake speed: " + menu.gameSpeed)
    println("Two player? " + menu.twoPlayerMode)
    println("AI? " + menu.battleWithAI)

    timeLimitInSecond = menu.timer
    framesPerSecond   = menu.gameSpeed
    gameSetting = GameSetting(menu.gameLevel,
                              menu.nrBombs,
                              menu.nrApples,
                              menu.startFlag,
                              menu.battleWithAI,
                              menu.twoPlayerMode)

    if (menu.startFlag)
      PApplet.main("snake.game.SnakeGame")
  }
}


