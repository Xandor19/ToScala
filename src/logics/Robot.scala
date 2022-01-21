package logics

class Robot (val initial: Coordinate, val steps: Array[String]) {
  private var stepCount = 0

  val path = new Array[Coordinate](steps.length)

  def totalSteps: Int = stepCount

  def canMove: Boolean = stepCount < steps.length

  def nextStep(board: Board): Unit = {

  }
}
