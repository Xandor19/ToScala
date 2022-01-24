package logics

import scala.util.Random

class Board (val n: Int, val m: Int, val goal: Coordinate) {
  private val board = Array.ofDim[Boolean](n, m)

  def initializeBoard(activeChances: Int): Unit = {
    for (i <- 0 until n; j <- 0 until m) {
      if (Random.between(0, 100) < activeChances) board(i)(j) = true
    }
  }

  def width: Int = n

  def depth: Int = m

  def stepFrom(coordinate: Coordinate, step: Int): Coordinate = {
    val newCoordinate = if (step == Step.down) new Coordinate(coordinate.x, coordinate.y - 1)
                        else if (step == Step.up) new Coordinate(coordinate.x, coordinate.y + 1)
                        else if (step == Step.left) new Coordinate(coordinate.x - 1, coordinate.y)
                        else if (step == Step.right) new Coordinate(coordinate.x + 1, coordinate.y)
                        else new Coordinate(-1, -1)

    try {
      if (board(newCoordinate.x)(newCoordinate.y)) newCoordinate
      else coordinate
    }
    catch {
      case _: IndexOutOfBoundsException => coordinate
    }
  }

  def gotToGoal(coordinate: Coordinate): Boolean = goal.isSame(coordinate)

  def distanceToGoal(coordinate: Coordinate): Int = math.abs(coordinate.x - goal.x) + math.abs(coordinate.y - goal.y)
