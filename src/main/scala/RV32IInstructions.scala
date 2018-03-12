package xyz.hyperreal.riscv


object RV32IInstructions {

  def LUI( operands: Map[Char, Int] ) = new LUI( operands('d') )

  def AUIPC( operands: Map[Char, Int] ) = new AUIPC( operands('d') )

  def JAL( operands: Map[Char, Int] ) = new JAL( operands('d') )

  def JALR( operands: Map[Char, Int] ) = new JALR( operands('a'), operands('d') )

}

class LUI( protected val rd: Int ) extends UTypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd = immediate( cpu )
  }
}

class AUIPC( protected val rd: Int ) extends UTypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd += immediate( cpu )
  }
}

class JAL( protected val rd: Int ) extends JTypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu.pc += immediate( cpu )
    cpu x rd = cpu.pc + 4
  }
}

class JALR( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu.pc += (immediate( cpu ) + cpu.x(rs1))&0xFFFFFFFE
    cpu x rd = cpu.pc + 4
  }
}
