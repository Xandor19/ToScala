package logics

import scala.util.Random
import scala.collection.mutable

object Board{
  private val tokens = Map(
    "active" -> " ",
    "inactive" -> "x",
    "goal" -> "G",
    "robot" -> "R",
    "start" -> "S"
  )
}

/**
 * Class to represent a board for the walks
 * @param m Board's depth
 * @param n Board's width
 * @param start Coordinates of the starting point
 * @param goal Coordinates of the goal
 */
class Board (val m: Int, val n: Int, val start: Coordinate, val goal: Coordinate) {
  //m is depth (rows amount)
  //n is width (columns amount)
  private val board = Array.ofDim[Boolean](m, n)
  private val robotOccupiedCells = new mutable.ArrayBuffer[Coordinate]()

  /**
   * Initializes this board, activating tiles
   * with the given chances
   *
   * @param activeChances Chances of having a single
   *                      tile activated
   */
  def initializeBoard(activeChances: Int): Unit = {
    for (i <- 0 until m; j <- 0 until n) {
      if (Random.between(0, 100) < activeChances) board(i)(j) = true
    }
    board(start.y)(start.x) = true
    board(goal.y)(goal.x) = true
  }

  /**
   * Gives this board width
   *
   * @return Board's width (n)
   */
  def width: Int = n

  /**
   * Gives this board depth
   *
   * @return Board's depth (m)
   */
  def depth: Int = m

  /**
   * Receives an step given by a robot and performs it if possible
   *
   * @param coordinate The position where the robot is
   * @param step       The direction of the step to make
   * @return A Coordinate object with the new position or the original
   *         one if no step was given
   */
  def stepFrom(coordinate: Coordinate, step: String): Coordinate = {
    //Creates new possible position from the original and the given step
    val newCoordinate = if (step == Step.down) new Coordinate(coordinate.x, coordinate.y - 1)
    else if (step == Step.up) new Coordinate(coordinate.x, coordinate.y + 1)
    else if (step == Step.left) new Coordinate(coordinate.x - 1, coordinate.y)
    else if (step == Step.right) new Coordinate(coordinate.x + 1, coordinate.y)
    else new Coordinate(-1, -1)

    //Tries to process the new position
    try {
      //Checks if the destination tile is active
      if (board(newCoordinate.x)(newCoordinate.y)) {
        //Valid move, the new full tile is deactivated if is not the goal and the released one activated
        board(newCoordinate.x)(newCoordinate.y) = gotToGoal(newCoordinate)
        board(coordinate.x)(coordinate.y) = true

        if(!coordinate.isSame(start)) robotOccupiedCells -= coordinate
        if(!newCoordinate.isSame(start) && !newCoordinate.isSame(goal)) robotOccupiedCells += newCoordinate

        newCoordinate
      }
      //Invalid move
      else coordinate
    }
    catch {
      //The robot tried to perform a step outside the board
      case _: IndexOutOfBoundsException => coordinate
    }
  }

  /**
   * Checks if a given coordinate is the goal, namely, that a
   * robot got to the goal
   *
   * @param coordinate The Coordinate object to check
   * @return True if the coordinate is the goal
   *         False otherwise
   */
  def gotToGoal(coordinate: Coordinate): Boolean = distanceToGoal(coordinate) == 0

  /**
   * Calculates the distance between a given position and this board's goal
   *
   * @param coordinate The initial position
   * @return The distance between the position and the goal, represented as
   *         the number of tiles between them
   */
  def distanceToGoal(coordinate: Coordinate): Int = math.abs(coordinate.x - goal.x) + math.abs(coordinate.y - goal.y)

  def restartRobotsPosition(): Unit = {
    robotOccupiedCells.foreach(c => board(c.x)(c.y) = true)
    robotOccupiedCells.clear()
  }
  /**
   * Return amount of inactive cells
   */
  def getInactiveAmount: Int = {
    board.map(row => row.filterNot(cell => cell).length).sum
  }

  /**
   * Shows the board in a grid shape
   */
  def show(): Unit = {
    for (i <- 0 until m) {
      var row = ""
      
      println("-" * (4 * n + 1))

      for (j <- 0 until n) {
        row += s"| ${getTokenForCoordinate(j, i)} "
      }
      row += "|"
      println(row)
    }
    println("-" * (4 * n + 1))
  }

  /**
   * Supporting method for show method
   * Given a coordinate, it returns its corresponding token
   */
  def getTokenForCoordinate(n: Int, m: Int): String = {
    if (start.isSame(n, m)) Board.tokens("start")
    else if (goal.isSame(n, m)) Board.tokens("goal")
    else if (robotOccupiedCells.exists(_.isSame(new Coordinate(n, m)))) Board.tokens("robot")
    else if (board(m)(n)) Board.tokens("active")
    else Board.tokens("inactive")
  }
}
