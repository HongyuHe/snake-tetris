package tetris.logic

import engine.random.RandomGenerator
import tetris.game._
import tetris.logic.GameController._
import util.control.Breaks._
import scala.collection.mutable

class GameController(val randomGen: RandomGenerator,
                     val nrColumns: Int,
                     val nrRows: Int,
                     var board: Seq[Seq[TetrisBlock]]
                ) {
  var isGameOver = false

  def letBlock (action: BlockAction): Unit = {
    saveCurrentBlock()
    action match {
      case MoveRight    =>  block.center.x += 1
      case MoveLeft     =>  block.center.x -= 1
      case MoveDown     =>  block.center.y += 1
      case HardDrop     =>  hardDropBlock()
      case RotateLeft   =>  block.shapeLeftRotate()
      case RotateRight  =>  block.shapeRightRotate()
    }
    if (action == MoveDown && isCrashedIntoDockedTails) {
      retrievePreviousBlock()
      dockBlockAndNewBlock()
      checkGameOver()
    };drawBoard()
  }

  def init(): Unit = {
    for (row <- 0 until nrRows; col <- 0 until nrColumns) {
      if (board(row)(col) != Empty) { dockedTiles = dockedTiles + Tuple2(Coordinates(row, col), board(row)(col)) }
      baseLine += Coordinates(nrRows, col)
    };drawBoard()
  }

/* =================================================== Implementation ================================================================ */
  private[this] var block: Block = generateNewBlock

  private[this] var previousBlock = Block()
  private[this] var previousBoard = board.transpose.transpose

  private[this] var baseLine:    Set[Coordinates] = Set()
  private[this] var dockedTiles: Set[(Coordinates, TetrisBlock)] = Set()

  private[this] def saveCurrentBoard(): Unit = previousBoard = board.transpose.transpose
  private[this] def saveCurrentBlock(): Unit = previousBlock = Block makeDeepCopy block

  private[this] def retrievePreviousBlock(): Unit = block = Block makeDeepCopy previousBlock
  private[this] def retrievePreviousBoard(): Unit = board = previousBoard

  private[this] def drawBlock(block: Block, tetrisBlock: TetrisBlock): Unit = block.getTilesOfTheBlock.foreach { tile => updateBoard(tile, tetrisBlock) }
  private[this] def drawTiles(tiles: Set[(Coordinates, TetrisBlock)]): Unit = tiles.foreach { tile => updateBoard(tile._1, tile._2)}
  private[this] def drawTiles(tiles: Set[(Coordinates, TetrisBlock)], tetrisBlock: TetrisBlock): Unit = tiles.foreach { tile => updateBoard(tile._1, tetrisBlock)}

  private[this] def generateNewBlockCenter: Coordinates = Coordinates(1, (nrColumns - 1) / 2)
  private[this] def generateNewBlock: Block = Block(generateNewBlockCenter, BlockTypes(randomGen.randomInt(BlockTypes.length)))
  private[this] def updateBoard (coordinates: Coordinates, tetrisBlock: TetrisBlock): Unit = board = board.updated(coordinates.y, board(coordinates.y).updated(coordinates.x, tetrisBlock))

  private[this] def drawBoard(): Unit = {
    if (isGameOver) return

    saveCurrentBoard()
    drawBlock(previousBlock, Empty)
    clearLine()
    drawTiles(dockedTiles)
    checkValidationAndDrawCurrentBlock()
  }

  private[this] def dockBlockAndNewBlock(): Unit = {
    dockedTiles ++= block.getTilesOfTheBlock.map(tile => (tile, block.blockType))
    block = generateNewBlock
  }

  private[this] def hardDropBlock(): Unit = {
    while (!isCrashedIntoDockedTails) { block.center.y += 1 }
    block.center.y -= 1
    dockBlockAndNewBlock()
    checkGameOver()
  }

  private[this] def checkGameOver(): Unit = if (isCrashedIntoDockedTails) { isGameOver = true }
  private[this] def checkValidationAndDrawCurrentBlock (): Unit = {
    val tiles = block.getTilesOfTheBlock
    breakable { tiles.foreach { tile =>
      if ( isCoordinatesValid(tile) ) { updateBoard(tile, block.blockType) }
      else {
        retrievePreviousBoard()
        retrievePreviousBlock()
        break
      }
    }}
  }

  private[this] def isCrashedIntoDockedTails: Boolean = {
    block.getTilesOfTheBlock.foreach { tile => if (baseLine.contains(tile)) { return true }
      dockedTiles.foreach { dockedTile => if (tile == dockedTile._1)            return true } }
    false
  }

  private[this] def isCoordinatesValid(coor: Coordinates): Boolean = {
    def isOutOfBoard:      Boolean = coor.x < 0 || coor.y < 0 || coor.x >= nrColumns || coor.y >= nrRows
    def touchExitingTiles: Boolean = !previousBlock.getTilesOfTheBlock.contains(coor) && board(coor.y)(coor.x) != Empty

    if (isOutOfBoard || touchExitingTiles || isCrashedIntoDockedTails) { false }
    else { true }
  }

  private[this] def clearLine(): Unit = {
    var fullRows = List[Int]()
    def isRollFull: Int => Boolean = row => dockedTiles.count(_._1.y == row) == nrColumns
    def deleteFullRow: Int => Unit = row => dockedTiles = dockedTiles.filter(_._1.y != row)
    drawTiles(dockedTiles, Empty)

    for (row <- 0 until nrRows)
      if (isRollFull(row)) { fullRows :+= row; deleteFullRow(row) }

    dockedTiles.foreach { tile => fullRows.foreach { fullRow =>
      var offset = 0
      if (tile._1.y < fullRow) { offset += 1 }
      tile._1.y += offset // move the tiles that above the removed line down by offset lines
    } }
  }

}

object GameController {
  val BlockTypes: Seq[TetrisBlock] = Seq(IBlock, JBlock, LBlock, OBlock, SBlock, TBlock, ZBlock)

  def apply(randomGen: RandomGenerator, nrColumns: Int, nrRows: Int, Board: Seq[Seq[TetrisBlock]]): GameController =
    new GameController(randomGen, nrColumns, nrRows, Board) { init() }
}