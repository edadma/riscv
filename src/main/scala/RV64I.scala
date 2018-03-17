package xyz.hyperreal.riscv


class LWU( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = cpu.mem.readInt( immediate(cpu) + cpu(rs1) ).asInstanceOf[Long]&0xFFFFFFFF
  }
}

class LD( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = cpu(rd) = load( cpu )
}

class SD( val rs1: Int, val rs2: Int ) extends STypeInstruction {
  override def perform( cpu: CPU ) = store( cpu, cpu(rs2) )
}

class ADDIW( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = immediate(cpu) + cpu(rs1).asInstanceOf[Int]
  }
}

//todo: find out if SLLIW/SRIW/SLLW/SRW should sign-extend to 64 bits or not; the manual (pg. 30) doesn't say to sign-extend
class SLLIW( val shamt: Int, val rs1: Int, val rd: Int ) extends ShiftWITypeInstruction {
  override def perform( cpu: CPU ) = {
    if (funct(cpu) == 0)
      cpu(rd) = cpu(rs1).asInstanceOf[Int] << shamt
    else
      illegal( cpu )
  }
}

class SRIW( val shamt: Int, val rs1: Int, val rd: Int ) extends ShiftWITypeInstruction {
  override def perform( cpu: CPU ) = {
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1).asInstanceOf[Int] >>> shamt
      case 0x20 => cpu(rd) = cpu(rs1).asInstanceOf[Int] >> shamt
      case _ => illegal( cpu )
    }
  }
}

class ADDW_MULW(val rs1: Int, val rs2: Int, val rd: Int ) extends RTypeInstruction {
  override def perform( cpu: CPU ) = {
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1).asInstanceOf[Int] + cpu(rs2).asInstanceOf[Int]
      case 1 => cpu(rd) = cpu(rs1).asInstanceOf[Int] * cpu(rs2).asInstanceOf[Int]
      case 0x20 => cpu(rd) = cpu(rs1).asInstanceOf[Int] - cpu(rs2).asInstanceOf[Int]
      case _ => illegal( cpu )
    }
  }
}

// division by zero should cause result to be -1 (all 1's) (pg. 36)
class SLLW( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 0 ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1).asInstanceOf[Int] << (cpu(rs2).asInstanceOf[Int]&0x1F)
}

class DIVW( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 1 ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1).asInstanceOf[Int] / cpu(rs2).asInstanceOf[Int]
}

class REMW( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 1 ) {
  override def perform( cpu: CPU ) = cpu(rd) = cpu(rs1).asInstanceOf[Int] % cpu(rs2).asInstanceOf[Int]
}

class REMUW( val rs1: Int, val rs2: Int, val rd: Int ) extends FRTypeInstruction( 1 ) {
  override def perform( cpu: CPU ) = cpu(rd) = (cpu(rs1)&0xFFFFFFFF) % (cpu(rs2)&0xFFFFFFFF)
}

class SRW_DIVUW(val rs1: Int, val rs2: Int, val rd: Int ) extends RTypeInstruction {
  override def perform( cpu: CPU ) = {
    funct(cpu) match {
      case 0 => cpu(rd) = cpu(rs1).asInstanceOf[Int] >>> (cpu(rs2).asInstanceOf[Int]&0x1F)
      case 1 => cpu(rd) = (cpu(rs1)&0xFFFFFFFF) / (cpu(rs2)&0xFFFFFFFF)
      case 0x20 => cpu(rd) = cpu(rs1).asInstanceOf[Int] >> (cpu(rs2).asInstanceOf[Int]&0x1F)
      case _ => illegal( cpu )
    }
  }
}
