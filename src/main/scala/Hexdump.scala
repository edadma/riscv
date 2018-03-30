package xyz.hyperreal.riscv

import scala.collection.mutable.{ArrayBuffer, ListBuffer}


object Hexdump {

  val SECTION = "^Hex dump of section '(.*)':"r
  val DATA = """^  0x(\w+) (\w+)(?: (\w+)(?: (\w+)(?: (\w+))?)?)? .*"""r

  case class Section( name: String, start: Long, data: Vector[Byte] )

  def read( src: io.Source ) = {
    val lines = src.getLines zip Iterator.from( 1 )
    val sections = new ListBuffer[Section]
    var section = false
    var name: String = null
    var start: Long = 0
    val data = new ArrayBuffer[Byte]

    def add: Unit = {
      sections += Section( name, start, data.toVector )
      data.clear
      section = false
    }

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
        case ("", _) if section => add
        case ("", _) =>
        case (s, line) => sys.error( s"unrecognized input on line $line: '$s'" )
      }

    if (section)
      add

    sections.toList
  }

}
