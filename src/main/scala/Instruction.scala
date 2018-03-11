package xyz.hyperreal.riscv


abstract class Instruction extends (CPU => Boolean)

abstract class UTypeInstruction extends Instruction {

  protected val rd: Int

  def immediate( cpu: CPU ) = cpu.instruction&0xFFFFF000
}

class LUI( protected val rd: Int ) extends UTypeInstruction {
  def apply( cpu: CPU ) = {
    cpu.registers(rd) = immediate( cpu )
    true
  }
}

object IllegalInstruction extends Instruction {

  def apply( cpu: CPU ) = problem( cpu, "illegal instruction" )

}
