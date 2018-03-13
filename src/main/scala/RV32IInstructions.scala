package xyz.hyperreal.riscv


object RV32IInstructions {

  def LUI( operands: Map[Char, Int] ) = new LUI( operands('d') )

  def AUIPC( operands: Map[Char, Int] ) = new AUIPC( operands('d') )

  def JAL( operands: Map[Char, Int] ) = new JAL( operands('d') )

  def JALR( operands: Map[Char, Int] ) = new JALR( operands('a'), operands('d') )

  def BEQ( operands: Map[Char, Int] ) = new BEQ( operands('a'), operands('b') )

  def BNE( operands: Map[Char, Int] ) = new BNE( operands('a'), operands('b') )

  def BLT( operands: Map[Char, Int] ) = new BLT( operands('a'), operands('b') )

  def BGE( operands: Map[Char, Int] ) = new BGE( operands('a'), operands('b') )

  def BLTU( operands: Map[Char, Int] ) = new BLTU( operands('a'), operands('b') )

  def BGEU( operands: Map[Char, Int] ) = new BGEU( operands('a'), operands('b') )

  def LB( operands: Map[Char, Int] ) = new LB( operands('a'), operands('d') )

  def LH( operands: Map[Char, Int] ) = new LH( operands('a'), operands('d') )

  def LW( operands: Map[Char, Int] ) = new LW( operands('a'), operands('d') )

  def LBU( operands: Map[Char, Int] ) = new LBU( operands('a'), operands('d') )

  def LHU( operands: Map[Char, Int] ) = new LHU( operands('a'), operands('d') )

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

class BEQ( protected val rs1: Int, protected val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (cpu.x(rs1) == cpu.x(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BNE( protected val rs1: Int, protected val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (cpu.x(rs1) != cpu.x(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BLT( protected val rs1: Int, protected val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (cpu.x(rs1) < cpu.x(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BLTU( protected val rs1: Int, protected val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (java.lang.Long.compareUnsigned( cpu.x(rs1), cpu.x(rs2) ) < 0)
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BGE( protected val rs1: Int, protected val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (cpu.x(rs1) >= cpu.x(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BGEU( protected val rs1: Int, protected val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (java.lang.Long.compareUnsigned( cpu.x(rs1), cpu.x(rs2) ) >= 0)
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class LB( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu x rd = cpu.mem.readByte( immediate(cpu) + cpu.x(rs1) )
  }
}

class LH( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu x rd = cpu.mem.readShort( immediate(cpu) + cpu.x(rs1) )
  }
}

class LW( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu x rd = cpu.mem.readInt( immediate(cpu) + cpu.x(rs1) )
  }
}

class LBU( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu x rd = cpu.mem.readByte( immediate(cpu) + cpu.x(rs1) )&0xFF
  }
}

class LHU( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu x rd = cpu.mem.readShort( immediate(cpu) + cpu.x(rs1) )&0xFFFF
  }
}
