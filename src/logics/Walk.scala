package logics

import scala.collection.mutable
import scala.util.Random

/**
 * Class to represent the development of a family
 * @param start The position from which the robots will start
 * @param board The board in which the walks will be made
 */
class Walk (val famSize: Int, val start: Coordinate, val board: Board) {
  private var family = new Array[Robot](famSize)
  private val byFitness = new mutable.Queue[Robot]()
  private var notToGoal = List.empty[Robot]
  private val rounds = new Array[RoundData](50)
  private var currentRound = 0

  /**
   * Creates a 1st generation robot family with random steps
   */
  def createFamily(): Unit = {
    for (i <- 0 until famSize) {
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
  }

  /**
   * Sorts the unsuccessful robots by their final position
   * relative to the goal
   */
  def sortFitness(): Unit = {
    notToGoal = notToGoal.sortWith((r1: Robot, r2: Robot) => board.distanceToGoal(r1.position) < board.distanceToGoal(r2.position));
  }

  /**
   * Function to go one generation forward after a walk is finished
   * The current generation has to be sorted by fitness
   * (sortFitness() function)
   * Should be used after roundStatistics as it replaces the data
   * structures with the results of the walk
   */
  def generationIn(): Unit = {
    notToGoal.foreach(r => byFitness.enqueue(r))

    family = byFitness.toArray

    family(famSize - 1) = new Robot(start, mergeSteps(family(0).steps, family(1).steps))
    family(famSize - 2) = new Robot(start, mergeSteps(family(1).steps, family(2).steps))
    family(famSize - 3) = new Robot(start, mergeSteps(family(2).steps, family(3).steps))
    family(famSize - 4) = new Robot(start, mergeSteps(family(3).steps, family(0).steps))
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
  def generateSteps(): Array[String] = {
    val moves = Array(Step.left, Step.right, Step.up, Step.down)
    val stepsAmount: Int = (board.width * board.depth) / 2
    val steps = new Array[String](stepsAmount)

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
  def mergeSteps(list1: Array[String], list2: Array[String]): Array[String] = {
    val l = list1.length
    val newList = new Array[String](l)

    for (i <- 0 until l) {
      //Randomly picks a step from one of the two lists at a time
      newList(i) = if (Random.nextBoolean()) list1(i) else list2(i)
    }
    newList
  }
}
