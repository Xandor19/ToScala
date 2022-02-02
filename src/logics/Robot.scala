package logics

/**
 * Class to represent a Robot that will walk
 * @param initial The initial position of the robot
 * @param steps The list of steps that the robot will perform
 */
class Robot (initial: Coordinate, val steps: Array[Int]) {
  private var stepCount = 0
  private var nextToPath = 0
  private var current = initial
  val path = new Array[Coordinate](steps.length)

  /**
   * Gives the total amount of steps given by
   * the robot
   * @return The steps count of the robot
   */
  def totalSteps: Int = stepCount

  /**
   * Gives the current position of the robot
   * @return A Coordinate object with the robot's
   *         (x, y) in the board
   */
  def position: Coordinate = current

  /**
   * Defines wetter the robot can make another step
   * @return True if the robot has more steps remaining
   *         False if it has run out of steps
   */
  def canMove: Boolean = stepCount < steps.length

  /**
   * Signals the robot to make his next step on a given board
   * by sending his current position and the direction of its
   * next step to it
   * If the step drives the robot to a valid new position, its
   * "current" field will update
   * In both cases, the step will be lost and the robot's steps
   * count will increase
   * @param board The board in which the robot will move
   */
  def nextStep(board: Board): Unit = {
    //Receives the robot's new coordinate after the step
    val destination = board.stepFrom(current, steps(stepCount))

    //Checks if the step resulted in a true move
    if (!destination.isSame(current)) {
      //The robot moved, its path and position are updated
      path(nextToPath) = current
      current = destination
      nextToPath += 1
    }
    //Robot's steps count is updated
    stepCount += 1
  }
}
