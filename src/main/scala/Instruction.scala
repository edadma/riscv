package xyz.hyperreal.riscv


class Instruction extends (CPU => Unit) {

  def perform( cpu: CPU ) {}

  def apply( cpu: CPU ): Unit = {
    perform( cpu )
    cpu.pc += 4
  }

  def illegal( cpu: CPU ) = problem( cpu, "illegal instruction" )

}

abstract class RTypeInstruction extends Instruction {

  protected val rs1: Int
  protected val rs2: Int
  protected val rd: Int

  def funct( cpu: CPU ) = cpu.instruction >>> 25

}

abstract class ITypeInstruction extends Instruction {

  protected val rs1: Int
  protected val rd: Int

  def immediate( cpu: CPU ) = cpu.instruction >> 20

}

abstract class ShiftITypeInstruction extends Instruction {

  protected val shamt: Int
  protected val rs1: Int
  protected val rd: Int

  def funct( cpu: CPU ) = cpu.instruction >>> 25

}

abstract class STypeInstruction extends Instruction {

  protected val rs1: Int
  protected val rs2: Int

  def immediate( cpu: CPU ) = (cpu.instruction >> 7)&0xF | (cpu.instruction >> 20)&0x7F0

}

abstract class BTypeInstruction extends Instruction {

  protected val rs1: Int
  protected val rs2: Int

  def immediate( cpu: CPU ) =
    (cpu.instruction >> 7)&0x1E |
      (cpu.instruction >> 20)&0x3F0 |
      (cpu.instruction >> 7)&1 |
      (cpu.instruction >> 19)&0x1000

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

  override def perform( cpu: CPU ) = illegal( cpu )

}

object HaltInstruction extends Instruction {

  override def apply( cpu: CPU ) = cpu.halt = true

}
