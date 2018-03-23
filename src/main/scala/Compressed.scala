//@
package xyz.hyperreal.riscv


abstract class Compressed extends (CPU => Unit) {

  def disassemble( cpu: CPU ): String

  def illegal( cpu: CPU ) = cpu.problem( "illegal instruction" )

}

object IllegalCompressed extends Compressed {

  def apply( cpu: CPU ) = illegal( cpu )

  val mnemonic = null

  def disassemble( cpu: CPU ): String = "ILLEGAL"

}
