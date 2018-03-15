package xyz.hyperreal.riscv

import java.lang.Long.{compareUnsigned => lcu}


class LUI( val rd: Int ) extends UTypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = immediate( cpu )
  }
}

class AUIPC( val rd: Int ) extends UTypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) += immediate( cpu )
  }
}

class JAL( val rd: Int ) extends JTypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu.pc += immediate( cpu )
    cpu(rd) = cpu.pc + 4
  }
}

class JALR( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def apply( cpu: CPU ) = {
    cpu.pc += (immediate( cpu ) + cpu(rs1))&0xFFFFFFFE
    cpu(rd) = cpu.pc + 4
  }
}

class BEQ( val rs1: Int, val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (cpu(rs1) == cpu(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BNE( val rs1: Int, val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (cpu(rs1) != cpu(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BLT( val rs1: Int, val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (cpu(rs1) < cpu(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BLTU( val rs1: Int, val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (lcu( cpu(rs1), cpu(rs2) ) < 0)
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BGE( val rs1: Int, val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (cpu(rs1) >= cpu(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class BGEU( val rs1: Int, val rs2: Int ) extends BTypeInstruction {
  override def apply( cpu: CPU ) = {
    if (lcu( cpu(rs1), cpu(rs2) ) >= 0)
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
  }
}

class LB( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = cpu.mem.readByte( immediate(cpu) + cpu(rs1) )
  }
}

class LH( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = cpu.mem.readShort( immediate(cpu) + cpu(rs1) )
  }
}

class LW( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = cpu.mem.readInt( immediate(cpu) + cpu(rs1) )
  }
}

class LBU( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = cpu.mem.readByte( immediate(cpu) + cpu(rs1) )&0xFF
  }
}

class LHU( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = cpu.mem.readShort( immediate(cpu) + cpu(rs1) )&0xFFFF
  }
}

class SB( val rs1: Int, val rs2: Int ) extends STypeInstruction {
	override def perform( cpu: CPU ) = {
		cpu.mem.writeByte( immediate(cpu) + cpu(rs1), cpu(rs2).toInt )
	}
}

class SH( val rs1: Int, val rs2: Int ) extends STypeInstruction {
	override def perform( cpu: CPU ) = {
		cpu.mem.writeShort( immediate(cpu) + cpu(rs1), cpu(rs2).toInt )
	}
}

class SW( val rs1: Int, val rs2: Int ) extends STypeInstruction {
	override def perform( cpu: CPU ) = {
		cpu.mem.writeInt( immediate(cpu) + cpu(rs1), cpu(rs2).toInt )
	}
}

class ADDI( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = immediate(cpu) + cpu(rs1)
  }
}

class SLTI( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = if (cpu(rs1) < immediate(cpu)) 1 else 0
  }
}

class SLTIU( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = if (lcu(cpu(rs1), immediate(cpu)) < 0) 1 else 0
  }
}

class XORI( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = cpu(rd) = immediate(cpu) ^ cpu(rs1)
}

class ORI( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = cpu(rd) = immediate(cpu) | cpu(rs1)
}

class ANDI( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = cpu(rd) = immediate(cpu) & cpu(rs1)
}

class SLLI( protected val shamt: Int, val rs1: Int, val rd: Int ) extends ShiftITypeInstruction {
  override def perform( cpu: CPU ) = {
    if (funct(cpu) == 0)
      cpu(rd) = cpu(rs1) << shamt
    else
      illegal( cpu )
  }
}

class SRLI( protected val shamt: Int, val rs1: Int, val rd: Int ) extends ShiftITypeInstruction {
  override def perform( cpu: CPU ) = {
    if (funct(cpu) == 0)
      cpu(rd) = cpu(rs1) >>> shamt
    else
      illegal( cpu )
  }
}

class SRI( protected val shamt: Int, val rs1: Int, val rd: Int ) extends ShiftITypeInstruction {
  override def perform( cpu: CPU ) = {
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) >> shamt
      case 0x20 => cpu(rd) = cpu(rs1) >> shamt
      case _ => illegal( cpu )
    }
  }
}

class ADD( val rs1: Int, val rs2: Int, val rd: Int ) extends RTypeInstruction {
  override def perform( cpu: CPU ) = {
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) + cpu(rs2)
      case 0x20 => cpu(rd) = cpu(rs1) - cpu(rs2)
      case _ => illegal( cpu )
    }
  }
}

class SLL( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 0 ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1) << (cpu(rs2)&0x1F)
}

class SLT( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 0 ) {
  override def perform( cpu: CPU ) = cpu(rd) = if (cpu(rs1) < cpu(rs2)) 1 else 0
}

class SLTU( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 0 ) {
  override def perform( cpu: CPU ) = cpu(rd) =
    if (lcu(cpu(rs1).asInstanceOf[Int], cpu(rs2).asInstanceOf[Int]) < 0) 1 else 0
}

class XOR( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 0 ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1) ^ cpu(rs2)
}

class SR( val rs1: Int, val rs2: Int, val rd: Int ) extends RTypeInstruction {
  override def perform( cpu: CPU ) = {
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) >>> (cpu(rs2)&0x1F)
      case 0x20 => cpu(rd) = cpu(rs1) >> (cpu(rs2)&0x1F)
      case _ => illegal( cpu )
    }
  }
}

class OR( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 0 ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1) | cpu(rs2)
}

class AND( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 0 ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1) & cpu(rs2)
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

  def ADD( operands: Map[Char, Int] ) = new ADD( operands('a'), operands('b'), operands('d') )

  def SLL( operands: Map[Char, Int] ) = new SLL( operands('a'), operands('b'), operands('d') )

  def SLT( operands: Map[Char, Int] ) = new SLT( operands('a'), operands('b'), operands('d') )

  def SLTU( operands: Map[Char, Int] ) = new SLTU( operands('a'), operands('b'), operands('d') )

  def XOR( operands: Map[Char, Int] ) = new XOR( operands('a'), operands('b'), operands('d') )

  def SR( operands: Map[Char, Int] ) = new SR( operands('a'), operands('b'), operands('d') )

  def OR( operands: Map[Char, Int] ) = new OR( operands('a'), operands('b'), operands('d') )

  def AND( operands: Map[Char, Int] ) = new AND( operands('a'), operands('b'), operands('d') )

}
