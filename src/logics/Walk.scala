package logics

import scala.collection.mutable
import scala.util.Random

class Walk (val start: Coordinate, val board: Board) {
  private val family = new Array[Robot](10)
  private val byFitness = new mutable.Queue[Robot]()
  private val notToGoal = new Array[Robot](10)
  private val rounds = new Array[RoundData](50)
  private var currentRound = 0

  def createFamily(): Unit = {
    for (i <- 0 until 10) {
      family(i) = new Robot(start, generateSteps())
    }
  }

  def round(): Unit = {
    
  }

  def sortFitness(): Unit = {

  }

  def roundStatistics: RoundData = {

  }

  def generateSteps(): Array[Int] = {
    val moves = Array(Step.left, Step.right, Step.up, Step.down)
    val stepsAmount: Int = (board.width * board.depth) / 2
    val steps = new Array[Int](stepsAmount)

    for (i <- 0 until stepsAmount) {
      steps(i) = moves(Random.between(0, 4))
    }
    steps
  }

  def mergeSteps(list1: Array[Int], list2: Array[Int]): Array[Int] = {
    val l = list1.length
    val newList = new Array[Int](l)

    for (i <- 0 until l) {
      newList(i) = if (Random.nextBoolean()) list1(i) else list2(i)
    }
    newList
  }
}
