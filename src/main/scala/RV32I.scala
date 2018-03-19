//@
package xyz.hyperreal.riscv

import java.lang.Long.{compareUnsigned => lcu}


class LUI( val rd: Int ) extends UTypeInstruction( "LUI" ) {
  override def perform( cpu: CPU ) = cpu(rd) = immediate( cpu )
}

class AUIPC( val rd: Int ) extends UTypeInstruction( "AUIPC" ) {
  override def perform( cpu: CPU ) = cpu(rd) += immediate( cpu )
}

class JAL( val rd: Int ) extends JTypeInstruction( "JAL" ) {
  override def apply( cpu: CPU ) = {
    cpu.pc += immediate( cpu )
    cpu(rd) = cpu.pc + 4
  }
}

class JALR( val rs1: Int, val rd: Int ) extends ITypeInstruction( "JALR" ) {
  override def apply( cpu: CPU ) = {
    cpu.pc += (immediate( cpu ) + cpu(rs1))&0xFFFFFFFE
    cpu(rd) = cpu.pc + 4
  }
}

class BEQ( val rs1: Int, val rs2: Int ) extends BTypeInstruction( "BEQ" ) {
  override def apply( cpu: CPU ) =
    if (cpu(rs1) == cpu(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
}

class BNE( val rs1: Int, val rs2: Int ) extends BTypeInstruction( "BNE" ) {
  override def apply( cpu: CPU ) =
    if (cpu(rs1) != cpu(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
}

class BLT( val rs1: Int, val rs2: Int ) extends BTypeInstruction( "BLT" ) {
  override def apply( cpu: CPU ) =
    if (cpu(rs1) < cpu(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
}

class BLTU( val rs1: Int, val rs2: Int ) extends BTypeInstruction( "BLTU" ) {
  override def apply( cpu: CPU ) =
    if (lcu( cpu(rs1), cpu(rs2) ) < 0)
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
}

class BGE( val rs1: Int, val rs2: Int ) extends BTypeInstruction( "BGE" ) {
  override def apply( cpu: CPU ) =
    if (cpu(rs1) >= cpu(rs2))
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
}

class BGEU( val rs1: Int, val rs2: Int ) extends BTypeInstruction( "BGEU" ) {
  override def apply( cpu: CPU ) =
    if (lcu( cpu(rs1), cpu(rs2) ) >= 0)
      cpu.pc += immediate( cpu )
    else
      cpu.pc += 4
}

class LB( val rs1: Int, val rd: Int ) extends ITypeInstruction( "LB" ) {
  override def perform( cpu: CPU ) =
    cpu(rd) = cpu.mem.readByte( immediate(cpu) + cpu(rs1) )
}

class LH( val rs1: Int, val rd: Int ) extends ITypeInstruction( "LH" ) {
  override def perform( cpu: CPU ) =
    cpu(rd) = cpu.mem.readShort( immediate(cpu) + cpu(rs1) )
}

class LW( val rs1: Int, val rd: Int ) extends ITypeInstruction( "LW" ) {
  override def perform( cpu: CPU ) =
    cpu(rd) = cpu.mem.readInt( immediate(cpu) + cpu(rs1) )
}

class LBU( val rs1: Int, val rd: Int ) extends ITypeInstruction( "LBU" ) {
  override def perform( cpu: CPU ) =
    cpu(rd) = cpu.mem.readByte( immediate(cpu) + cpu(rs1) )&0xFF
}

class LHU( val rs1: Int, val rd: Int ) extends ITypeInstruction( "LHU" ) {
  override def perform( cpu: CPU ) =
    cpu(rd) = cpu.mem.readShort( immediate(cpu) + cpu(rs1) )&0xFFFF
}

class SB( val rs1: Int, val rs2: Int ) extends STypeInstruction( "SB" ) {
	override def perform( cpu: CPU ) =
		cpu.mem.writeByte( immediate(cpu) + cpu(rs1), cpu(rs2).asInstanceOf[Int] )
}

class SH( val rs1: Int, val rs2: Int ) extends STypeInstruction( "SH" ) {
	override def perform( cpu: CPU ) =
		cpu.mem.writeShort( immediate(cpu) + cpu(rs1), cpu(rs2).asInstanceOf[Int] )
}

class SW( val rs1: Int, val rs2: Int ) extends STypeInstruction( "SW" ) {
	override def perform( cpu: CPU ) =
		cpu.mem.writeInt( immediate(cpu) + cpu(rs1), cpu(rs2).asInstanceOf[Int] )
}

class ADDI( val rs1: Int, val rd: Int ) extends ITypeInstruction( "ADDI" ) {
  override def perform( cpu: CPU ) =
    cpu(rd) = immediate(cpu) + cpu(rs1)
}

class SLTI( val rs1: Int, val rd: Int ) extends ITypeInstruction( "SLTI" ) {
  override def perform( cpu: CPU ) =
    cpu(rd) = if (cpu(rs1) < immediate(cpu)) 1 else 0
}

class SLTIU( val rs1: Int, val rd: Int ) extends ITypeInstruction( "SLTIU" ) {
  override def perform( cpu: CPU ) =
    cpu(rd) = if (lcu(cpu(rs1), immediate(cpu)) < 0) 1 else 0
}

class XORI( val rs1: Int, val rd: Int ) extends ITypeInstruction( "XORI" ) {
  override def perform( cpu: CPU ) = cpu(rd) = immediate(cpu) ^ cpu(rs1)
}

class ORI( val rs1: Int, val rd: Int ) extends ITypeInstruction( "ORI" ) {
  override def perform( cpu: CPU ) = cpu(rd) = immediate(cpu) | cpu(rs1)
}

class ANDI( val rs1: Int, val rd: Int ) extends ITypeInstruction( "ANDI" ) {
  override def perform( cpu: CPU ) = cpu(rd) = immediate(cpu) & cpu(rs1)
}

class SLLI( val rs1: Int, val rd: Int ) extends ShiftITypeInstruction( "SLLI" ) {
  override def perform( cpu: CPU ) =
    if (funct(cpu) == 0)
      cpu(rd) = cpu(rs1) << shamt( cpu )
    else
      illegal( cpu )
}

class SRI( val rs1: Int, val rd: Int ) extends ShiftITypeInstruction( "SRI" ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) >>> shamt( cpu )
      case 0x10 => cpu(rd) = cpu(rs1) >> shamt( cpu )
      case _ => illegal( cpu )
    }
}

class ADD_SUB_MUL( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "ADD", 0x20 -> "SUB", 1 -> "MUL") ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) + cpu(rs2)
      case 0x20 => cpu(rd) = cpu(rs1) - cpu(rs2)
      case 1 => cpu(rd) = cpu(rs1) * cpu(rs2)
      case _ => illegal( cpu )
    }
}

class SLL_MULH( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "SLL", 1 -> "MULH") ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) << (cpu(rs2)&0x3F)
      case 1 => cpu(rd) = ((BigInt(cpu(rs1)) * cpu(rs2)) >> 32).longValue
      case _ => illegal( cpu )
    }
}

