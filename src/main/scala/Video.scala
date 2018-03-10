package xyz.hyperreal.riscv

import java.awt.Color

import scala.swing.{Graphics2D, MainFrame, Panel, SimpleSwingApplication}
import scala.swing.Swing._


class Video( columns: Int, rows: Int, font: BDF ) extends Panel {

  preferredSize = (columns*font.width, rows*font.height)
  background = Color.BLACK

  case class Cell( char: Char, color: Color )

  private val array = Array.fill[Cell]( columns, rows )( Cell(' ', Color.GRAY) )
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

  private def write( c: Char ): Unit = {
    c match {
      case '\n' =>
        curx = 0
        cury = (cury + 1)%rows
      case _ =>
        array( curx )( cury ) = Cell( c, curc )
        curx = (curx + 1)%columns

        if (curx == 0)
          cury = (cury + 1)%rows
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

    for (j <- 0 until rows; i <- 0 until columns) {
      val Cell( char, color ) = array(i)(j)

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
      pack
    }

}