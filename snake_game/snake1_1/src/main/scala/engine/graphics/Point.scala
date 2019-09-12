// DO NOT MODIFY FOR BASIC SUBMISSION

package engine.graphics

case class Point(x: Float, y: Float) {
  def +(rhs: Point) = Point(x + rhs.x, y + rhs.y)
}

object Point {
  val Origin = Point(0,0)
}
