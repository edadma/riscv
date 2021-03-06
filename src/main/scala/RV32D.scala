//@
package xyz.hyperreal.riscv


// todo: code FITypeInstruction to do correct register disassembly
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
          case 0 => cpu.f(rd) = ltod( dtol(cpu.f(rs1))&0x7FFFFFFFFFFFFFFFL | dtol(cpu.f(rs2))&0x8000000000000000L )
          case 1 => cpu.f(rd) = ltod( dtol(cpu.f(rs1))&0x7FFFFFFFFFFFFFFFL | dtol(cpu.f(rs2))&0x8000000000000000L^0x8000000000000000L )
          case 2 => cpu.f(rd) = ltod( dtol(cpu.f(rs1))&0x7FFFFFFFFFFFFFFFL ^ (dtol(cpu.f(rs2))&0x8000000000000000L) )
          case 3 => illegal( cpu )
        }
      case 1 => cpu.f(rd) = cpu.f(rs1) + cpu.f(rs2)
      case 5 => cpu.f(rd) = cpu.f(rs1) - cpu.f(rs2)
      case 9 => cpu.f(rd) = cpu.f(rs1) * cpu.f(rs2)
      case 13 => cpu.f(rd) = cpu.f(rs1) / cpu.f(rs2)
      case 0x51 =>
        mode match {
          case 0 => cpu(rd) = boolean2int( cpu.f(rs1) <= cpu.f(rs2) )
          case 1 => cpu(rd) = boolean2int( cpu.f(rs1) < cpu.f(rs2) )
          case 2 => cpu(rd) = boolean2int( cpu.f(rs1) == cpu.f(rs2) )
        }
      case 0x61 => // FCVT
        rs2 match {
          case 0 => cpu(rd) = cpu.f(rs1).asInstanceOf[Int]
          case 1 => cpu(rd) = cpu.f(rs1).asInstanceOf[Long]&0xFFFFFFFFL
          case 2 => cpu(rd) = cpu.f(rs1).asInstanceOf[Long]
          case 3 => cpu(rd) = BigDecimal( cpu.f(rs1) ).longValue
        }
			case 0x69 => // FCVT
				rs2 match {
					case 0 => cpu.f(rd) = cpu(rs1).asInstanceOf[Int]
          case 1 => cpu.f(rd) = cpu(rs1)&0xFFFFFFFFL
          case 2 => cpu.f(rd) = cpu(rs1)
          case 3 => cpu.f(rd) = ulong( cpu(rs1) ).doubleValue
				}
      // RV64D
      case 0x79 => // FMV.D.X
        if (rs2 == 0 && mode == 0)
          cpu.f(rd) = ltod( cpu(rs1) )
        else
          illegal( cpu )
    }
}

/*
      0af777d3
      0000 1010 11111 0111 0111 0111 1101 0011
*/
