//@
package xyz.hyperreal.riscv


class FLD( val rs1: Int, val rd: Int ) extends ITypeInstruction( "FLD" ) {
  def apply( cpu: CPU ) = cpu.f(rd) = load( cpu )
}

class FSD( val rs1: Int, val rs2: Int ) extends STypeInstruction( "FSD" ) {
  def apply( cpu: CPU ) = store( cpu, dtol(cpu.f(rs2)) )
}

// todo: deal with rm
class FMADD( val rs1: Int, val rs2: Int, val rd: Int, val rm: Int ) extends R4TypeInstruction( "FMADD" ) {
  def apply( cpu: CPU ) =
    funct( cpu ) match {
      case 0 =>
      case 1 => cpu.f(rd) = cpu.f(rs1)*cpu.f(rs2) + rs3( cpu )
    }
}

class FP( val rs1: Int, val rs2: Int, val rd: Int, val mode: Int ) extends FloatRTypeInstruction( Map(0x22 -> "FSGNJ") ) {
  def apply( cpu: CPU ) =
    funct( cpu ) match {
      case 0x11 =>  // FSGNJ
        mode match {
          case 0 => ltod( dtol(cpu.f(rs1))&0x7FFFFFFFFFFFFFFFL | dtol(cpu.f(rs2))&0x8000000000000000L )
          case 1 => ltod( dtol(cpu.f(rs1))&0x7FFFFFFFFFFFFFFFL | dtol(cpu.f(rs2))&0x8000000000000000L^0x8000000000000000L )
          case 2 => ltod( dtol(cpu.f(rs1))&0x7FFFFFFFFFFFFFFFL ^ (dtol(cpu.f(rs2))&0x8000000000000000L) )
          case 3 => illegal( cpu )
        }
      case 1 => cpu.f(rd) = cpu.f(rs1) + cpu.f(rs2)
      case 5 => cpu.f(rd) = cpu.f(rs1) - cpu.f(rs2)
    }
}
