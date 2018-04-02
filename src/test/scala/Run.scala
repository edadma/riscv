package xyz.hyperreal.riscv


object Run {

/* start.s

  .align 6
  .globl _start
    _start:
    lui	 sp, 0x10
  call main
    csrrwi x0, 0, 0

    .globl halt
    halt:
    csrrwi x0, 0, 0
*/
  val cpu =
    new CPU(
      new Memory {
        def init: Unit = {
          regions.clear
          add( new StdIOChar(0x20000) )
          add( new RAM("ram", 0, 0xFFFF) )
        }
      } )

  def apply( code: String ) = synchronized {
    capture {
      cpu.memory.init
      cpu.reset
      cpu.memory.addHexdump( io.Source.fromString(code) )
      cpu.pc = cpu.memory.code
      cpu.run
    }
  }

}