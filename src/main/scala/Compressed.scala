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

trait CIWTypeCompressed {

  val nzuimm: Int

	def immediate( cpu: CPU ) = ((nzuimm >> 2)&8) | ((nzuimm >> 3)&4) | ((nzuimm << 1)&0x3C0) | ((nzuimm >> 1)&0x30)

}

//abstract class CIWTypeCompressed( mnemonic: String ) extends AbstractITypeInstruction( mnemonic: String ) {
//
//  val nzuimm: Int
//  val rd: Int
//
//	def immediate( cpu: CPU ) = ((nzuimm >> 2)&8) | ((nzuimm >> 3)&4) | ((nzuimm << 1)&0x3C0) | ((nzuimm >> 1)&0x30)
//
//}


trait ITypeInstruction {

  val rs1: Int

  def immediate( cpu: CPU ) = (cpu.instruction >> 20).asInstanceOf[Long]

  def load( cpu: CPU ) = cpu.mem.readLong( immediate(cpu) + cpu(rs1) )

}
