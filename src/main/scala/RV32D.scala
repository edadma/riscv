//@
package xyz.hyperreal.riscv


class FLD( val rs1: Int, val rd: Int ) extends ITypeInstruction( "FLD" ) {
  def apply( cpu: CPU ) = cpu.f(rd) = load( cpu )
}

class FSD( val rs1: Int, val rs2: Int ) extends STypeInstruction( "FSD" ) {
  def apply( cpu: CPU ) = store( cpu, java.lang.Double.doubleToLongBits(cpu.f(rs2)) )
}

// todo: deal with rm
class FMADD( val rs1: Int, val rs2: Int, val rd: Int, val rm: Int ) extends R4TypeInstruction( "FMADD" ) {
  def apply( cpu: CPU ) =
    funct( cpu ) match {
      case 0 =>
      case 1 => cpu.f(rd) = cpu.f(rs1)*cpu.f(rs2) + rs3( cpu )
    }
}
