package xyz.hyperreal.riscv


object Run {

  val cpu =
    new CPU(
      new Memory {
        def init: Unit = {
          removeDevices
          regions.clear
          removeROM
          add( new StdIOChar(0x20000) )
          add( new RAM("ram", 0, 0xFFFF) )
        }
      } )

  def apply( code: String ) = {
    cpu.memory.init
    cpu.reset
    cpu.memory.addHexdump( io.Source.fromString(code) )
    cpu.pc = cpu.memory.code

    capture( cpu.run )
  }

}