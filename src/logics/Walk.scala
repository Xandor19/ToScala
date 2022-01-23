package logics

import scala.collection.mutable

class Walk (val start: Coordinate, val board: Board) {
  private val family = new Array[Robot](10)
  private val byFitness = new mutable.Queue[Robot]()
  private val notToGoal = new Array[Robot](10)
  private val rounds = new Array[RoundData](50)
  private var currentRound = 0

  def round: Unit = {

  }

  def sortFitness: Unit = {

  }

  def roundStatistics: Unit = {

  }

  def generateSteps: Array[Int] = {

  }

  def mergeSteps(list1: Array[Int], list2: Array[Int]): Array[Int] = {

  }
}
