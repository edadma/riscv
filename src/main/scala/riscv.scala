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

}