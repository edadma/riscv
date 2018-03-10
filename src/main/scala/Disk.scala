package xyz.hyperreal.riscv


class Disk( cylinders: Int, heads: Int, sectors: Int, block: Int ) {

  private val storage = Array.fill[Byte]( cylinders, heads, sectors, block )( 0 )


}