package xyz.hyperreal.riscv

import java.io.RandomAccessFile


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

class FileDisk( pathname: String, val cylinders: Int, val heads: Int, val sectors: Int, val block: Int ) extends Disk {

  private val file = new RandomAccessFile( pathname, "rw" )

  if (file.length == 0)
    format

  override def format: Unit = {
    val b = new Array[Byte]( block )

    for (_ <- 1 to cylinders*heads*sectors)
      file.write( block )
  }

  override def read(c: Int, h: Int, s: Int): Vector[Byte] = {
    val b = new Array[Byte]( block )

    file.seek( c*heads*sectors*block + h*sectors*block + s*block )
    file.readFully( b )
    b.toVector
  }

  override def write(c: Int, h: Int, s: Int, b: Seq[Byte]): Unit = {
    file.seek( c*heads*sectors*block + h*sectors*block + s*block )
    file.write( b.toArray )
  }

}