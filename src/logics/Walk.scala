package logics

class WalkAux {
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
    private var byFitness = new mutable.Queue[Robot]()
    private var notToGoal = List.empty[Robot]
    private val roundStatistics = new Array[RoundData](50)
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
      gotToGoalCounter = 0
      //Make all robots walk one step
      println("Robots before the walk:\n" + family.map(r => r.showRobotState(board)).mkString("\n"))
      launchWalk()
      println("Robots in goal:\n" + byFitness.map(r => r.showRobotState(board)).mkString("\n"))
      println("Robots not in goal:\n" + notToGoal.map(r => r.showRobotState(board)).mkString("\n"))
      //Sort robots according to their position
      sortFitness()
      println("Robots sorted by round fitness:\n" + byFitness.map(r => r.showRobotState(board)).mkString("\n"))
      //Collect data from the walk
      calculateRoundStatistics()
      //Accommodate robots for next round
      generationIn()
      println("Robots ready for next generation:\n" + family.map(r => r.showRobotState(board)).mkString("\n"))
      //Increase round number
      currentRound += 1
    }

    /**
     * Gives account of the current state
     * - Provides statistics
     * - Prints the board
     */
    def showCurrentState(): Unit = {
      print(s"Current round: $currentRound\n")
      print(s"$gotToGoalCounter robots got to goal\n")
      print(s"The closest a robot was to goal was ${board.distanceToGoal(roundStatistics(currentRound-1).bestPath.last)}\n")
      print(s"The robots took ${roundStatistics(currentRound-1).stepsMean} steps on average out of ${board.m * board.n / 2}\n")
      print(s"Best walk was: ${roundStatistics(currentRound-1).bestStepsSequence.mkString(", ")}\n")
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
      //Restarts the unsuccessful's list to the next round
      println(byFitness.map(r => board.distanceToGoal(r.position)).mkString(", "))
      notToGoal = notToGoal.empty
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
      byFitness = byFitness.empty

      //Replaces this walk last robot's steps list with the corresponding merge of the bests robots
      family(famSize - 1).steps = mergeSteps(family(0).steps, family(1).steps)
      family(famSize - 2).steps = mergeSteps(family(1).steps, family(2).steps)
      family(famSize - 3).steps = mergeSteps(family(2).steps, family(3).steps)
      family(famSize - 4).steps = mergeSteps(family(3).steps, family(0).steps)

    }

    /**
     * Generates a group of statistical data about the results
     * of the current generation's walk
     * @return A RoundData object containing the required info
     */
    def calculateRoundStatistics(): Unit = {
      //Find average steps in round
      val stepsAvg = byFitness.map(robot => robot.totalSteps).sum / famSize
      val cpy = byFitness.toArray
      roundStatistics(currentRound) = new RoundData(cpy(0).has_visited, cpy(0).steps, stepsAvg)
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

}
