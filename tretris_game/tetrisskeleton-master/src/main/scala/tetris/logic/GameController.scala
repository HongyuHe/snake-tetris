package tetris.logic

import engine.random.RandomGenerator
import tetris.logic.GameController._
import tetris.game._

class GameController(val randomGen: RandomGenerator,
                     val nrColumns: Int,
                     val nrRows: Int,
                     var board: Seq[Seq[TetrisBlock]] ) {
  var isGameOver = false

  def init(): Unit = {
    for (row <- 0 until nrRows; col <- 0 until nrColumns) {
      if (board(row)(col) != Empty) { dockedTiles = dockedTiles + Tuple2(Coordinates(row, col), board(row)(col)) }
      boardBottomLine += Coordinates(nrRows, col)
    };updateFrame()
  }

  def invokeBlockAction(action: BlockAction): Unit = {
    saveCurrentBlock()
    action match {
      case HardDrop     =>  hardDropBlock()
      case MoveDown     =>  currblock.center.y += 1
      case MoveLeft     =>  currblock.center.x -= 1
      case MoveRight    =>  currblock.center.x += 1
      case RotateLeft   =>  currblock.setLeftRotateShape()
      case RotateRight  =>  currblock.setRightRotateShape()
    }
    if (action == MoveDown && isCrashedIntoDockedTails) {
      retrievePreviousBlock()
      dockAndSpawnBlock()
      checkGameOver()
    };updateFrame()
  }


  private[this] var currblock: Block = spawnNewBlock

  private[this] var previousBlock = Block()
  private[this] var previousBoard = board.transpose.transpose

  private[this] var boardBottomLine: Set[Coordinates] = Set()
  private[this] var dockedTiles: Set[(Coordinates, TetrisBlock)] = Set()


  private[this] def saveCurrentBlock(): Unit = previousBlock = Block makeDeepCopy currblock
  private[this] def saveCurrentBoard(): Unit = previousBoard = board.transpose.transpose

  private[this] def retrievePreviousBoard(): Unit = board = previousBoard
  private[this] def retrievePreviousBlock(): Unit = currblock = Block makeDeepCopy previousBlock

  private[this] def spawnNewBlockCenter: Coordinates = Coordinates(1, (nrColumns - 1) / 2)
  private[this] def spawnNewBlock: Block = Block(spawnNewBlockCenter, BlockTypes(randomGen.randomInt(BlockTypes.length)))

  private[this] def drawBlock(block: Block, tetrisBlock: TetrisBlock): Unit = block.getAllTilesOfTheBlock.foreach { tile => updateBoard(tile, tetrisBlock) }
  private[this] def drawTiles(tiles: Set[(Coordinates, TetrisBlock)]): Unit = tiles.foreach { tile => updateBoard(tile._1, tile._2)}
  private[this] def drawTiles(tiles: Set[(Coordinates, TetrisBlock)], tetrisBlock: TetrisBlock): Unit = tiles.foreach { tile => updateBoard(tile._1, tetrisBlock)}

  private[this] def checkGameOver(): Unit = if (isCrashedIntoDockedTails) { isGameOver = true }
  private[this] def updateBoard (coordinates: Coordinates, tetrisBlock: TetrisBlock): Unit = board = board.updated(coordinates.y, board(coordinates.y).updated(coordinates.x, tetrisBlock))

  private[this] def updateFrame(): Unit = {
    if (isGameOver) return

    saveCurrentBoard()
    drawBlock(previousBlock, Empty)
    clearLines()
    drawTiles(dockedTiles)
    validateAndDrawCurrentBlock()
  }

  private[this] def dockAndSpawnBlock(): Unit = {
    dockedTiles ++= currblock.getAllTilesOfTheBlock.map(tile => (tile, currblock.blockType))
    currblock = spawnNewBlock
  }

  private[this] def hardDropBlock(): Unit = {
    while (!isCrashedIntoDockedTails) { currblock.center.y += 1 }
    currblock.center.y -= 1
    dockAndSpawnBlock()
    checkGameOver()
  }

  private[this] def clearLines(): Unit = {
    var fullRows = List[Int]()
    def isRollFull: Int => Boolean = row => dockedTiles.count(_._1.y == row) == nrColumns
    def deleteFullRow: Int => Unit = row => dockedTiles = dockedTiles.filter(_._1.y != row)
    drawTiles(dockedTiles, Empty)

    for (row <- 0 until nrRows) if (isRollFull(row)) { fullRows :+= row; deleteFullRow(row) }
    dockedTiles.foreach { tile => fullRows.foreach   { fullRow =>
      var offsetFromDeletedRow = 0
      if (tile._1.y < fullRow) { offsetFromDeletedRow += 1 }
      tile._1.y += offsetFromDeletedRow   // pull the docked tiles that are above the deleted row downwards by #offset lines
    } }
  }

  private[this] def validateAndDrawCurrentBlock (): Unit =
    if (isBlockPositionValid(currblock)) { drawBlock(currblock, currblock.blockType) }
    else { retrievePreviousBoard(); retrievePreviousBlock() }

  private[this] def isCrashedIntoDockedTails: Boolean =
    currblock.getAllTilesOfTheBlock.exists ( tile => boardBottomLine.contains(tile) ||
        dockedTiles.exists( dockedTile => tile == dockedTile._1) )

  private[this] def isBlockPositionValid(block: Block): Boolean = {
    def isCoordinatesValid(coor: Coordinates): Boolean = {
      def isOutOfBoard:      Boolean = coor.x < 0 || coor.y < 0 || coor.x >= nrColumns || coor.y >= nrRows
      def touchExitingTiles: Boolean = !previousBlock.getAllTilesOfTheBlock.contains(coor) && board(coor.y)(coor.x) != Empty
      !(isOutOfBoard || touchExitingTiles || isCrashedIntoDockedTails)
    }
    block.getAllTilesOfTheBlock.forall(isCoordinatesValid)
  }
}

object GameController {
  val BlockTypes: Seq[TetrisBlock] = Seq(IBlock, JBlock, LBlock, OBlock, SBlock, TBlock, ZBlock)

  def apply(randomGen: RandomGenerator, nrColumns: Int, nrRows: Int, Board: Seq[Seq[TetrisBlock]]): GameController =
    new GameController(randomGen, nrColumns, nrRows, Board) { init() }
}