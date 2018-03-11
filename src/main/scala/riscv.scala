//@
package xyz.hyperreal


package object riscv {

  def hexByte( a: Int ) = "%02x".format( a&0xFF ).toUpperCase

  def hexWord( a: Int ) = hexByte( a>>8 ) + hexByte( a )

  def isHex( s: String ) = !s.isEmpty && s.forall( c => "0123456789abcdefABCDEF" contains c )

  def hex( s: String ) = Integer.parseInt( s, 16 )

  def problem( cpu: CPU, error: String ) = {
    sys.error( error )
  }

}