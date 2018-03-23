package xyz.hyperreal.riscv


object C {
	class ADDI4SPN( val nzuimm: Int, val rd: Int ) extends AbstractADDI with CIWTypeCompressed {val rs1 = 2}
}