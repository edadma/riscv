package xyz.hyperreal.riscv


object Instructions {

  def LUI( operands: Map[Char, Int] ) = new LUI( operands('d') )

}