package tetris.logic

import engine.random.RandomGenerator
import tetris.logic.GameController._
import tetris.game._

class GameController(val randomGen: RandomGenerator,
                     val nrColumns: Int,
                     val nrRows: Int,
                     var board: Seq[Seq[TetrisBlock]] ) {
  var isGameOver = false

  def invokeTetrominoAction(action: TetrominoAction): Unit = {
    saveCurrTetromino()
    action match {
      case HardDrop     =>  hardDropTetroAndDockBlocks()
      case MoveDown     =>  moveDownTetroAndCheckCrash()
      case MoveLeft     =>  currTetromino.center.x -= 1
      case MoveRight    =>  currTetromino.center.x += 1
      case RotateLeft   =>  currTetromino.setLeftRotateOrientation()
      case RotateRight  =>  currTetromino.setRightRotateOrientation()
    }
    updateFrame()
  }

  def init(): Unit = {
    for (row <- 0 until nrRows; col <- 0 until nrColumns) {
      if (board(row)(col) != Empty)
        dockedBlocks = dockedBlocks + Tuple2(Coordinates(row, col), board(row)(col))
      boardBottomLine += Coordinates(nrRows, col)
    }
    updateFrame()
  }


  private[this] type Blocks = Set[BlockPosition]
  private[this] type BlockPosition = (Coordinates, TetrisBlock)

  private[this] var prevTetromino = Tetromino()
  private[this] var currTetromino: Tetromino = spawnTetromino
  private[this] var prevBoard = board.transpose.transpose

  private[this] var dockedBlocks: Blocks = Set()
  private[this] var boardBottomLine: Set[Coordinates] = Set()

  private[this] def checkGameOver(): Unit = if (isCrashedIntoDockedTails) isGameOver = true

  private[this] def saveCurrBoard():     Unit = prevBoard = board.transpose.transpose
  private[this] def saveCurrTetromino(): Unit = prevTetromino = Tetromino makeDeepCopy currTetromino

  private[this] def retrievePrevBoard():     Unit = board = prevBoard
  private[this] def retrievePrevTetromino(): Unit = currTetromino = Tetromino makeDeepCopy prevTetromino

  private[this] def spawnTetroCenter: Coordinates = Coordinates(1, (nrColumns - 1) / 2)
  private[this] def spawnTetromino: Tetromino =
    Tetromino(spawnTetroCenter, BlockTypes(randomGen.randomInt(BlockTypes.length)))

  private[this] def drawBlocks(blocks: Blocks): Unit = blocks.foreach { tile => updateBoard(tile._1, tile._2)}
  private[this] def drawBlocks(blocks: Blocks, tetrisBlock: TetrisBlock): Unit =
    blocks.foreach { tile => updateBoard(tile._1, tetrisBlock)}

  private[this] def drawTetromino(block: Tetromino, tetrisBlock: TetrisBlock): Unit =
    block.getAllBlocksOfTetromino.foreach { tile => updateBoard(tile, tetrisBlock) }

  private[this] def updateBoard (coordinates: Coordinates, tetrisBlock: TetrisBlock): Unit =
    board = board.updated(coordinates.y, board(coordinates.y).updated(coordinates.x, tetrisBlock))

  private[this] def updateFrame(): Unit = {
    if (isGameOver) return
    saveCurrBoard()
    drawTetromino(prevTetromino, Empty)
    clearLines()
    drawBlocks(dockedBlocks)
    validateAndDrawTetromino()
  }

  private[this] def dockAndSpawnTetromino(): Unit = {
    dockedBlocks ++= currTetromino.getAllBlocksOfTetromino.map(tile => (tile, currTetromino.blockType))
    currTetromino = spawnTetromino
  }

  private[this] def moveDownTetroAndCheckCrash(): Unit = {
    currTetromino.center.y += 1
    if (isCrashedIntoDockedTails) {
      retrievePrevTetromino()
      dockAndSpawnTetromino()
      checkGameOver()
    }
  }

  private[this] def hardDropTetroAndDockBlocks(): Unit = {
    while (!isCrashedIntoDockedTails) { currTetromino.center.y += 1 }
    currTetromino.center.y -= 1
    dockAndSpawnTetromino()
    checkGameOver()
  }

  private[this] def clearLines(): Unit = {
    var fullRows = List[Int]()
    def isRollFull: Int => Boolean = row => dockedBlocks.count(_._1.y == row) == nrColumns
    def deleteFullRow: Int => Unit = row => dockedBlocks = dockedBlocks.filter(_._1.y != row)
    drawBlocks(dockedBlocks, Empty)

    for (row <- 0 until nrRows) if (isRollFull(row)) { fullRows :+= row; deleteFullRow(row) }
    dockedBlocks.foreach { tile => fullRows.foreach  { fullRow =>
      var offsetFromDeletedRow = 0
      if (tile._1.y < fullRow) { offsetFromDeletedRow += 1 }
      tile._1.y += offsetFromDeletedRow   // pull the docked blocks that are above the deleted row downwards by #offset lines
    } }
  }

  private[this] def isBlockPositionValid(block: Tetromino): Boolean = {
    def isCoordinatesValid(coor: Coordinates): Boolean = {
      def isOutOfBoard:      Boolean = coor.x < 0 || coor.y < 0 || coor.x >= nrColumns || coor.y >= nrRows
      def touchExitingTiles: Boolean = !prevTetromino.getAllBlocksOfTetromino.contains(coor) && board(coor.y)(coor.x) != Empty
      !(isOutOfBoard || touchExitingTiles || isCrashedIntoDockedTails)
    }
    block.getAllBlocksOfTetromino.forall( isCoordinatesValid )
  }

  private[this] def validateAndDrawTetromino (): Unit =
    if (isBlockPositionValid(currTetromino)) { drawTetromino(currTetromino, currTetromino.blockType) }
    else { retrievePrevBoard(); retrievePrevTetromino() }

  private[this] def isCrashedIntoDockedTails: Boolean =
    currTetromino.getAllBlocksOfTetromino.exists( tile =>
      boardBottomLine.contains(tile) || dockedBlocks.exists( dockedBlock => tile == dockedBlock._1) )
}

object GameController {
  val BlockTypes: Seq[TetrisBlock] = Seq(IBlock, JBlock, LBlock, OBlock, SBlock, TBlock, ZBlock)

  def apply(randomGen: RandomGenerator, nrColumns: Int, nrRows: Int, Board: Seq[Seq[TetrisBlock]]): GameController =
    new GameController(randomGen, nrColumns, nrRows, Board) { init() }
}