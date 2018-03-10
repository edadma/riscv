package xyz.hyperreal.riscv


abstract class Disk {
  val cylinders: Int
  val heads: Int
  val sectors: Int
  val block: Int

  def format: Unit

  def read( c: Int, h: Int, s: Int ): Vector[Byte]

  def write( c: Int, h: Int, s: Int, b: Seq[Byte] ): Unit
}

class RamDisk( val cylinders: Int, val heads: Int, val sectors: Int, val block: Int ) extends Disk {

  private var storage: Array[Array[Array[Array[Byte]]]] = _

  def format: Unit = {
    storage = Array.fill[Byte]( cylinders, heads, sectors, block )( 0 )
  }

  def read( c: Int, h: Int, s: Int ) = storage(c)(h)(s).toVector

  def write( c: Int, h: Int, s: Int, b: Seq[Byte] ): Unit = {
    for ((d, i) <- b zipWithIndex)
      storage(c)(h)(s)(i) = d
  }

}