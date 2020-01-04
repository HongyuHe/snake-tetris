// DO NOT MODIFY FOR BASIC SUBMISSION

package engine

import engine.graphics.Color.Black
import engine.graphics.{Color, Point, Rectangle, Triangle}
import processing.core.{PApplet, PConstants}

class GameBase   extends PApplet {

  // inner class: can call current time of outer class
  class UpdateTimer(val framesPerSecond: Float) {

    val frameDuration: Float = 1000 / framesPerSecond // ms
    var nextFrame: Float = Float.MaxValue

    def init(): Unit = nextFrame = currentTime() + frameDuration
    def timeForNextFrame(): Boolean = currentTime() >= nextFrame
    def advanceFrame(): Unit = nextFrame = nextFrame + frameDuration

  }

  // ===Processing Wrappers & Abstractions===

  /** An alias for the obscurely named function millis()
    *
    * @return Current time in milliseconds since stating the program.
    */
  def currentTime(): Int = millis()

  def drawTextCentered(string: String, size: Float, center: Point): Unit = {
    val (x, y) = (center.x, center.y-(size/2))
    textAlign(PConstants.CENTER, PConstants.CENTER)
    textSize(size)
    drawText(string, Point(x, y))
  }

  def drawText(string: String, pos: Point, withShadow: Boolean = true): Unit = {
    if (withShadow) drawTextShadow(string, pos)
    text(string, pos.x, pos.y)
  }

  /** Quick hack for legibility on different backgrounds */
  def drawTextShadow(string: String, pos: Point, color: Color = Black, thickness: Float = 1): Unit = {
    pushStyle()
    setFillColor(color)
    List((1,0),(-1,0),(0,1),(0,-1)).foreach(t => {
      text(string, pos.x+(t._1*thickness), pos.y+t._2*thickness)
    })
    popStyle()
  }

  def drawRectangle(r: Rectangle): Unit =
    rect(r.left,r.top, r.width, r.height)

  def drawTriangle(t: Triangle): Unit =
    triangle(t.p1.x, t.p1.y, t.p2.x,t.p2.y, t.p3.x,t.p3.y)

  def drawEllipse(r: Rectangle): Unit =
    ellipse(r.center.x, r.center.y, r.width, r.height)

  def setFillColor(c: Color): Unit =
    fill(c.red, c.green, c.blue, c.alpha)

  def setBackground(c: Color): Unit =
    background(c.red, c.green, c.blue, c.alpha)

}
