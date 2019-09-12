package tetris.logic

import engine.random.RandomGenerator
import tetris.game._
import util.control.Breaks._
import scala.collection.mutable

class GameDriver(val randomGen: RandomGenerator,
                 val nrColumns: Int,
                 val nrRows: Int,
                 var board: Seq[Seq[TetrisBlock]]
                ) {
  //  println("*"*50)
  //  board.map(println)
  //  println("*"*50)
  val BlockTypes: Seq[TetrisBlock] = Seq(IBlock, JBlock, LBlock, OBlock, SBlock, TBlock, ZBlock)

  var block: Block = generateNewBlock
  var oldBlock = Block()
  var oldBoard: Seq[Seq[TetrisBlock]] = board.transpose.transpose

  //  var isOverLapped = false
  //  var isOutOfBoundary = false
  var dockedTiles: Set[(Coordinates, TetrisBlock)] = Set()
  var baseLine: Set[Coordinates] = Set()

  var hasNewBlock = false
  var isGameOver = false

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

    if (isCrashedDockedTails) {
      //      hasNewBlock = false
      block = Block(oldBlock)

      // add docked tiles
//      println("*" * 50)
//      println("Base line: " + baseLine)
//      println("Original Docked: " + dockedTiles)
//      println("Add docked tiles: " + block.getBlockTiles)
      dockedTiles ++= block.getBlockTiles.map(tile => (tile, block.blockType))


      block = generateNewBlock
      // check game over
      if (isCrashedDockedTails) isGameOver = true

    }

    drawBoard()
  }

  def hardDropBlock(): Unit = {
    // this worked but not immediate.
    //    val dockedNum = dockedTiles.size
    //    while (dockedTiles.size == dockedNum) {
    //      moveBlockDown()
    //    }
    println("Drop block: " + block)
    oldBlock = Block(block)
    while (!isCrashedDockedTails) {
      block.center.y += 1
    }
    block.center.y -= 1
    dockedTiles ++= block.getBlockTiles.map(tile => (tile, block.blockType))

    //    clearLine()

    block = generateNewBlock
    // check game over
    if (isCrashedDockedTails) isGameOver = true

    drawBoard()
  }

  def isCrashedDockedTails: Boolean = {
    block.getBlockTiles.foreach { tile =>
      if (baseLine.contains(tile))
        return true
      dockedTiles.foreach { dockedTile =>
        if (tile == dockedTile._1) {
          return true
        }
      }
    }
    //    println("Docked tiles:" + dockedTiles)
    //    println("Valid block: " + block.getBlockTiles)
    false
  }

  def rotateBlock(opt: String): Unit = {
    //    println("Rotate " + opt)

    oldBlock = Block(block)
    //    println("PrevBlock" + oldBlock)
    opt match {
      case "right" => block.shapeRightRotate
      case "left" => block.shapeLeftRotate
    }
    //    println(block.index)
    //    println(block.blockShape)
    drawBoard()
  }


  def drawBoard(): Unit = {
    if (isGameOver) return

//    println("Draw Docked tiles before: " + dockedTiles)

    oldBoard = board.transpose.transpose
    //    clearBoard()
    clearOldBlock()
    clearLine()

    //    println("*" * 50)
//    println("Draw Docked tiles after: " + dockedTiles)
    //    println("*" * 50)

    // drawn docked tiles
    dockedTiles.foreach { tile =>
      board = board.updated(tile._1.y, board(tile._1.y).updated(tile._1.x, tile._2))
    }

    //draw current block
    val tiles = block.getBlockTiles
    breakable {
      for (i <- tiles.indices) {
        if (isCoordinatesValid(tiles(i))) {
          board = board.updated(tiles(i).y, board(tiles(i).y).updated(tiles(i).x, block.blockType))
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
    oldBlock.getBlockTiles.foreach { tile =>
      board = board.updated(tile.y, board(tile.y).updated(tile.x, Empty))
    }
  }

  def clearLine(): Unit = {
    //    println("Before updating:" + dockedTiles)
    // clear original docked places
    dockedTiles.foreach { tile =>
      board = board.updated(
        tile._1.y,
        board(tile._1.y)
          .updated(tile._1.x, Empty))
    }
    var fullRows = List[Int]()
    for (row <- 0 until nrRows) {
      if (dockedTiles.count(_._1.y == row) == nrColumns) {
        fullRows :+= row
        dockedTiles = dockedTiles.filter(_._1.y != row) // update docked tiles
      }
    }
//    println("Full rows: " + fullRows)
    dockedTiles.foreach { tile =>
      fullRows.foreach { fullRow =>
        var offset = 0
        if (tile._1.y < fullRow) {
          offset += 1
        }
        tile._1.y += offset // move the tiles that above the removed line down by offset lines
      }
    }

    //    println("Full rows:" + fullRows)
    //    println("After  updating:" + dockedTiles)
    //    println("Old block: " + oldBlock)
  }

  private def newBlockCenterCoor: Coordinates = Coordinates(1, (nrColumns - 1) / 2)

  private def isCoordinatesValid(coor: Coordinates): Boolean = {
    //    isOutOfBoundary = false
    //    isOverLapped = false
    // later replace this by match or literal functions
    if (coor.x < 0 || coor.y < 0 || coor.x >= nrColumns || coor.y >= nrRows) {
      //      isOutOfBoundary = true
      false
    } else if (!oldBlock.getBlockTiles.contains(coor) && board(coor.y)(coor.x) != Empty) {
      //      isOverLapped = true
      false
    } else if (isCrashedDockedTails)
      false
    else
      true
  }

}

object GameDriver {
  def apply(randomGen: RandomGenerator, nrColumns: Int, nrRows: Int, Board: Seq[Seq[TetrisBlock]]): GameDriver =
    new GameDriver(randomGen, nrColumns, nrRows, Board) {
      //      block.blockType = LBlock
      //      block.blockShape = LeftmostShape
//      println(s"${nrRows} X ${nrColumns}")

      // initial docked tiles for none-empty tests board
      for (row <- 0 until nrRows) {
        for (col <- 0 until nrColumns) {
          if (board(row)(col) != Empty) {
            //            println("Add Docked tile at: " + row + ", " + col)
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