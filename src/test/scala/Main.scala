//@
package xyz.hyperreal.riscv


object Main extends App {
//  println( "opcodes..." )
//
//  val cpu = new CPU(
//    new Memory {
//      def init: Unit = {
//        removeDevices
//        regions.clear
//        add( new RAM("ram", 0x10000, 0x1FFFF) )
//        add( ROM.code("program", 0x0000, List(
//          itype( 1, 0, 'b000, 1, 'b0010011 ),
//          itype( 0, 0, 'b000, 2, 'b0010011 ),
//          utype( 1000, 2, 'b0010111 ),
//          itype( 1, 1, 'b000, 1, 'b0010011 ),
//          btype( -4, 2, 1, 'b001, 'b1100011 ),
//          itype( 0, 0, 'b101, 0, 'b1110011 )
//        )) )
//      }
//    } )
//
//  println( "collect..." )
//
//  System.gc
//  System.gc
//  System.gc
//
//  val start = System.currentTimeMillis
//
//  println( "run..." )
//  cpu.run
//  println( (cpu.counter.toDouble/(System.currentTimeMillis - start)*1000).toInt + " instructions per second" )
//  println( cpu.x mkString ", " )

//  println( utype( 3, 15, 'b0110111 ).toHexString )
//
//  val cpu = new CPU(
//    new Memory {
//      def init: Unit = {
//        removeDevices
//        regions.clear
//        add( new RAM("ram", 0x10000, 0x1FFFF) )
//        add( ROM.code("program", 0x0000, List(
//          utype( 0x11, 15, 'b0110111 ),   // 000117B7
//          csrtype( 0, 0, 'b101, 0, 'b1110011 )
//        )) )
//      }
//    } )
//
//  cpu.run
//  cpu.registers

  val cpu =
    new CPU(
      new Memory {
        def init: Unit = {
          removeDevices
          regions.clear
          add( new StdIOChar(0x20000) )
          add( new RAM("ram", 0, 0xFFFF) )
          addHexdump( io.Source.fromFile("examples/signum.hex") )
        }
      } ) {
//      trace = true
      }

  cpu.reset
  cpu.run
  cpu.registers
}
