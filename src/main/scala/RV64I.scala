package xyz.hyperreal.riscv


class LWU( val rs1: Int, val rd: Int ) extends ITypeInstruction( "LWU" ) {
  def apply( cpu: CPU ) =
    cpu(rd) = cpu.mem.readInt( immediate(cpu) + cpu(rs1) ).asInstanceOf[Long]&0xFFFFFFFF
}

class LD( val rs1: Int, val rd: Int ) extends ITypeInstruction( "LD" ) {
  def apply( cpu: CPU ) = cpu(rd) = load( cpu )
}

class SD( val rs1: Int, val rs2: Int ) extends STypeInstruction( "SD" ) {
  def apply( cpu: CPU ) = store( cpu, cpu(rs2) )
}

class ADDIW( val rs1: Int, val rd: Int ) extends ITypeInstruction( "ADDIW" ) {
  def apply( cpu: CPU ) =
    cpu(rd) = immediate(cpu) + cpu(rs1).asInstanceOf[Int]
}

//todo: find out if SLLIW/SRIW/SLLW/SRW should sign-extend to 64 bits or not; the manual (pg. 30) doesn't say to sign-extend
class SLLIW( val shamt: Int, val rs1: Int, val rd: Int ) extends ShiftWITypeInstruction( "SLLIW" ) {
  def apply( cpu: CPU ) =
    if (funct(cpu) == 0)
      cpu(rd) = cpu(rs1).asInstanceOf[Int] << shamt
    else
      illegal( cpu )
}

class SRIW( val shamt: Int, val rs1: Int, val rd: Int ) extends ShiftWITypeInstruction( "SRIW" ) {
  def apply( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1).asInstanceOf[Int] >>> shamt
      case 0x20 => cpu(rd) = cpu(rs1).asInstanceOf[Int] >> shamt
      case _ => illegal( cpu )
    }
}

class ADDW_SUBW_MULW(val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "ADDW", 1 -> "MULW", 0x20 -> "SUBW") ) {
  def apply( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1).asInstanceOf[Int] + cpu(rs2).asInstanceOf[Int]
      case 1 => cpu(rd) = cpu(rs1).asInstanceOf[Int] * cpu(rs2).asInstanceOf[Int]
      case 0x20 => cpu(rd) = cpu(rs1).asInstanceOf[Int] - cpu(rs2).asInstanceOf[Int]
      case _ => illegal( cpu )
    }
}

// division by zero should cause result to be -1 (all 1's) (pg. 36)
class SLLW( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 0, "SLLW" ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1).asInstanceOf[Int] << (cpu(rs2).asInstanceOf[Int]&0x1F)
}

class DIVW( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 1, "DIVW" ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1).asInstanceOf[Int] / cpu(rs2).asInstanceOf[Int]
}

class REMW( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 1, "REMW" ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1).asInstanceOf[Int] % cpu(rs2).asInstanceOf[Int]
}

class REMUW( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 1, "REMUW" ) {
  override def perform( cpu: CPU ) = cpu(rd) = (cpu(rs1)&0xFFFFFFFF) % (cpu(rs2)&0xFFFFFFFF)
}

class SRW_DIVUW( val rs1: Int, val rs2: Int, val rd: Int ) extends
  RTypeInstruction( Map(0 -> "SRLW", 1 -> "DIVUW", 0x20 -> "SRAW") ) {
  def apply( cpu: CPU ) =
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1).asInstanceOf[Int] >>> (cpu(rs2).asInstanceOf[Int]&0x1F)
      case 1 => cpu(rd) = (cpu(rs1)&0xFFFFFFFF) / (cpu(rs2)&0xFFFFFFFF)
      case 0x20 => cpu(rd) = cpu(rs1).asInstanceOf[Int] >> (cpu(rs2).asInstanceOf[Int]&0x1F)
      case _ => illegal( cpu )
    }
}
