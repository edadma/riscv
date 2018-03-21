//@
package xyz.hyperreal.riscv


abstract class CSR( name: String ) {

  def read( cpu: CPU, addr: Int ): Long

  def write( cpu: CPU, addr: Int, v: Long )

}

object IllegalCSR extends CSR( "Illegal" ) {

  def read( cpu: CPU, addr: Int ) = cpu.problem( s"attempt to read from illegal CSR address: $addr" )

  def write( cpu: CPU, addr: Int, v: Long ) = cpu.problem( s"attempt to write to illegal CSR address: $addr" )

}