package xyz.hyperreal.riscv

import scala.collection.mutable.{ArrayBuffer, ListBuffer}


object Main extends App {
//  println( "opcodes..." )
//
//  val HALT = 0X7F
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
////          0x101,
//          itype( 1, 1, 'b000, 1, 'b0010011 ),
//          btype( -4, 2, 1, 'b001, 'b1100011 ),
//          HALT
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

  println( Hexdump.read( "hex" ) mkString "\n" )
}

object Hexdump {

  val SECTION = "^Hex dump of section '(.*)':"r
  val DATA = """^  0x(\w{8}) (\w{8})(?: (\w{8})(?: (\w{8})(?: (\w{8}))?)?)? .*"""r

  case class Section( name: String, start: Long, data: Vector[Byte] )

  def read( file: String ) = {
    val src = io.Source.fromFile( file )
    val lines = src.getLines zip Iterator.from( 1 )
    val sections = new ListBuffer[Section]
    var section = false
    var name: String = null
    var start: Long = 0
    val data = new ArrayBuffer[Byte]

    while (lines.hasNext)
      lines.next match {
        case (SECTION( n ), _) => name = n
        case (DATA( addr, d1, d2, d3, d4 ), _) =>
          if (!section) {
            start = Integer.parseInt( addr, 16 )
            section = true
          }

          Seq( d1, d2, d3, d4 ) foreach {
            case null =>
            case d => data ++= d grouped 2 map (Integer.parseInt( _, 16 ).toByte)
          }
        case ("", _) if section =>
          sections += Section( name, start, data.toVector )
          data.clear
          section = false
        case ("", _) =>
        case (s, line) => sys.error( s"unrecognized input on line $line: '$s'" )
      }

    sections.toList
  }

}
