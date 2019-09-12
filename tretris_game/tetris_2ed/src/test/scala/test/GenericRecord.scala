// DO NOT MODIFY FOR BASIC SUBMISSION

package test

import java.io.{File, PrintWriter}

import engine.random.RandomGenerator
import test.util.{StringUtils, TestRandomGen}
import test.util.StringUtils._
import org.scalatest.{FunSuiteLike, Matchers}
import test.util.CryptoUtils._

/** Generic test infrastructure for Snake and Tetris.
  *
  * This supports a game with a grid, filled with some GridType
  * on which some actions can be done.
  *
  * An initial game can be constructed out of some InitialInfo
  * For testing we perform the actions and check is the grid is the same
  * as specified in the test.
  */
trait GridTypeInterface[T] {
  def conforms(rhs: T): Boolean

  def toChar: Char
}

trait GameLogicInterface[GameAction, GridType] {
  def nrRows: Int

  def nrColumns: Int

  def performAction(action: GameAction): Unit

  def getGridTypeAt(col: Int, row: Int): GridType

  def isGameOver: Boolean
}

abstract class GenericRecord[
  GameAction,
  GridType <: GridTypeInterface[GridType],
  GameLogic <: GameLogicInterface[GameAction, GridType],
  InitialInfo]() {

  def charToGridType(ch: Char): GridType

  def makeGame(r: RandomGenerator, info: InitialInfo): GameLogic

  def gameLogicName: String

  sealed abstract class GameDisplay {
    def conforms(other: GameDisplay): Boolean
  }

  case class GridDisplay(grid: Seq[Seq[GridType]]) extends GameDisplay {

    val nrRows: Int = grid.length
    val nrColumns: Int = grid.head.length

    override def toString: String =
      grid.map(_.map(_.toChar).mkString).mkString("\n")

    def gridConforms(otherGrid: Seq[Seq[GridType]]): Boolean = {

      def zippedCells: Seq[(GridType, GridType)] =
        for ((rowL, rowR) <- grid zip otherGrid; p <- rowL zip rowR) yield p

      def sameContent: Boolean =
        zippedCells.forall(pair => pair._1.conforms(pair._2))

      sameContent
    }

    def sameDimensions(rhs: GridDisplay): Boolean =
      nrRows == rhs.nrRows && nrColumns == rhs.nrColumns


    override def conforms(other: GameDisplay): Boolean =
      other match {
        case GameOverDisplay() => false
        case g: GridDisplay => sameDimensions(g) && gridConforms(g.grid)
    }

  }

  case class GameOverDisplay() extends GameDisplay {
    override def conforms(other: GameDisplay): Boolean = other match {
      case GameOverDisplay() => true
      case _ => false
    }
  }

  case class FrameInput(randomNumber: Int, actions: Seq[GameAction])


  case class TestFrame(input: FrameInput, display: GameDisplay) {

    override def toString: String = {
      val actionsString =
        if (input.actions.isEmpty) ""
        else ", " + input.actions.toString
      "TestFrame(" + input.randomNumber + actionsString + ",\n" +
        asIndentendMultilineString(display.toString, 6)
    }

  }

  def gridString(s: String): GridDisplay = {
    GridDisplay(
      for (row <- s.stripMargin.lines.to[collection.immutable.Seq])
        yield for (char <- row.toList)
          yield charToGridType(char)
    )
  }

  def getDisplay(logic: GameLogic): GameDisplay = {
    def getGridDisplay(logic: GameLogic): GridDisplay = {
      GridDisplay(
        for (row <- 0 until logic.nrRows)
          yield for (col <- 0 until logic.nrColumns)
            yield logic.getGridTypeAt(col, row)
      )
    }

    if (logic.isGameOver) GameOverDisplay()
    else getGridDisplay(logic)
  }


  object TestFrame {

    def apply(rand: Int,
              actions: Seq[GameAction],
              grid: String): TestFrame = {
      TestFrame(FrameInput(rand, actions), gridString(grid))
    }

    def apply(rand: Int, grid: String): TestFrame = {
      apply(rand, List(), grid)
    }

    def apply(rand: Int,
              actions: Seq[GameAction],
              display: GameDisplay): TestFrame = {
      TestFrame(FrameInput(rand, actions), display)
    }

    def apply(rand: Int, display: GameDisplay): TestFrame = {
      TestFrame(FrameInput(rand, List()), display)
    }

  }

  def performActionsAndGetDisplay(random: TestRandomGen,
                                  logic: GameLogic,
                                  frameInput: FrameInput): GameDisplay = {
    random.curNumber = frameInput.randomNumber
    frameInput.actions.foreach(logic.performAction)
    getDisplay(logic)
  }

  def checkInterleave(testA: Test, testB: Test): Boolean = {
    val randomA = new TestRandomGen(testA.frames.head.input.randomNumber)
    val randomB = new TestRandomGen(testB.frames.head.input.randomNumber)

    val logicA = makeGame(randomA, testA.initialInfo)
    val logicB = makeGame(randomB, testB.initialInfo)

    if (!getDisplay(logicA).conforms(testA.referenceDisplays.head)
    ||  !getDisplay(logicB).conforms(testB.referenceDisplays.head))
      return false

    for ((a, b) <- testA.frames.tail.zip(testB.frames.tail)) {
      val successA =
        performActionsAndGetDisplay(randomA, logicA, a.input).conforms(a.display)
      val successB =
        performActionsAndGetDisplay(randomB, logicB, b.input).conforms(b.display)

      if (!successA || !successB) return false
    } ; true
  }

  case class Test(name: String, initialInfo: InitialInfo, frames: Seq[TestFrame]) {
    lazy val referenceDisplays: Seq[GameDisplay] = frames.map(_.display)

    lazy val implementationDisplays: Seq[GameDisplay] = {
      val random = new TestRandomGen(frames.head.input.randomNumber)
      val logic = makeGame(random, initialInfo)

      getDisplay(logic) +:
        frames.tail.map(frame =>
          performActionsAndGetDisplay(random, logic, frame.input))
    }

    lazy val passes: Boolean =
      checkSameRecording(referenceDisplays, implementationDisplays)
  }

  def checkSameRecording(testRecord: Seq[GameDisplay],
                         actualRecord: Seq[GameDisplay]): Boolean = {
    testRecord.zip(actualRecord).forall(p => p._1.conforms(p._2))
  }

  class TestSuite extends FunSuiteLike with Matchers {

    type GradedTest = (Test, Double)
    type InterTest = (String, Test, Test)
    type GradedInterTest = (String, Test, Test,Double)

    val InterleaveFailMsg =
      s"""
         |Assuming you passed the non-interleaved version of each test:
         |
         |You likely have some global state. Running two instances of $gameLogicName
         |and alternately doing steps between them results in some interference.
         |Running your game with a single  $gameLogicName instance works, but when
         |we have two  $gameLogicName instances running in 'parallel', the test fails.
    """


    val PassStr = "[√] Pass"
    val FailStr = "[X] Fail"

    val DisplayPadWidth = 3
    val DisplayPadString: String = " " * DisplayPadWidth

    val FunctionalityPoints = 4
    val CodeStylePoints = 5

    def reportOnUniformlyScoreTests(testList: List[Test] = Nil, mainInterTestList: List[InterTest] = Nil, suiteName : String) : Unit = {
      val ((nrPassed,nrTests),(pts, maxPts)) = runUnformilyScoredTestsAndGetGrade(testList, mainInterTestList)
      writePoints(nrPassed,nrTests,pts, maxPts, suiteName)
    }

    def runUnformilyScoredTestsAndGetGrade(tests: List[Test] = Nil, interTests: List[InterTest] = Nil): ((Int,Int), (Double, Double)) = {
      val nrTests = tests.length + interTests.length
      val scorePerTest :Double = FunctionalityPoints.toDouble / nrTests.toDouble
      val testsWithScore : List[GradedTest] =
        tests.map((_,scorePerTest))
      val interTestsWithScore : List[GradedInterTest] =
        interTests.map(p => (p._1, p._2, p._3,scorePerTest))
      runTestsAndGetGrade(testsWithScore, interTestsWithScore)
    }


    def runTestsAndGetGrade(gts: List[GradedTest] = Nil, gits: List[GradedInterTest] = Nil): ((Int,Int), (Double, Double)) = {
      val ranTests : List[(Boolean,Double)] = gts.map(handleTest)  ++ gits.map(handleInterleaveTests)
      val nrPassed : Int = ranTests.count(_._1)
      val pts = ranTests.map(_._2).sum
      val maxPts = gts.map(_._2).sum +  gits.map(_._4).sum
      ((nrPassed,ranTests.length), (pts,maxPts))
    }

    def handleTest(t: GradedTest): (Boolean,Double) = {
      val (theTest, points) = t
      val passes = theTest.passes

      lazy val handleAssertion: Boolean = {
        def actionsString(actions: Seq[GameAction]): String =
          "<" ++ actions.map(_.toString).mkString(", ") ++ ">"

        def printTraceFrame(frame: TestFrame, actual: GameDisplay, index: Int): Unit = {
          println(s"step=$index, rand=${frame.input.randomNumber}, actions=${actionsString(frame.input.actions)}")

          val frameIsCorrect = frame.display.conforms(actual)
          val frameString =
            if (frameIsCorrect) withHeader("Want & Got", frame.display.toString)
            else twoColumnTable("Want", "Got", frame.display.toString, actual.toString)
          println(frameString)
          println()

        }

        val ptsStr = if (passes) f"+$points%.2f Points" else "No Points"
        val headerString = s"${theTest.name} : ${if (passes) PassStr else FailStr} : $ptsStr"
        println(List.fill(headerString.length)("=").mkString + "\n" + headerString)

        if (!passes) {
          println("This is what went wrong:\n")
        } else {
          println("This is what we got & expected:\n")
        }

        (theTest.frames, theTest.implementationDisplays, theTest.frames.indices)
          .zipped.map(printTraceFrame)

        passes
      }

      test(theTest.name)(assert(handleAssertion))
      val score= if (passes) points else 0
      (passes,score)
    }

    def handleInterleaveTests(t: GradedInterTest): (Boolean, Double) = {
      val (name, testA, testB, points) = t
      val passes = checkInterleave(testA, testB)

      lazy val handleAssertion: Boolean = {
        val message = s"Interleave Test: ${testA.name}, ${testB.name} : " +
          s"${if (!passes) FailStr+" : No Points\n"+InterleaveFailMsg.stripMargin
          else PassStr+f" : +$points%.2f Points"}"
        println("="*StringUtils.widthOfMultilineString(message)+"\n"+message)
        passes
      }

      test(name)(assert(handleAssertion))
      val score= if (passes) points else 0
      (passes,score)
    }

    def writePoints(nrPassedTests: Int, totalNrTests: Int, points: Double, maxPoints: Double, suiteName: String = "x.x"): Unit = {

      def writeGradeFile(): Unit =
        try {
          val filename = s"grade_${suiteName.replace('.', '_')}.tmp"
          val pointStr = s"grade $points"
          val sig = sign(pointStr)

          assert(verify(pointStr, sig))
          using(new PrintWriter(new File(filename))) {
            file => file.println(sig + "\n" + pointStr)
          }
        } catch {
          case _: Any => ()
        }

      val percentage = (points/maxPoints)*100

      val resultStr = f"Total Functionality Points : $points%.2f/$maxPoints%.2f [$percentage%.2f"+"%]"
      println(f"Passed $nrPassedTests%d/$totalNrTests%d tests")
      println(s"${"="*resultStr.length}\n$resultStr\n${"="*resultStr.length}")

      val initialCodeStyleGrade = (points/maxPoints) * CodeStylePoints
      println(f"(Initial code style points: $initialCodeStyleGrade%.2f)")

      writeGradeFile()
    }

  }

}
