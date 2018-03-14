//@
package xyz.hyperreal


package object riscv {

  def hexByte( a: Int ) = "%02x".format( a&0xFF ).toUpperCase

  def hexShort( a: Int ) = hexByte( a>>8 ) + hexByte( a )

  def hexInt( a: Int ) = hexShort( a>>16 ) + hexShort( a )

  def hexLong( a: Long ) = hexInt( (a>>32).asInstanceOf[Int] ) + hexInt( a.asInstanceOf[Int] )

  def isHex( s: String ) = !s.isEmpty && s.forall( c => "0123456789abcdefABCDEF" contains c )

  def hex( s: String ) = Integer.parseInt( s, 16 )

  def problem( cpu: CPU, error: String ) = {
    sys.error( error )
  }

  def itype( imm: Int, rs1: Int, funct3: Symbol, rd: Int, opcode: Symbol ) =
    (imm << 20) | (rs1 << 15) | (Integer.parseInt(funct3.name drop 1, 2) << 12) | (rd << 7) |
      Integer.parseInt(opcode.name drop 1, 2)
}