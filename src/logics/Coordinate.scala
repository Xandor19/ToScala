package logics

class Coordinate (val x: Int, val y: Int) {

  def compare(other: Coordinate): Boolean = other.x == x && other.y == y
}
