//@
package xyz.hyperreal.riscv

import scala.collection.mutable.ListBuffer


class CPU( val mem: Memory ) {

  private [riscv] val registers = new Array[Long]( 32 )
  private [riscv] var pc: Long = 0
  private [riscv] var instruction = 0
  private [riscv] var halt = false

  private val opcodes = Array.fill[Instruction]( 0x2000000 )( IllegalInstruction )

  def apply( r: Int ) = if (r == 0) 0L else registers(r)

  def update( r: Int, v: Long ): Unit = registers(r) = v

//  private [riscv] val x =
//    new AnyRef {
//      def apply( r: Int ) = if (r == 0) 0L else registers(r)
//
//      def update( r: Int, v: Long ): Unit = registers(r) = v
//    }
//  private [riscv] def x_=( r: Int, v: Long ): Unit = registers(r) = v
//
//  private [riscv] def x( r: Int ) = if (r == 0) 0L else registers(r)

  private def populate( pattern: String, inst: Map[Char, Int] => Instruction ) =
    for ((idx, m) <- generate( pattern ))
      opcodes(idx) = inst( m )

  private def populate( insts: List[(String, Map[Char, Int] => Instruction)] ): Unit =
    for ((p, c) <- insts)
      populate( p, c )

  private def generate( pattern: String ) = {
    case class Variable( v: Char, lower: Int, upper: Int, bits: List[Int] )

    val Range = "([a-zA-Z]):([0-9]+)-([0-9]+)"r
    val p = pattern replace (" ", "") split ";"

    require( p.nonEmpty, "empty pattern" )

    val bits = p(0)

    require( bits.length == 25, "pattern should comprise twenty-five bits" )
    require( bits.forall(c => c == '0' || c == '1' || c.isLetter || c == '-'), "pattern should comprise only 0's, 1's, letters or -'s" )

    val ranges = Map( (p drop 1 map {case Range( v, l, u ) => v(0) -> (l.toInt, u.toInt)}): _* )

    require( ranges forall {case (_, (l, u)) => 0 <= l && l <= u}, "first value of range must be less than or equal to second and be non-negative" )

    val (constant, variables) = {
      def scan( acc: Int, pos: Int, chars: List[Char], vars: Map[Char, List[Int]] ): (Int, Map[Char,List[Int]]) =
        chars match {
          case Nil => (acc, vars)
          case '0' :: t => scan( acc, pos << 1, t, vars )
          case '1' :: t => scan( acc | pos, pos << 1, t, vars )
          case v :: t if vars contains v => scan( acc, pos << 1, t, vars + (v -> (vars(v) :+ pos)) )
          case v :: t => scan( acc, pos << 1, t, vars + (v -> List(pos)) )
        }

      scan( 0, 1, bits.reverse.toList, Map() )
    }

    val enumeration = new ListBuffer[(Int, Map[Char, Int])]

    def enumerate( acc: Int, vars: List[Variable], vals: Map[Char, Int] ): Unit =
      vars match {
        case Nil => enumeration += ((acc, vals))
        case v :: t =>
          for (i <- v.lower to v.upper)
            enumerate( acc|int2bits(0, i, v.bits), t, vals + (v.v -> i) )
      }

    def int2bits( res: Int, n: Int, bits: List[Int] ): Int =
      bits match {
        case Nil => res
        case b :: t if (n&1) > 0 => int2bits( res|b, n >> 1, t )
        case b :: t => int2bits( res, n >> 1, t )
      }

    enumerate( constant, variables.toList map {
      case (v, b) =>
        if (ranges contains v) {
          require( ranges(v)._2 < (1 << b.length), "second value of range must be less than 2^#bits" )
          Variable( v, ranges(v)._1, ranges(v)._2, b )
        } else
          Variable( v, 0, (1 << b.length) - 1, b )
      }, Map() )
    enumeration.toList
  }

  import RV32IInstructions._

  val RV32I =
    List[(String, Map[Char, Int] => Instruction)](
      "00000 00000 000 00000 0000000" -> (_ => HaltInstruction),
      "----- ----- --- ddddd 0110111" -> LUI,
      "----- ----- --- ddddd 0010111" -> AUIPC,
      "----- ----- --- ddddd 1101111" -> JAL,
      "----- aaaaa 000 ddddd 1100111" -> JALR,
	    "bbbbb aaaaa 000 ----- 1100011" -> BEQ,
			"bbbbb aaaaa 001 ----- 1100011" -> BNE,
			"bbbbb aaaaa 100 ----- 1100011" -> BLT,
			"bbbbb aaaaa 101 ----- 1100011" -> BGE,
			"bbbbb aaaaa 110 ----- 1100011" -> BLTU,
			"bbbbb aaaaa 111 ----- 1100011" -> BGEU,
      "----- aaaaa 000 ddddd 0000011" -> LB,
      "----- aaaaa 001 ddddd 0000011" -> LH,
      "----- aaaaa 010 ddddd 0000011" -> LW,
      "----- aaaaa 100 ddddd 0000011" -> LBU,
      "----- aaaaa 101 ddddd 0000011" -> LHU,
      "bbbbb aaaaa 000 ----- 0100011" -> SB,
      "bbbbb aaaaa 001 ----- 0100011" -> SH,
      "bbbbb aaaaa 010 ----- 0100011" -> SW,
      "----- aaaaa 000 ddddd 0010011" -> ADDI,
      "----- aaaaa 010 ddddd 0010011" -> SLTI,
      "----- aaaaa 011 ddddd 0010011" -> SLTIU,
      "----- aaaaa 100 ddddd 0010011" -> XORI,
      "----- aaaaa 110 ddddd 0010011" -> ORI,
      "----- aaaaa 111 ddddd 0010011" -> ANDI,
      "sssss aaaaa 001 ddddd 0010011" -> SLLI,
      "sssss aaaaa 101 ddddd 0010011" -> SRI,
		)

  populate( RV32I )

  def run: Unit = {

    while (!halt) {
      instruction = mem.readInt( pc.toInt )
      opcodes(instruction&0xFFFFFF)( this )
    }
  }
}
