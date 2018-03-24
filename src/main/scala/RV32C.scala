package xyz.hyperreal.riscv


object C {
	class ADDI4SPN( val nzuimm: Int, val rd: Int ) extends CIWTypeCompressed( "ADDI" ) {
		def apply( cpu: CPU ) = cpu(rd) = immediate(cpu) + cpu(2)
	}
}