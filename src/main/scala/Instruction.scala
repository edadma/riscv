//@
package xyz.hyperreal.riscv


abstract class Instruction extends (CPU => Unit) {

  def perform( cpu: CPU ) {}

  def disassemble( cpu: CPU ): String

  def apply( cpu: CPU ): Unit = {
    perform( cpu )
    cpu.pc += 4
  }

  def illegal( cpu: CPU ) = problem( cpu, "illegal instruction" )

}

abstract class RTypeInstruction( mnemonic: Map[Int, String] ) extends Instruction {

  val rs1: Int
  val rs2: Int
  val rd: Int

  def funct( cpu: CPU ) = cpu.instruction >>> 25

  def funct( cpu: CPU, f: Int ): Int =
    funct( cpu ) match {
      case a if a != f => illegal( cpu )
      case a => a
    }

  def disassemble( cpu: CPU ) = s"${mnemonic(funct( cpu ))} x$rd, x$rs1, x$rs2"
}

abstract class R4TypeInstruction( mnemonic: String ) extends Instruction {

  val rs1: Int
  val rs2: Int
  val rd: Int
  val rm: Int

  def funct( cpu: CPU ) = (cpu.instruction >>> 25)&0x3

  def funct( cpu: CPU, f: Int ): Int =
    funct( cpu ) match {
      case a if a != f => illegal( cpu )
      case a => a
    }

  def rs3( cpu: CPU ) = cpu.f(cpu.instruction >>> 27)

  def disassemble( cpu: CPU ) = s"$mnemonic x$rd, x$rs1, x$rs2, rm: $rm"

}

abstract class FRTypeInstruction( f: Int, m: String ) extends RTypeInstruction( Map(f -> m) ) {

  override def apply( cpu: CPU ): Unit = {
    funct( cpu, f )
    super.apply( cpu )
  }

}

abstract class ITypeInstruction( mnemonic: String ) extends Instruction {

  val rs1: Int
  val rd: Int

  def immediate( cpu: CPU ) = cpu.instruction >> 20

  def load( cpu: CPU ) = cpu.mem.readLong( immediate(cpu) + cpu(rs1) )

  def disassemble( cpu: CPU ) = s"$mnemonic x$rd, x$rs1, ${immediate( cpu )}"

}

abstract class ShiftITypeInstruction( mnemonic: String ) extends Instruction {

  val rs1: Int
  val rd: Int

  def funct( cpu: CPU ) = cpu.instruction >>> 26

  def shamt( cpu: CPU ) = (cpu.instruction >> 20)&0x3F

  def disassemble( cpu: CPU ) = s"$mnemonic x$rd, x$rs1, ${shamt( cpu )}"

}

abstract class ShiftWITypeInstruction( mnemonic: String ) extends Instruction {

  val shamt: Int
  val rs1: Int
  val rd: Int

  def funct( cpu: CPU ) = cpu.instruction >>> 25

  def disassemble( cpu: CPU ) = s"$mnemonic x$rd, x$rs1, $shamt"

}

abstract class STypeInstruction( mnemonic: String ) extends Instruction {

  val rs1: Int
  val rs2: Int

  def immediate( cpu: CPU ) = (cpu.instruction >> 7)&0xF | (cpu.instruction >> 20)&0x7F0

  def store( cpu: CPU, v: Long ) = cpu.mem.writeLong( immediate(cpu) + cpu(rs1), v )

  def disassemble( cpu: CPU ) = s"$mnemonic x$rs1, x$rs2, ${immediate( cpu )}"

}

abstract class BTypeInstruction( mnemonic: String ) extends Instruction {

  val rs1: Int
  val rs2: Int

  def immediate( cpu: CPU ) =
    (cpu.instruction >> 7)&0x1E |
      (cpu.instruction >> 20)&0x7E0 |
      (cpu.instruction << 4)&0x800 |
      (cpu.instruction >> 19)&0xFFFFF000

  def disassemble( cpu: CPU ) = s"$mnemonic x$rs1, x$rs2, ${immediate( cpu )}"

}

abstract class UTypeInstruction( mnemonic: String ) extends Instruction {

  val rd: Int

  def immediate( cpu: CPU ) = cpu.instruction&0xFFFFF000

  def disassemble( cpu: CPU ) = s"$mnemonic x$rd, ${immediate( cpu )}"

}

abstract class JTypeInstruction( mnemonic: String ) extends Instruction {

  val rd: Int

  def immediate( cpu: CPU ) =
    (cpu.instruction >> 21)&0x7FE |
      (cpu.instruction >> 9)&0x800 |
      cpu.instruction&0xFF000 |
      (cpu.instruction >> 11)&0xFFF00000

  def disassemble( cpu: CPU ) = s"$mnemonic x$rd, ${immediate( cpu )}"

}

object IllegalInstruction extends Instruction {

  override def perform( cpu: CPU ) = illegal( cpu )

  val mnemonic = null

  def disassemble( cpu: CPU ): String = "ILLEGAL"

}

object HaltInstruction extends Instruction {

  override def apply( cpu: CPU ) = cpu.halt = true

  val mnemonic = null

  def disassemble( cpu: CPU ): String = "HALT"

}

abstract class EmulatorInstruction extends Instruction {

  val r: Int

  val mnemonic = null

}

class PrintInstruction( val r: Int ) extends EmulatorInstruction {

  override def perform( cpu: CPU ) = {
    println( cpu(r) )
  }

  def disassemble( cpu: CPU ): String = s"PRINT $r"

}
