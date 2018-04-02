import xyz.hyperreal.riscv._

object Example extends App {
  val cpu =
    new CPU(
      new Memory {
        def init: Unit = {
          regions.clear
          add( new StdIOChar(0x20000) )
          add( new RAM("ram", 0, 0xFFFF) )
          addHexdump( io.Source.fromFile("hello.hex") )
        }
      } )

  cpu.reset
  cpu.run
}