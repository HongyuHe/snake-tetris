package snake.components

import java.awt.Font

import scala.swing.{Dimension, _}
import scala.swing.event._

//class Solver {
//  private val allWords = scala.io.Source.fromFile("/home/hongyu/Desktop/University/second-year/AdvancedProgramming/assignments_code/snake_game/snake_bonus/src/main/scala/snake/components/word.txt").getLines().toSet
//  private val scrabble =
//    (scala.io.Source.fromFile("/home/hongyu/Desktop/University/second-year/AdvancedProgramming/assignments_code/snake_game/snake_bonus/src/main/scala/snake/components/word.txt").getLines().toSet
//      map ((s: String) => s.toLowerCase))
//
//  private def matches(pattern: String, word: String): Boolean = {
//    if (word.length != pattern.length)
//      return false
//    for (i <- 0 until word.length) {
//      val p = pattern(i)
//      if (p != '?' && p != word(i))
//        return false
//    }
//    true
//  }
//
//  def findWords(pattern: String, dictNo: Int): List[String] = {
//    var w = List[String]()
//    val words = if (dictNo == 1) allWords else scrabble
//    for (e <- words) {
//      if (matches(pattern, e))
//        w = e :: w
//    }
//    w
//  }
//}

class SwUI() extends MainFrame {
  private def restrictHeight(s: Component): Unit = {
    s.maximumSize = new Dimension(Short.MaxValue, s.preferredSize.height)
  }

  title = "Snake Game - Hongyu"
//    preferredSize = new Dimension(500, 300)

  val levelField = new TextField()
  val levelLabel: Label = new Label("Set difficulty level:") {
    //Set the position of its text, relative to its icon:
    //    verticalTextPosition = Alignment.Bottom
    //    horizontalTextPosition = Alignment.Left
  }
  val level1: RadioButton = new RadioButton("level 1") {
    mnemonic = Key.Key1
    selected = false
  }
  val level2: RadioButton = new RadioButton("level 2") {
    mnemonic = Key.Key2
    selected = false
  }
  val level3: RadioButton = new RadioButton("level 3") {
    mnemonic = Key.Key3
    selected = false
  }
  val levelBottons: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    contents += level1
    contents += level2
    contents += level3
  }
  //  val twoPlayerLabel = new Label("Tow players")
  val twoPlayerBotton: RadioButton = new RadioButton("Two player") {
    mnemonic = Key.C // .VK_C
    selected = false
  }
  val startButton: Button = new Button("Start") {
        verticalTextPosition = Alignment.Bottom
        horizontalTextPosition = Alignment.Center
    mnemonic = event.Key.Enter
    tooltip = "Start a brand new game!"
  }

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += levelLabel
      contents += Swing.VStrut(10)
      contents += level1
      contents += level2
      contents += level3
    }
//    contents += Swing.VStrut(20)
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += twoPlayerBotton
      contents += Swing.HStrut(50)
    }
    contents += startButton
//    override def preferredSize: Dimension = super.preferredSize

    //    contents += new BoxPanel(Orientation.Horizontal) {
    //      contents += startButton
    //    }
  }

  listenTo(twoPlayerBotton)
  listenTo(startButton)

  reactions += {
    case ButtonClicked(`twoPlayerBotton`) => println("Two player!")
    case ButtonClicked(`startButton`) => println("Start!!!")
  }

  //  val searchField = new TextField { columns = 32 }
  //  val searchButton = new Button("Search")
  //  val searchLine = new BoxPanel(Orientation.Horizontal) {
  //    contents += searchField
  //    contents += Swing.HStrut(20)
  //    contents += searchButton
  //  }
  //  val resultField = new TextArea {
  //    rows = 10
  //    lineWrap = true
  //    wordWrap = true
  //    editable = false
  //  }
  //  val dict = new ComboBox(List("Scrabble words", "All words"))
  //  val largerFont = Button("+") { fontChange(+1) }
  //  val smallerFont = Button("-") { fontChange(-1) }
  //  var fontSize = 12
  //  fontChange(0)
  //
  //  // make sure that resizing only changes the resultField:
  //  restrictHeight(searchLine)
  //
  //  contents = new BoxPanel(Orientation.Vertical) {
  //    contents += searchLine
  //    contents += Swing.VStrut(10)
  //    contents += new ScrollPane(resultField)
  //    contents += Swing.VStrut(10)
  //    contents += new BoxPanel(Orientation.Horizontal) {
  //      contents += dict
  //      contents += Swing.HStrut(10)
  //      contents += largerFont
  //      contents += Swing.HStrut(10)
  //      contents += smallerFont
  //    }
  //    border = Swing.EmptyBorder(10, 10, 10, 10)
  //  }
  //
  //  listenTo(searchField)
  //  listenTo(searchButton)
  //  reactions += {
  //    case EditDone(`searchField`) => searchNow()
  //    case ButtonClicked(`searchButton`) => searchNow()
  //  }
  //
  //  def fontChange(d: Int) {
  //    fontSize += d
  //    resultField.font = new Font(null, Font.PLAIN, fontSize)
  //  }
  //
  //  def searchNow() {
  //    val pattern = searchField.text.toLowerCase
  //    val words = solver.findWords(pattern, dict.selection.index)
  //    if (words.length == 0) {
  //      resultField.text = "\n\nSorry, no words found."
  //    } else {
  //      resultField.text = words.sorted mkString "\n"
  //      resultField.caret.position = 0
  //    }
  //  }
}

//object Crossword {
//  def main(args: Array[String]) {
//    val solver = new Solver
//    val ui = new StartUI(solver)
//    ui.visible = true
//  }
//}
