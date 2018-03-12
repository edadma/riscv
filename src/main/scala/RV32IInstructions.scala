package xyz.hyperreal.riscv


object RV32IInstructions {

  def LUI( operands: Map[Char, Int] ) = new LUI( operands('d') )

  def AUIPC( operands: Map[Char, Int] ) = new AUIPC( operands('d') )

  def JAL( operands: Map[Char, Int] ) = new JAL( operands('d') )

}

class LUI( protected val rd: Int ) extends UTypeInstruction {
  def perform( cpu: CPU ) = {
    cpu.registers(rd) = immediate( cpu )
  }
}

class AUIPC( protected val rd: Int ) extends UTypeInstruction {
  def perform( cpu: CPU ) = {
    cpu.registers(rd) += immediate( cpu )
  }
}

class JAL( protected val rd: Int ) extends JTypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu.pc += immediate( cpu )
    cpu.registers(rd) = cpu.pc + 4
  }

  def perform(cpu: CPU): Unit = ???
}
