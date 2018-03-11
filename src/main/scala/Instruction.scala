package xyz.hyperreal.riscv


abstract class Instruction extends (CPU => Boolean)

abstract class UTypeInstruction extends Instruction {

  protected val rd: Int

}

class LUI( protected val rd: Int ) extends UTypeInstruction {
  def apply( cpu: CPU ) = {
    true
  }
}

object IllegalInstruction extends Instruction {

  def apply( cpu: CPU ) = problem( cpu, "illegal instruction" )

}
