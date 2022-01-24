package logics

class Robot (initial: Coordinate, val steps: Array[Int]) {
  private var stepCount = 0
  private var nextToPath = 1
  private var current = initial
  val path = new Array[Coordinate](steps.length)
  path(0) = initial

  def totalSteps: Int = stepCount

  def canMove: Boolean = stepCount < steps.length

  def nextStep(board: Board): Unit = {
    val destination = board.stepFrom(current, steps(stepCount))

    if (!destination.isSame(current)) {
      path(nextToPath) = destination
      current = destination
      nextToPath += 1
    }
    stepCount += 1
  }
}
