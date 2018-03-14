package xyz.hyperreal.riscv


object Main extends App {
  val cpu = new CPU(
    new Memory {
      def init: Unit = {
        removeDevices
        regions.clear
        add( new RAM("ram", 0x10000, 0x1FFFF) )
        add( ROM.code("program", 0x0000, List(
          itype( 1, 0, 'b000, 1, 'b0010011 ),
          itype( 5, 0, 'b000, 2, 'b0010011 ),
          itype( 1, 1, 'b000, 1, 'b0010011 ),
          btype( -4, 2, 1, 'b001, 'b1100011 ),
          0
        )) )
      }
    } )

  cpu.run
  println( cpu.registers mkString ", " )
}