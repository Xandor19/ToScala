package logics

class Board (val n: Int, val m: Int, val goal: Coordinate) {
  private val board = Array.ofDim[Boolean](n, m)

  def initializeBoard: Unit = {

  }

  def width: Int = n

  def depth: Int = m

  def validMove(coordinate: Coordinate): Boolean = {

  }

  def gotToGoal(coordinate: Coordinate): Boolean = goal.compare(coordinate)

  def distanceToGoal(coordinate: Coordinate): Int = {

  }

}
