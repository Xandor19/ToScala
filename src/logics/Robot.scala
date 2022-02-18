package logics

/**
 * Class to represent a Robot that will walk
 * @param initial The initial position of the robot
 * @param steps The list of steps that the robot will perform
 */
class Robot (initial: Coordinate, val steps: Array[String]) {
  private var stepCount = 0
  private var current = initial
  private val visited = new Array[Coordinate](steps.length)

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
   * Gives this robot's path so far
   * @return An array with the steps that the robot has
   *         already made
   */
  def path: Array[String] = steps.slice(0, stepCount)

  /**
   * Gives the coordinates that the robot has visited
   * so far
   * @return An array of Coordinate objects with the
   *         visited coordinates
   */
  def has_visited: Array[Coordinate] = visited

  /**
   * Signals the robot to make his next step on a given board
   * by sending his current position and the direction of its
   * next step to it
   * If the step drives the robot to a valid new position, its
   * "current" field will update, if not, it will remain as before
   * In both cases, the step will be lost and the robot's steps
   * count will increase
   * @param board The board in which the robot will move
   */
  def nextStep(board: Board): Unit = {
    //Marks the current tile as visited
    visited(stepCount) = current

    //Receives the robot's new coordinate after the step
    current = board.stepFrom(current, steps(stepCount))

    //Robot's steps count is updated
    stepCount += 1
  }
}
