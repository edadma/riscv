//@
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

  val rs1: Int
  val rs2: Int
  val rd: Int

  def funct( cpu: CPU ) = cpu.instruction >>> 25

  def funct( cpu: CPU, f: Int ) = if ((cpu.instruction >>> 25) != f) illegal( cpu )

}

abstract class FRTypeInstruction( f: Int ) extends RTypeInstruction {

  override def apply( cpu: CPU ): Unit = {
    funct( cpu, f )
    super.apply( cpu )
  }

}

abstract class ITypeInstruction extends Instruction {

  val rs1: Int
  val rd: Int

  def immediate( cpu: CPU ) = cpu.instruction >> 20

  def load( cpu: CPU ) = cpu.mem.readLong( immediate(cpu) + cpu(rs1) )

}

abstract class ShiftITypeInstruction extends Instruction {

  val rs1: Int
  val rd: Int

  def funct( cpu: CPU ) = cpu.instruction >>> 26

  def shamt( cpu: CPU ) = (cpu.instruction >> 20)&0x3F

}

abstract class ShiftWITypeInstruction extends Instruction {

  val shamt: Int
  val rs1: Int
  val rd: Int

  def funct( cpu: CPU ) = cpu.instruction >>> 25

}

abstract class STypeInstruction extends Instruction {

  val rs1: Int
  val rs2: Int

  def immediate( cpu: CPU ) = (cpu.instruction >> 7)&0xF | (cpu.instruction >> 20)&0x7F0

  def store( cpu: CPU, v: Long ) = cpu.mem.writeLong( immediate(cpu) + cpu(rs1), v )

}

abstract class BTypeInstruction extends Instruction {

  val rs1: Int
  val rs2: Int

  def immediate( cpu: CPU ) =
    (cpu.instruction >> 7)&0x1E |
      (cpu.instruction >> 20)&0x7E0 |
      (cpu.instruction << 4)&0x800 |
      (cpu.instruction >> 19)&0xFFFFF000

}

abstract class UTypeInstruction extends Instruction {

  val rd: Int

  def immediate( cpu: CPU ) = cpu.instruction&0xFFFFF000
}

abstract class JTypeInstruction extends Instruction {

  val rd: Int

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

abstract class EmulatorInstruction extends Instruction {

  val r: Int

}

class PrintInstruction( val r: Int ) extends EmulatorInstruction {

  override def perform( cpu: CPU ) = {
    println( cpu(r) )
  }

}
