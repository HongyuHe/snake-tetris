// DO NOT MODIFY FOR BASIC SUBMISSION

package test.tetris

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import test.tetris.TetrisRecord._
import test.tetris.TetrisTests._

@RunWith(classOf[JUnitRunner])
class TetrisTestSuite extends TestSuite {

  val testList: List[Test] = List(
    testPlacementI,
    testPlacementIEven,
    testPlacementJ,
    testPlacementL,
    testPlacementOEven,
    testPlacementO,
    testPlacementS,
    testPlacementT,
    testPlacementZ,
    testIRotations,
    testJRotations,
    testLRotations,
    testORotations,
    testSRotations,
    testTRotations,
    testZRotations,
    testRotate360,
    testRotate360CC,
    testRotateLeftRight,
    testRotateRightLeft,
    testMovement,
    testMovement2,
    testMoveOutLeft,
    testMoveOutRight,
    testBlockedByBlocksLeft,
    testBlockedByBlocksRight,
    testBlockedByBlocksRotateLeft,
    testBlockedByBlocksRotateRight,
    testSpawn,
    testGameOver,
    testNoEscapeGameOver,
    testNoEscapeGameOver2,
    testDrop,
    testClear1Line,
    testClear1LineMiddle,
    testClear2Lines,
    testClear3Lines,
    testClear4Lines,
    testGame5x9,
    testGame8x11,
    testGame6x10
  )

  val mainInterTestList: List[InterTest] = List(
    ("testInterleave5x9and8x11", testGame5x9, testGame8x11),
    ("testInterleave8x11and6x10", testGame8x11, testGame6x10),
  )

  reportOnUniformlyScoreTests(testList, mainInterTestList, "2.0")
}