package xyz.hyperreal.riscv


class LWU( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = cpu.mem.readInt( immediate(cpu) + cpu(rs1) ).asInstanceOf[Long]&0xFFFFFFFF
  }
}

class LD( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = cpu.mem.readLong( immediate(cpu) + cpu(rs1) )
  }
}

class SD( val rs1: Int, val rs2: Int ) extends STypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu.mem.writeLong( immediate(cpu) + cpu(rs1), cpu(rs2) )
  }
}

class ADDIW( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = {
    cpu(rd) = immediate(cpu) + cpu(rs1).asInstanceOf[Int]
  }
}
