package xyz.hyperreal.riscv


abstract class Instruction extends (CPU => Unit) {

  def perform( cpu: CPU )

  def apply( cpu: CPU ): Unit = {
    perform( cpu )
    cpu.pc += 4
  }

}

abstract class UTypeInstruction extends Instruction {

  protected val rd: Int

  def immediate( cpu: CPU ) = cpu.instruction&0xFFFFF000
}

abstract class JTypeInstruction extends Instruction {

  protected val rd: Int

  def immediate( cpu: CPU ) =
    (cpu.instruction >> 21)&0x7FE |
      (cpu.instruction >> 9)&0x800 |
      cpu.instruction&0xFF000 |
      (cpu.instruction >> 11)&0xFFF00000
}

object IllegalInstruction extends Instruction {

  def perform( cpu: CPU ) = problem( cpu, "illegal instruction" )

}
