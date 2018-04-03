//@
package xyz.hyperreal.riscv

import scala.collection.mutable.ListBuffer


class CPU( private [riscv] val memory: Memory ) {

  private [riscv] val x = new Array[Long]( 32 )
  private [riscv] var pc: Long = 0
  private [riscv] var instruction = 0
  private [riscv] var halt = false
  private [riscv] val f = new Array[Double]( 32 )
  private [riscv] var fcsr: Int = 0
  private [riscv] var disp: Long = 0

  val csrs = Array.fill[CSR]( 0x1000 )( IllegalCSR )
  var counter = 0L
  var trace = false

	protected var running = false

  private val opcodes32 = Array.fill[Instruction]( 0x2000000 )( IllegalInstruction )
  private val opcodes16 = Array.fill[Compressed]( 0x10000 )( IllegalCompressed )

  private [riscv] def ecall = problem( "ecall" )

  private [riscv] def ebreak = problem( "ebreak" )

	def isRunning = running

  def apply( r: Int ) = if (r == 0) 0L else x(r)

  def update( r: Int, v: Long ): Unit = x(r) = v

  private def populate32( pattern: String, inst: Map[Char, Int] => Instruction ) =
    for ((idx, m) <- generate( pattern ))
      opcodes32(idx) = inst( m )

  private def populate32( insts: List[(String, Map[Char, Int] => Instruction)] ): Unit =
    for ((p, c) <- insts)
      populate32( p, c )

  private def populate16( pattern: String, inst: Map[Char, Int] => Compressed ) =
    for ((idx, m) <- generate( pattern ))
      opcodes16(idx) = inst( m )

  private def populate16( insts: List[(String, Map[Char, Int] => Compressed)] ): Unit =
    for ((p, c) <- insts)
      populate16( p, c )

