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
  var hasNewBlock = false

  var block: Block = generateNewBlock

  var oldBlock = Block()
  var oldBoard: Seq[Seq[TetrisBlock]] = board.transpose.transpose

  var baseLine: Set[Coordinates] = Set()
  var dockedTiles: Set[(Coordinates, TetrisBlock)] = Set()

  def saveBoard():     Unit = oldBoard = board.transpose.transpose
  def saveBlock():     Unit = oldBlock = Block makeDeepCopy block

  def retrieveBlock(): Unit = block = Block makeDeepCopy oldBlock
  def retrieveBoard(): Unit = board = oldBoard

  def updateBoard (coordinates: Coordinates, tetrisBlock: TetrisBlock): Unit =
    board = board.updated(coordinates.y, board(coordinates.y).updated(coordinates.x, tetrisBlock))

  def drawBlock(block: Block, tetrisBlock: TetrisBlock): Unit = block.getTilesOfTheBlock.foreach { tile => updateBoard(tile, tetrisBlock) }
  def drawTiles(tiles: Set[(Coordinates, TetrisBlock)]): Unit = tiles.foreach { tile => updateBoard(tile._1, tile._2)}
  def drawTiles(tiles: Set[(Coordinates, TetrisBlock)], tetrisBlock: TetrisBlock): Unit = tiles.foreach { tile => updateBoard(tile._1, tetrisBlock)}

  def checkGameOver(): Unit = if (isCrashedIntoDockedTails) { isGameOver = true }
  def dockBlockAndNewBlock(): Unit = {
    dockedTiles ++= block.getTilesOfTheBlock.map(tile => (tile, block.blockType))
    block = generateNewBlock
  }
  def hardDropBlock(): Unit = {
    while (!isCrashedIntoDockedTails) { block.center.y += 1 }
    block.center.y -= 1
    dockBlockAndNewBlock()
    checkGameOver()
  }

  def letBlock (action: BlockAction): Unit = {
    saveBlock()
    action match {
      case MoveRight =>     block.center.x += 1
      case MoveLeft =>      block.center.x -= 1
      case MoveDown =>      block.center.y += 1
      case HardDrop =>      hardDropBlock()
      case RotateLeft =>    block.shapeLeftRotate()
      case RotateRight =>   block.shapeRightRotate()
    }
    if (action == MoveDown && isCrashedIntoDockedTails) {
      retrieveBlock()
      dockBlockAndNewBlock()
      checkGameOver()
    };drawBoard()
  }



  def isCrashedIntoDockedTails: Boolean = {
    block.getTilesOfTheBlock.foreach { tile => if (baseLine.contains(tile)) { return true }
    dockedTiles.foreach { dockedTile => if (tile == dockedTile._1)            return true } }
    false
  }

  def checkValidationAndDrawCurrentBlock (): Unit = {
    val tiles = block.getTilesOfTheBlock
    breakable { tiles.foreach { tile =>
      if ( isCoordinatesValid(tile) ) { updateBoard(tile, block.blockType) }
      else {
        retrieveBoard()
        retrieveBlock()
        break
      } }
    }
  }

  def drawBoard(): Unit = {
    if (isGameOver) return
    saveBoard()
    drawBlock(oldBlock, Empty)
    clearLine()
    drawTiles(dockedTiles)
    checkValidationAndDrawCurrentBlock()
  }


  def generateNewBlock: Block = {
    hasNewBlock = true
    Block(center = newBlockCenterCoor,
          blockType = BlockTypes(randomGen.randomInt(BlockTypes.length)) )
  }

  def clearBoard(): Unit = board = Seq.fill(nrRows, nrColumns)(Empty)

  def clearLine(): Unit = {
    drawTiles(dockedTiles, Empty)
    var fullRows = List[Int]()
    for (row <- 0 until nrRows) {
      if (dockedTiles.count(_._1.y == row) == nrColumns) {
        fullRows :+= row
        dockedTiles = dockedTiles.filter(_._1.y != row) // update docked tiles
      }
    }
    dockedTiles.foreach { tile =>
      fullRows.foreach { fullRow =>
        var offset = 0
        if (tile._1.y < fullRow) {
          offset += 1
        }
        tile._1.y += offset // move the tiles that above the removed line down by offset lines
      }
    }
  }

  private def newBlockCenterCoor: Coordinates = Coordinates(1, (nrColumns - 1) / 2)

  private def isCoordinatesValid(coor: Coordinates): Boolean = { // gigantic!!!
    // later replace this by match or literal functions
    if (coor.x < 0 || coor.y < 0 || coor.x >= nrColumns || coor.y >= nrRows) {
      false
    } else if (!oldBlock.getTilesOfTheBlock.contains(coor) && board(coor.y)(coor.x) != Empty) {
      false
    } else if (isCrashedIntoDockedTails)
      false
    else
      true
  }

}

object GameController {
  val BlockTypes: Seq[TetrisBlock] = Seq(IBlock, JBlock, LBlock, OBlock, SBlock, TBlock, ZBlock)

  def apply(randomGen: RandomGenerator, nrColumns: Int, nrRows: Int, Board: Seq[Seq[TetrisBlock]]): GameController =
    new GameController(randomGen, nrColumns, nrRows, Board) {

      // initial docked tiles for none-empty tests board
      for (row <- 0 until nrRows) {
        for (col <- 0 until nrColumns) {
          if (board(row)(col) != Empty) {
            dockedTiles = dockedTiles + Tuple2(Coordinates(row, col), board(row)(col))
          }
        }
      }
      // Add baseline
      for (col <- 0 until nrColumns) {
        baseLine += Coordinates(nrRows, col)
      }
      drawBoard()
    }
}