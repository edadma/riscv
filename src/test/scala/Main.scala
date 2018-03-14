package xyz.hyperreal.riscv


object Main extends App {
  val cpu = new CPU(
    new Memory {
      def init: Unit = {
        removeDevices
        regions.clear
        add( new RAM("ram", 0x10000, 0x1FFFF) )
        add( ROM.code("program", 0x0000, List(
          itype( 0x34, 0, 'b000, 1, 'b0010011 ),
          0
        )) )
      }
    } )

  cpu.run
  println( cpu.registers mkString ", " )
}