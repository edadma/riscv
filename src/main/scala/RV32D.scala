//@
package xyz.hyperreal.riscv


class LD( val rs1: Int, val rd: Int ) extends LDITypeInstruction {
  override def perform( cpu: CPU ) = cpu.f(rd) = load( cpu )
}
