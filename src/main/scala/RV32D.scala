//@
package xyz.hyperreal.riscv


class FLD( val rs1: Int, val rd: Int ) extends ITypeInstruction {
  override def perform( cpu: CPU ) = cpu.f(rd) = load( cpu )
}

class FSD( val rs1: Int, val rs2: Int ) extends STypeInstruction {
  override def perform( cpu: CPU ) = store( cpu, java.lang.Double.doubleToLongBits(cpu.f(rs2)) )
}

class FMADD( val rs1: Int, val rs2: Int, val rd: Int, val rm: Int ) extends R4TypeInstruction {
  override def perform( cpu: CPU ) =
    funct( cpu ) match {
      case 0 =>
      case 1 => cpu.f(rd) = cpu.f(rs1)*cpu.f(rs2) + rs3( cpu )
    }
}
