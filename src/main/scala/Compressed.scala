//@
package xyz.hyperreal.riscv


abstract class Compressed extends (CPU => Unit) {

  def disassemble( cpu: CPU ): String

  def illegal( cpu: CPU ) = cpu.problem( "illegal instruction" )

}

object IllegalCompressed extends Compressed {

  def apply( cpu: CPU ) = illegal( cpu )

  def disassemble( cpu: CPU ): String = "ILLEGAL"

}

abstract class CIWTypeCompressed( val mnemonic: String ) extends Compressed {

  val nzuimm: Int
  val rd: Int

	def immediate( cpu: CPU ) = ((nzuimm >> 2)&8) | ((nzuimm >> 3)&4) | ((nzuimm << 1)&0x3C0) | ((nzuimm >> 1)&0x30)

	def disassemble( cpu: CPU ) = s"$mnemonic x$rd, x2, ${immediate( cpu )}"

}
