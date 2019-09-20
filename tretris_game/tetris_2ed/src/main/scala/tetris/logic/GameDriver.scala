package tetris.logic

import engine.random.RandomGenerator
import tetris.game._
import tetris.logic.GameDriver._
import util.control.Breaks._
import scala.collection.mutable

class GameDriver(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int,
                 var board: Seq[Seq[TetrisBlock]]
                ) {
  var isGameOver = false
  var hasNewBlock = false

  var oldBlock = Block()
  var block: Block = generateNewBlock
  var baseLine: Set[Coordinates] = Set()
  var dockedTiles: Set[(Coordinates, TetrisBlock)] = Set()
  var oldBoard: Seq[Seq[TetrisBlock]] = board.transpose.transpose

  def moveBlockRight(): Unit = {
    oldBlock = Block(block) // change name to save later
    block.center.x += 1
    drawBoard()
  }

  def moveBlockLeft(): Unit = {
    oldBlock = Block(block)
    block.center.x -= 1
    drawBoard()
  }

  def moveBlockDown(): Unit = {
    oldBlock = Block(block)
    block.center.y += 1

    if (isCrashedIntoDockedTails) {
      block = Block(oldBlock)
      dockedTiles ++= block.getTilesOfTheBlock.map(tile => (tile, block.blockType))

      block = generateNewBlock
      // check game over
      if (isCrashedIntoDockedTails) isGameOver = true
    }
    drawBoard()
  }

  def hardDropBlock(): Unit = {
    // this worked but not immediate.
    //    val dockedNum = dockedTiles.size
    //    while (dockedTiles.size == dockedNum) {
    //      moveBlockDown()
    //    }
//    println("Drop block: " + block)
    oldBlock = Block(block)
    while (!isCrashedIntoDockedTails) {
      block.center.y += 1
    }
    block.center.y -= 1
    dockedTiles ++= block.getTilesOfTheBlock.map(tile => (tile, block.blockType))

    block = generateNewBlock
    // check game over
    if (isCrashedIntoDockedTails) isGameOver = true

    drawBoard()
  }

  def isCrashedIntoDockedTails: Boolean = {
    block.getTilesOfTheBlock.foreach { tile => if (baseLine.contains(tile)) { return true }
      dockedTiles.foreach { dockedTile => if (tile == dockedTile._1) { return true } }
    }
    false
  }

  def rotateBlock(opt: String): Unit = {
    oldBlock = Block(block) // duplicate
    opt match {
      case "right" => block.shapeRightRotate()
      case "left" => block.shapeLeftRotate()
    }
    drawBoard()
  }


  def drawBoard(): Unit = {
    if (isGameOver) { return }
    oldBoard = board.transpose.transpose
    clearOldBlock()
    clearLine()
    dockedTiles.foreach { tile =>
      board = board.updated(tile._1.y, board(tile._1.y).updated(tile._1.x, tile._2)) // duplicate
    }

    //draw current block
    val tiles = block.getTilesOfTheBlock
    breakable {
      for (i <- tiles.indices) {
        if (isCoordinatesValid(tiles(i))) {
          board = board.updated(tiles(i).y, board(tiles(i).y).updated(tiles(i).x, block.blockType)) // duplicate
        } else {
          board = oldBoard
          block = Block(oldBlock)
          break
        }
      }
    }
  }


  def generateNewBlock: Block = {
    hasNewBlock = true
    Block(
      center = newBlockCenterCoor,
      blockType = BlockTypes(randomGen.randomInt(BlockTypes.length)),
    )
  }

  def clearBoard(): Unit = board = Seq.fill(nrRows, nrColumns)(Empty)

  def clearOldBlock(): Unit = {
    oldBlock.getTilesOfTheBlock.foreach { tile =>
      board = board.updated(tile.y, board(tile.y).updated(tile.x, Empty)) // duplicate
    }
  }

  def clearLine(): Unit = {
    dockedTiles.foreach { tile => // clear docked tiles
      board = board.updated(
        tile._1.y,
        board(tile._1.y)
          .updated(tile._1.x, Empty)) // duplicate
    }
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

object GameDriver {
  val BlockTypes: Seq[TetrisBlock] = Seq(IBlock, JBlock, LBlock, OBlock, SBlock, TBlock, ZBlock)

  def apply(randomGen: RandomGenerator, nrColumns: Int, nrRows: Int, Board: Seq[Seq[TetrisBlock]]): GameDriver =
    new GameDriver(randomGen, nrColumns, nrRows, Board) {

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