package logics

/**
 * Class to group pairs of (x, y) values to represent
 * positions in a board
 * @param x Horizontal position of the tile
 * @param y Vertical position of the tile
 */
class Coordinate (val x: Int, val y: Int) {

  /**
   * Compares wetter two coordinates are the same
   * @param other Coordinate object to compare with
   * @return True if both objects refer to the same (x, y) pair
   *         False otherwise
   */
  def isSame(other: Coordinate): Boolean = other.x == x && other.y == y
}
