package xyz.hyperreal.riscv


object RV32IConstructors {

  def LUI( operands: Map[Char, Int] ) = new LUI( operands('d') )

  def AUIPC( operands: Map[Char, Int] ) = new AUIPC( operands('d') )

}