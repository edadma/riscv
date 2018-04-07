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
          add( new RAM("stack", 0, 0xFFFF) )
          add( new RAM("bss", 0x1000000, 0x100FFFF) )
        }
      } )

  def apply( code: String ): String = Run( io.Source.fromFile(code) )

  def apply( code: io.Source ) = synchronized {
    capture {
      cpu.memory.init
      cpu.reset
      cpu.memory.addHexdump( code )
      cpu.pc = cpu.memory.code
      cpu.run
    }
  }

}