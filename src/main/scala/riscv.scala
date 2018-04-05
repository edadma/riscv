//@
package xyz.hyperreal
import java.io.ByteArrayOutputStream


package object riscv {

	def boolean2int( b: Boolean ) = if (b) 1 else 0

  def dtol( d: Double ) = java.lang.Double.doubleToLongBits( d )

  def ltod( l: Long ) = java.lang.Double.longBitsToDouble( l )

  def hexByte( a: Int ) = "%02x".format( a&0xFF ).toUpperCase

  def hexShort( a: Int ) = hexByte( a>>8 ) + hexByte( a )

  def hexInt( a: Int ) = hexShort( a>>16 ) + hexShort( a )

  def hexLong( a: Long ) = hexInt( (a>>32).asInstanceOf[Int] ) + hexInt( a.asInstanceOf[Int] )

  def isHex( s: String ) = !s.isEmpty && s.forall( c => "0123456789abcdefABCDEF" contains c )

  def hex( s: String ) = Integer.parseInt( s, 16 )

  def itype( imm: Int, rs1: Int, funct3: Symbol, rd: Int, opcode: Symbol ) =
    (imm << 20) | (rs1 << 15) | (Integer.parseInt(funct3.name drop 1, 2) << 12) | (rd << 7) |
      Integer.parseInt(opcode.name drop 1, 2)

  def btype( imm: Int, rs2: Int, rs1: Int, funct3: Symbol, opcode: Symbol ) =
    ((imm&0x800) >> 4) | ((imm&0x1E) << 7) | ((imm&0x7E0) << 20) | ((imm&0x1000) << 19) |
      (rs2 << 20) | (rs1 << 15) | (Integer.parseInt(funct3.name drop 1, 2) << 12) |
      Integer.parseInt(opcode.name drop 1, 2)

  def utype( imm: Int, rd: Int, opcode: Symbol ) =
    (imm << 12) | (rd << 7) | Integer.parseInt(opcode.name drop 1, 2)

  def csrtype( csr: Int, zimm: Int, funct3: Symbol, rd: Int, opcode: Symbol ) =
    (csr << 20) | (zimm << 15) | (Integer.parseInt(funct3.name drop 1, 2) << 12) | (rd << 7) | Integer.parseInt(opcode.name drop 1, 2)

  def ulong( v: Long ) =
    if (v < 0)
      BigInt( v&0x7FFFFFFFFFFFFFFFL ).setBit( 63 )
    else
      BigInt( v )

	def capture( code: => Unit ) = {
		val out = new ByteArrayOutputStream

		Console.withOut( out )( code )
		out.toString.trim
	}

}