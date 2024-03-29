package logics

import scala.collection.mutable
import scala.util.Random

/**
 * Class to represent the development of a family
 * @param start The position from which the robots will start
 * @param board The board in which the walks will be made
 */
class Walk (val famSize: Int, val start: Coordinate, val board: Board, val totWalks: Int) {
  private var family = new Array[Robot](famSize)
  private val byFitness = new mutable.Queue[Robot]()
  private var notToGoal = List.empty[Robot]
  private val roundStatistics = new Array[RoundData](totWalks)
  private var currentRound = 0
  private var gotToGoalCounter = 0

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
   * Organizes all processes that happen in a round
   */
  def round(): Unit = {
    //Restarts the counter of robots in goal for current round
    gotToGoalCounter = 0

    //Make all robots walk one step
    launchWalk()

    //Sort robots according to their position
    sortFitness()

    //Collect data from the walk
    calculateRoundStatistics()

    //Accommodate robots for next round
    generationIn()

    //Shows robots' state after the round
    showCurrentState()

    //Restarts data structures used each round
    board.restartRobotsPosition()
    notToGoal = notToGoal.empty
    byFitness.clear()

    //Increase round number
    currentRound += 1
  }

  /**
   * Gives account of the current state
   * - Provides statistics
   * - Prints the board
   */
  def showCurrentState(): Unit = {
    print(s"Review of round: ${currentRound + 1}\n")
    println("Robots in goal:\n" + byFitness.filter(r => board.gotToGoal(r.position)).map(r => r.showRobotState(board)).mkString("\n"))
    println("Robots not in goal:\n" + notToGoal.map(r => r.showRobotState(board)).mkString("\n"))
    println(s"$gotToGoalCounter robots got to goal")
    println(s"The closest distance that a unsuccessful robot was to goal was ${board.distanceToGoal(roundStatistics(currentRound).bestPath.last)}")
    println(s"The robots took an average of ${roundStatistics(currentRound).stepsMean} steps out of ${board.m * board.n / 2}")
    println(s"Best walk was: ${roundStatistics(currentRound).bestStepsSequence.mkString(", ")}")
    printBoard()
  }

  /**
   * Function to send the current generation to a full walk
   */
  def launchWalk(): Unit = {
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
        if (board.gotToGoal(r.position)) {
          byFitness.enqueue(r)
          gotToGoalCounter += 1
        }
        else if (!r.canMove) notToGoal = notToGoal.appended(r)
      })
    }
  }

  /**
   * Sorts the robots according to fitness
   */
  def sortFitness(): Unit = {
    // Sorts robots that didn't get to goal
    notToGoal = notToGoal.sortWith((r1: Robot, r2: Robot) => board.distanceToGoal(r1.position) < board.distanceToGoal(r2.position))

    //Adds each of the robots that didn't got to the goal to the fitness queue
    notToGoal.foreach(r => byFitness.enqueue(r))
  }

  /**
   * Function to go one generation forward after a walk is finished
   * The current generation has to be sorted by fitness
   * (sortFitness() function)
   * Should be used after roundStatistics as it replaces the data
   * structures with the results of the walk
   */
  def generationIn(): Unit = {
    //Replaces old generation with a copy of themselves ready for a new walk
    family = byFitness.toArray.map(r => r.grow())

    //Replaces this walk last robot's steps list with the corresponding merge of the bests robots
    family(famSize - 1).steps = mergeSteps(family(0).steps, family(1).steps)
    family(famSize - 2).steps = mergeSteps(family(1).steps, family(2).steps)
    family(famSize - 3).steps = mergeSteps(family(2).steps, family(3).steps)
    family(famSize - 4).steps = mergeSteps(family(3).steps, family(0).steps)

    //Shuffles the order of the robots to make them start walking in a random order
    family = Random.shuffle(family.toList).toArray
  }

  /**
   * Generates a group of statistical data about the results
   * of the current generation's walk
   * @return A RoundData object containing the required info
   */
  def calculateRoundStatistics(): Unit = {
    //Find average steps in round
    val stepsAvg = byFitness.map(robot => robot.totalSteps).sum / famSize.toFloat
    //Collects this round's best walk and the obtained average
    roundStatistics(currentRound) = new RoundData(byFitness(0).has_visited, byFitness(0).steps, stepsAvg)
  }

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
   * @return An array with the resulting merge of both step lists
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