  private def generate( pattern: String ) = {
    case class Variable( v: Char, lower: Int, upper: Int, bits: List[Int] )

    val Range = "([a-zA-Z]):([0-9]+)-([0-9]+)"r
    val p = pattern replace (" ", "") split ";"

    require( p.nonEmpty, "empty pattern" )

    val bits = p(0)

    require( bits.length > 0, "pattern should comprise at least one bit" )
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

  csrs(0) =
    new CSR( "state" ) {
      def read( cpu: CPU, addr: Int ) = 0

      def write( cpu: CPU, addr: Int, v: Long ): Unit =
        v match {
          case 0 => halt = true
          case _ => problem( s"undefined emulator control value: $v" )
        }
    }

  import RV32I._

  // RV32I
  populate32(
    List[(String, Map[Char, Int] => Instruction)](
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
      "----- aaaaa 001 ddddd 0010011" -> SLLI,
      "----- aaaaa 101 ddddd 0010011" -> SRI,
      "bbbbb aaaaa 000 ddddd 0110011" -> ADD_SUB_MUL,
      "bbbbb aaaaa 001 ddddd 0110011" -> SLL_MULH,
      "bbbbb aaaaa 010 ddddd 0110011" -> SLT_MULHSU,
      "bbbbb aaaaa 011 ddddd 0110011" -> SLTU_MULHU,
      "bbbbb aaaaa 100 ddddd 0110011" -> XOR_DIV,
      "bbbbb aaaaa 101 ddddd 0110011" -> SR_DIVU,
      "bbbbb aaaaa 110 ddddd 0110011" -> OR_REM,
      "bbbbb aaaaa 111 ddddd 0110011" -> AND_REMU,
      "----- 00000 000 00000 1110011" -> ((operands: Map[Char, Int]) => new ECALL_EBREAK),
      "----- iiiii 101 ddddd 1110011" -> ((operands: Map[Char, Int]) => new CSRRWI( operands('i'), operands('d') )),
    ) )

  // RV64I
  populate32(
    List[(String, Map[Char, Int] => Instruction)](
      "----- aaaaa 110 ddddd 0000011" -> ((operands: Map[Char, Int]) => new LWU( operands('a'), operands('d') )),
      "----- aaaaa 011 ddddd 0000011" -> ((operands: Map[Char, Int]) => new LD( operands('a'), operands('d') )),
      "bbbbb aaaaa 011 ----- 0100011" -> ((operands: Map[Char, Int]) => new SD( operands('a'), operands('b') )),
      "----- aaaaa 000 ddddd 0011011" -> ((operands: Map[Char, Int]) => new ADDIW( operands('a'), operands('d') )),
      "sssss aaaaa 001 ddddd 0011011" -> ((operands: Map[Char, Int]) => new SLLIW( operands('s'), operands('a'), operands('d') )),
      "sssss aaaaa 101 ddddd 0011011" -> ((operands: Map[Char, Int]) => new SRIW( operands('s'), operands('a'), operands('d') )),
      "bbbbb aaaaa 000 ddddd 0111011" -> ((operands: Map[Char, Int]) => new ADDW_SUBW_MULW( operands('a'), operands('b'), operands('d') )),
      "bbbbb aaaaa 001 ddddd 0111011" -> ((operands: Map[Char, Int]) => new SLLW( operands('a'), operands('b'), operands('d') )),
      "bbbbb aaaaa 100 ddddd 0111011" -> ((operands: Map[Char, Int]) => new DIVW( operands('a'), operands('b'), operands('d') )),
      "bbbbb aaaaa 101 ddddd 0111011" -> ((operands: Map[Char, Int]) => new SRW_DIVUW( operands('a'), operands('b'), operands('d') )),
      "bbbbb aaaaa 110 ddddd 0111011" -> ((operands: Map[Char, Int]) => new REMW( operands('a'), operands('b'), operands('d') )),
      "bbbbb aaaaa 111 ddddd 0111011" -> ((operands: Map[Char, Int]) => new REMUW( operands('a'), operands('b'), operands('d') )),
    ) )

  // RV32D
  populate32(
    List[(String, Map[Char, Int] => Instruction)](
      "----- aaaaa 011 ddddd 0000111" -> ((operands: Map[Char, Int]) => new FLD( operands('a'), operands('d') )),
      "bbbbb aaaaa 011 ----- 0100111" -> ((operands: Map[Char, Int]) => new FSD( operands('a'), operands('b') )),
      "bbbbb aaaaa rrr ddddd 1000011" -> ((operands: Map[Char, Int]) => new FMADD( operands('a'), operands('b'), operands('d'), operands('r') )),
      "bbbbb aaaaa mmm ddddd 1010011" -> ((operands: Map[Char, Int]) => new FP( operands('a'), operands('b'), operands('d'), operands('m') )),
    ) )

  // RV32C
  populate16(
    List[(String, Map[Char, Int] => Compressed)](
      "000 iiiiiiii ddd 00" -> ((operands: Map[Char, Int]) => new C.ADDI4SPN( operands('i'), operands('d') )),
    ) )

  def registers: Unit = {
    if (memory.valid( pc )) {
      val m = memory.find( pc )
      val low = m.readByte( pc )
      val (inst, disassembly) =
        if ((low&3) == 3) {
          val inst = m.readInt( pc, low )

          (hexInt( inst ), opcodes32(inst&0x1FFFFFF).disassemble(this))
        } else {
          val inst = m.readShort( pc, low )

          (hexShort( inst ), opcodes16(inst).disassemble(this))
        }

      printf( "%8x  %s  %s\n", pc, inst, disassembly )
    } else
      println( s"pc=${pc.toHexString}")

    def regs( start: Int ) {
      for (i <- start until (start + 5 min 32))
        printf( "%21s  ", s"x$i=${x(i).toHexString}" )

      println
    }

    for (i <- 0 until 32 by 5)
      regs( i )
  }

  def problem( error: String ) = {
    registers
    sys.error( s"error at ${pc.toHexString}: $error" )
  }

  def reset: Unit = {
    memory.reset

    for (r <- csrs if r != IllegalCSR)
      r.init( this )

    for (i <- x indices) {
      x(i) = 0
      f(i) = 0
    }

    pc = memory.code
    halt = false
    fcsr = 0
  }

  def execute: Unit = {
    if (trace)
      registers

    val m = memory.find( pc )
    val low = m.readByte( pc )

    if ((low&3) == 3) {
      instruction = m.readInt( pc, low )
      disp = 4
      opcodes32(instruction&0x1FFFFFF)( this )
    } else {
      instruction = m.readShort( pc, low )
      disp = 2
      opcodes16(instruction)( this )
    }

    pc += disp
    counter += 1
  }

	def step =
		if (running)
			sys.error( "already running" )
		else {
			running = true
			execute
			running = false
		}

  def run: Unit = {
    running = true

    while (!halt)
      execute

    running = false
  }

  memory.problem = problem
}
