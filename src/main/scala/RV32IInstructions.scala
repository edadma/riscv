package xyz.hyperreal.riscv


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
  override def perform( cpu: CPU ) = {
    cpu x rd = cpu.mem.readByte( immediate(cpu) + cpu.x(rs1) )
  }
}

class LH( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd = cpu.mem.readShort( immediate(cpu) + cpu.x(rs1) )
  }
}

class LW( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd = cpu.mem.readInt( immediate(cpu) + cpu.x(rs1) )
  }
}

class LBU( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd = cpu.mem.readByte( immediate(cpu) + cpu.x(rs1) )&0xFF
  }
}

class LHU( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd = cpu.mem.readShort( immediate(cpu) + cpu.x(rs1) )&0xFFFF
  }
}

class SB( protected val rs1: Int, protected val rs2: Int ) extends STypeInstruction {
	override def perform( cpu: CPU ) = {
		cpu.mem.writeByte( immediate(cpu) + cpu.x(rs1), (cpu x rs2).toInt )
	}
}

class SH( protected val rs1: Int, protected val rs2: Int ) extends STypeInstruction {
	override def perform( cpu: CPU ) = {
		cpu.mem.writeShort( immediate(cpu) + cpu.x(rs1), (cpu x rs2).toInt )
	}
}

class SW( protected val rs1: Int, protected val rs2: Int ) extends STypeInstruction {
	override def perform( cpu: CPU ) = {
		cpu.mem.writeInt( immediate(cpu) + cpu.x(rs1), (cpu x rs2).toInt )
	}
}

class ADDI( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd = immediate(cpu) + cpu.x(rs1)
  }
}

class SLTI( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd = if (cpu.x(rs1) < immediate(cpu)) 1 else 0
  }
}

class SLTIU( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu x rd = if (java.lang.Long.compareUnsigned(cpu.x(rs1), immediate(cpu)) < 0) 1 else 0
  }
}

class XORI( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = cpu x rd = immediate(cpu) ^ cpu.x(rs1)
}

class ORI( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = cpu x rd = immediate(cpu) | cpu.x(rs1)
}

class ANDI( protected val rs1: Int, protected val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = cpu x rd = immediate(cpu) & cpu.x(rs1)
}

class SLLI( protected val shamt: Int, protected val rs1: Int, protected val rd: Int ) extends ShiftITypeInstruction {
  override def perform( cpu: CPU ) = {
    if (funct(cpu) == 0)
      cpu x rd = cpu.x(rs1) << shamt
    else
      illegal( cpu )
  }
}

class SRLI( protected val shamt: Int, protected val rs1: Int, protected val rd: Int ) extends ShiftITypeInstruction {
  override def perform( cpu: CPU ) = {
    if (funct(cpu) == 0)
      cpu x rd = cpu.x(rs1) >>> shamt
    else
      illegal( cpu )
  }
}

class SRI( protected val shamt: Int, protected val rs1: Int, protected val rd: Int ) extends ShiftITypeInstruction {
  override def perform( cpu: CPU ) = {
    funct(cpu) match {
      case 0 => cpu x rd = cpu.x(rs1) >> shamt
      case 0x20 => cpu x rd = cpu.x(rs1) >> shamt
      case _ => illegal( cpu )
    }
  }
}

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

	def SB( operands: Map[Char, Int] ) = new SB( operands('a'), operands('b') )

	def SH( operands: Map[Char, Int] ) = new SH( operands('a'), operands('b') )

	def SW( operands: Map[Char, Int] ) = new SW( operands('a'), operands('b') )

	def ADDI( operands: Map[Char, Int] ) = new ADDI( operands('a'), operands('d') )

  def SLTI( operands: Map[Char, Int] ) = new SLTI( operands('a'), operands('d') )

  def SLTIU( operands: Map[Char, Int] ) = new SLTIU( operands('a'), operands('d') )

  def XORI( operands: Map[Char, Int] ) = new XORI( operands('a'), operands('d') )

  def ORI( operands: Map[Char, Int] ) = new ORI( operands('a'), operands('d') )

  def ANDI( operands: Map[Char, Int] ) = new ANDI( operands('a'), operands('d') )

  def SLLI( operands: Map[Char, Int] ) = new SLLI( operands('s'), operands('a'), operands('d') )

  def SRI( operands: Map[Char, Int] ) = new SRI( operands('s'), operands('a'), operands('d') )

}
