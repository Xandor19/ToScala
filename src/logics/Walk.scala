package logics

import scala.collection.mutable
import scala.util.Random

/**
 * Class to represent the development of a family
 * @param start The position from which the robots will start
 * @param board The board in which the walks will be made
 */
class Walk (val start: Coordinate, val board: Board) {
  private val family = new Array[Robot](10)
  private val byFitness = new mutable.Queue[Robot]()
  private var notToGoal = List.empty[Robot]
  private val rounds = new Array[RoundData](50)
  private var currentRound = 0

  /**
   * Creates a 1st generation robot family with random steps
   */
  def createFamily(): Unit = {
    for (i <- 0 until 10) {
      //Initializes current robot with a randomly generated step list
      family(i) = new Robot(start, generateSteps())
    }
  }

  /**
   * Calls the show method on the board object to print itself displaying its state
   */
  def printBoard(): Unit = board.show();

  /**
   * Function to send the current generation to a full walk
   */
  def round(): Unit = {
    var canWalk = family

    //Loops while there are robots able to walk
    while (canWalk.nonEmpty) {
      //Reduces the family to the robots which can still walk
      canWalk = canWalk.filter(r => r.canMove && !board.gotToGoal(r.position))

      //For each walker, applies the given lambda function
      canWalk.foreach(r => {
        //Tells current robot to make its next step
        r.nextStep(board)

        //Checks whether the robot got to the goal or ran out of steps
        if (board.gotToGoal(r.position)) byFitness.enqueue(r)
        else if (!r.canMove) notToGoal = notToGoal.appended(r)
      })
    }
    //TODO end of round and fitness calculation
  }

  /**
   * Sorts the unsuccessful robots by their final position
   * relative to the goal
   */
  def sortFitness(): Unit = {
    notToGoal = notToGoal.sortWith((r1: Robot, r2: Robot) => board.distanceToGoal(r1.position) < board.distanceToGoal(r2.position));
  }

  /**
   * Generates a group of statistical data about the results
   * of the current generation's walk
   * @return A RoundData object containing the required info
   */
//  def roundStatistics(): RoundData = {
//
//  }

  /**
   * Generates a random list of steps for a 1st generation
   * robot
   * @return An array with the resulting step list
   */
  def generateSteps(): Array[Int] = {
    val moves = Array(Step.left, Step.right, Step.up, Step.down)
    val stepsAmount: Int = (board.width * board.depth) / 2
    val steps = new Array[Int](stepsAmount)

    //Generates the steps list with the board defined size
    for (i <- 0 until stepsAmount) {
      //Picks randomly one of the four possible steps
      steps(i) = moves(Random.between(0, 4))
    }
    steps
  }

  /**
   * Merges the existent steps lists of two robots of a
   * generation
   * @param list1 Steps list of robot 1
   * @param list2 Steps list of robot 2
   * @return
   */
  def mergeSteps(list1: Array[Int], list2: Array[Int]): Array[Int] = {
    val l = list1.length
    val newList = new Array[Int](l)

    for (i <- 0 until l) {
      //Randomly picks a step from one of the two lists at a time
      newList(i) = if (Random.nextBoolean()) list1(i) else list2(i)
    }
    newList
  }
}
