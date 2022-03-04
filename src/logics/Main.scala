package logics

import scala.collection.mutable

object Main {
  def main(args: Array[String]): Unit = {
    val goal: Coordinate = new Coordinate(9, 8)
    val start: Coordinate = new Coordinate(1, 1)
    val board: Board = new Board(10, 10, start, goal)
    val walks = 50
    board.initializeBoard(80)

    val walk: Walk = new Walk(10, start, board, walks)
    walk.createFamily()

    for(_ <- 1 to walks) {
      walk.round()
    }
  }
}
