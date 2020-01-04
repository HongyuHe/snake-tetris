// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package test.snake

import org.scalatestplus.junit.JUnitRunner
import test.snake.SnakeRecord._
import test.snake.SnakeTests._
import org.junit.runner.RunWith


abstract class SnakeTestSuite extends TestSuite {

  val mainTestList: List[Test] = List(
    testStartCorrectly,
    testApplePos3,
    testApplePos8,
    testNoRoomForApple,
    testMove,
    testIgnoreEatHead,
    testIgnoreEatHeadQuickSwitch,
    testIgnoreEatHeadQuickSwitch2,
    testChangeDirs,
    testWrapAroundEast,
    testWrapAroundWest,
    testWrapAroundNorth,
    testWrapAroundSouth,
    testGrow,
    testGrowTwice,
    testPreciselyDoesNotDie,
    testGameOver,
    testNoEscapeGameOver,
    testGame3x1,
    testGame6x6,
    testGame6x3,
    testGame10x7,
  )

  val mainInterTestList: List[InterTest] = List(
    ("testInterleave6x3and10x7", testGame6x3, testGame10x7),
    ("testInterleave6x6and10x7", testGame6x6, testGame10x7),
  )



}

@RunWith(classOf[JUnitRunner])
class SnakeTestsAssignment1_1 extends SnakeTestSuite {

  val testList: List[Test] = mainTestList :+ testNoReverseMode
  reportOnUniformlyScoredTests(testList, mainInterTestList, "1.1")
}

@RunWith(classOf[JUnitRunner])
class SnakeTestsAssignment1_3 extends SnakeTestSuite {
  val reverseTests: List[Test] = List(
    testReverseSimple,
    testReversePastStart,
    testReverseGameOver,
    testChangeThePast,
    testReverseLong,
  )

  val testList: List[Test] = mainTestList ++ reverseTests

  reportOnUniformlyScoredTests(testList, mainInterTestList, "1.3")
}