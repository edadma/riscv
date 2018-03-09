//@
package xyz.hyperreal.riscv


class BDF( val name: String, val chars: Vector[Bitmap], val width: Int, val height: Int, val ascent: Int ) {

  case class Bit( x: Int, y: Int, set: Boolean )

  def bitStream( c: Char ): Stream[Bit] = {
    require( c < 256, s"character $c is out of range (0 - 255)" )

    val bitmap = chars(c)

    def bitStream( x: Int, y: Int ): Stream[Bit] =
      if (y == height)
        Stream.empty
      else if (x == width)
        bitStream( 0, y + 1 )
      else
        Bit( x, y, (((bitmap.data(y) >>> (15 - x))&1) == 1) ) #:: bitStream( x + 1, y )

    bitStream( 0, 0 )
  }

  case class Segment( x: Int, y: Int, length: Int )

  def segmentStream( c: Char ): Stream[Segment] = {
    def segmentStream( s: Stream[Bit] ): Stream[Segment] = {
      s.dropWhile( !_.set ) match {
        case Stream.Empty => Stream.empty
        case bs@(Bit(_, y, _) #:: _) => bs.span( b => b.y == y && b.set ) match {
          case (segment@(Bit(x, y, _) #:: _), next) =>
            Segment( x, y, segment.length) #:: segmentStream( next )
        }
      }
    }

    segmentStream( bitStream(c) )
  }

  def show( c: Char ): Unit = {
    require( c < 256, s"character $c is out of range (0 - 255)" )

    chars(c) match {
      case null => println( s"character $c not defined" )
      case Bitmap( name, array ) =>
        println( s"character: $name" )
        println( "-"*10 )

        for (row <- array) {
          for (col <- 0 until width)
            if ((row & (1 << (15 - col))) == 0)
              print( ' ' )
            else
              print( 'X' )

          println
        }

        println( "-"*10 )
    }
  }

}

object BDF {

  def read( src: io.Source ) = {
    var font: String = null
    var bbx: Vector[Int] = null
    var ascent: Option[Int] = None
    val chars = new Array[Bitmap]( 256 )
    val lines = src.getLines
    var count = 0
    var bitmap = false
    var index: Int = 0
    var name: String = null
    var encoding: Int = 0
    var data: Array[Int] = null

    def readchars {
      while (lines hasNext) {
        val line = lines.next
        val (word, rest) =
          line.indexOf( ' ' ) match {
            case -1 => (line, "")
            case idx => (line.substring( 0, idx ), line.substring( idx ).trim)
          }

        if (bitmap) {
          word match {
            case "ENDCHAR" =>
              chars(encoding) = Bitmap( name, data.toVector )
              bitmap = false
            case _ if word.length == 4 && word.forall( "0123456789ABCDEF" contains _ ) =>
              data(index) = Integer.parseInt( word, 16 )
              index += 1
            case _ =>
          }
        } else {
          word match {
            case "FONT" => font = rest
            case "FONTBOUNDINGBOX" => bbx = rest.split( " " ) map (_.toInt) toVector
            case "FONT_ASCENT" => ascent = Some( rest.toInt )
            case "ENDFONT" => return
            case "STARTCHAR" => name = rest
            case "ENCODING" => encoding = rest.toInt
            case "BITMAP" =>
              count += 1
              data = new Array[Int]( bbx(1) )
              index = 0
              bitmap = true
            case _ =>
          }
        }
      }
    }

    readchars

    if (ascent isEmpty)
      sys.error( s"font $font is missing ascent value" )

    println( s"$count characters have been read for font $font" )
    new BDF( font, chars.toVector, bbx(0), bbx(1), ascent.get )
  }
}

case class Bitmap( name: String, data: Vector[Int] )

object BDFMain extends App {

//  val src = io.Source.fromString(
//    """
//      |FONT -Misc-Fixed-Medium-R-Normal--15-140-75-75-C-90-ISO8859-1
//      |FONTBOUNDINGBOX 9 15 0 -3
//      |
//      |STARTPROPERTIES 1
//      |FONT_ASCENT 12
//      |ENDPROPERTIES
//      |
//      |STARTCHAR A
//      |ENCODING 65
//      |SWIDTH 576 0
//      |DWIDTH 9 0
//      |BBX 9 15 0 -3
//      |BITMAP
//      |0000
//      |0000
//      |0800
//      |1400
//      |2200
//      |4100
//      |4100
//      |4100
//      |7F00
//      |4100
//      |4100
//      |4100
//      |0000
//      |0000
//      |0000
//      |ENDCHAR
//      |
//      |ENDFONT
//    """.stripMargin
//  )

  val font9x15 = BDF.read( io.Source.fromFile("9x15.bdf") )

  println( font9x15.segmentStream( 'A' ).toList )
}
