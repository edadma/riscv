//@
package xyz.hyperreal.riscv


class FLD( val rs1: Int, val rd: Int ) extends ITypeInstruction( "FLD" ) {
  def apply( cpu: CPU ) = cpu.f(rd) = ltod( load(cpu) )
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

class FP( val rs1: Int, val rs2: Int, val rd: Int, val mode: Int ) extends RTypeInstruction( Map(0x11 -> "FSGNJ", 1 -> "FADD", 5 -> "FSUB") ) {
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
      case 0x51 =>
        mode match {
          case 0 => cpu(rd) = boolean2int( cpu.f(rs1) <= cpu.f(rs2) )
          case 1 => cpu(rd) = boolean2int( cpu.f(rs1) < cpu.f(rs2) )
          case 2 => cpu(rd) = boolean2int( cpu.f(rs1) == cpu.f(rs2) )
        }
    }
}

/*
      0af777d3
      0000 1010 11111 0111 0111 0111 1101 0011
*/