class SLT_MULHSU( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "SLT", 1 -> "MULHSU") ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = if (cpu(rs1) < cpu(rs2)) 1 else 0
      case 1 => cpu(rd) = ((BigInt(cpu(rs1)) * ulong(cpu(rs2))) >> 32).longValue
      case _ => illegal( cpu )
    }
}

class SLTU_MULHU( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "SLTU", 1 -> "MULHU") ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 =>
        cpu(rd) =
          if (lcu(cpu(rs1).asInstanceOf[Int], cpu(rs2).asInstanceOf[Int]) < 0) 1 else 0
      case 1 => cpu(rd) = ((ulong(cpu(rs1)) * ulong(cpu(rs2))) >> 32).asInstanceOf[Long]
      case _ => illegal( cpu )
    }
}

class XOR_DIV( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "XOR", 1 -> "DIV") ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) ^ cpu(rs2)
      case 1 => cpu(rd) = cpu(rs1) / cpu(rs2)
      case _ => illegal( cpu )
    }
}

class SR_DIVU( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "SR", 1 -> "DIVU") ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) >>> (cpu(rs2)&0x3F)
      case 1 => cpu(rd) = (ulong(cpu(rs1)) / ulong(cpu(rs2))).asInstanceOf[Long]
      case 0x20 => cpu(rd) = cpu(rs1) >> (cpu(rs2)&0x3F)
      case _ => illegal( cpu )
    }
}

class OR_REM( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "OR", 1 -> "REM") ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) | cpu(rs2)
      case 1 => cpu(rd) = cpu(rs1) % cpu(rs2)
      case _ => illegal( cpu )
    }
}

class AND_REMU( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "AND", 1 -> "REMU") ) {
  override def perform( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1) & cpu(rs2)
      case 1 => cpu(rd) = (ulong(cpu(rs1)) % ulong(cpu(rs2))).asInstanceOf[Long]
      case _ => illegal( cpu )
    }
}

object RV32I {

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

  def SLLI( operands: Map[Char, Int] ) = new SLLI( operands('a'), operands('d') )

  def SRI( operands: Map[Char, Int] ) = new SRI( operands('a'), operands('d') )

  def ADD_SUB_MUL(operands: Map[Char, Int] ) = new ADD_SUB_MUL( operands('a'), operands('b'), operands('d') )

  def SLL_MULH(operands: Map[Char, Int] ) = new SLL_MULH( operands('a'), operands('b'), operands('d') )

  def SLT_MULHSU( operands: Map[Char, Int] ) = new SLT_MULHSU( operands('a'), operands('b'), operands('d') )

  def SLTU_MULHU( operands: Map[Char, Int] ) = new SLTU_MULHU( operands('a'), operands('b'), operands('d') )

  def XOR_DIV( operands: Map[Char, Int] ) = new XOR_DIV( operands('a'), operands('b'), operands('d') )

  def SR_DIVU( operands: Map[Char, Int] ) = new SR_DIVU( operands('a'), operands('b'), operands('d') )

  def OR_REM( operands: Map[Char, Int] ) = new OR_REM( operands('a'), operands('b'), operands('d') )

  def AND_REMU( operands: Map[Char, Int] ) = new AND_REMU( operands('a'), operands('b'), operands('d') )

}
