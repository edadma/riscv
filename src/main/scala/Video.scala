package xyz.hyperreal.riscv

import java.awt.Color
import java.util.{Timer, TimerTask}

import scala.swing.{Graphics2D, MainFrame, Panel, SimpleSwingApplication}
import scala.swing.Swing._
import scala.swing.event.WindowActivated


class Video( columns: Int, rows: Int, font: BDF ) extends Panel {

  preferredSize = (columns*font.width, rows*font.height)
  background = Color.BLACK

  case class Cell( char: Char, color: Color )

  private val array = Array.fill[Cell]( rows, columns )( Cell(' ', Color.GRAY) )
  private var curx = 0
  private var cury = 0
  private var curc = Color.GRAY

  def position_=( pos: (Int, Int) ): Unit = {
    val (x, y) = pos

    require( 0 <= x && x < columns, s"column out of range: $x" )
    require( 0 <= y && y < rows, s"row out of range: $y" )
    curx = x
    cury = y
  }

  def position = (curx, cury)

  def color_=( c: Color ): Unit = {
    require( c ne null, "null when Color instance was expected" )
    curc = c
  }

  def color = curc

  def scrollup( lines: Int ): Unit = {
    require( 0 < lines && lines < rows, s"number of lines to scroll is out of range: $lines" )
    Array.copy( array, lines, array, 0, rows - lines )

    for (i <- rows - lines until rows)
      array(i) = Array.fill( columns )( Cell(' ', Color.BLACK) )
  }

  private def write( c: Char ): Unit = {
    def nextline: Unit =
      if (cury == rows - 1)
        scrollup( 1 )
      else
        cury += 1

    c match {
      case '\n' =>
        curx = 0
        nextline
      case _ =>
        array( cury )( curx ) = Cell( c, curc )
        curx = (curx + 1)%columns

        if (curx == 0)
          nextline
    }
  }

  private def write( o: Any ) {String.valueOf( o ) foreach write}

  def print( o: Any ) {
    write( o )
    repaint
  }

  def println = print( '\n' )

  def println( o: Any ) {
    write( o )
    write( '\n' )
    repaint
  }

  override def paintComponent( g: Graphics2D ): Unit = {
    super.paintComponent( g )

    for (i <- 0 until columns; j <- 0 until rows) {
      val Cell( char, color ) = array(j)(i)

      g setColor color
      font.draw( g, char, i*font.width, j*font.height )
    }
  }
}

object VideoMain extends SimpleSwingApplication {

  val video =
    new Video( 80, 25, BDF.read(io.Source.fromFile("9x15.bdf")) ) {
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
      println( "This is a very very very very very very very very boring test." )
      println( "Have a really really really really really really really nice day." )
    }

  def top =
    new MainFrame {
      title = "video test"
      contents = video
      listenTo( this )

      val timer = new Timer
      val task =
        new TimerTask {
          var counter = 1

          def run: Unit = {
            video.println( s"$counter - this is another very very very very very very very very boring test" )
            counter += 1
          }
        }

      reactions += {
        case WindowActivated( _ ) =>
          timer.scheduleAtFixedRate( task, 0, 500 )
      }
    }


}

//for (i <- 1 to 2) {
//  video.println( i )
//  Thread.sleep( 500 )
//}
