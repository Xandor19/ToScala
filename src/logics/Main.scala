package logics

object Main {
  def main(args: Array[String]): Unit = {
    val goal: Coordinate = new Coordinate(9, 8)
    val start: Coordinate = new Coordinate(1, 1)
    val board: Board = new Board(10, 10, start, goal)
    board.initializeBoard(80)
    println(s"Board has ${board.getInactiveAmount()} inactive cells out of ${board.width * board.depth}")
    val walk: Walk = new Walk(start, board)
    walk.printBoard()
  }
}
