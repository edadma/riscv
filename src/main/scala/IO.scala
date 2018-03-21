package xyz.hyperreal.riscv

import jline.console.ConsoleReader


class StdIOChar( val start: Long ) extends SingleAddressDevice {
	
	val name = "stdio-char"
	
	def readByte( addr: Long ) = io.StdIn.readChar.toInt
	
	def writeByte( addr: Long, value: Int ) = print( value.toChar )
	
}

class StdIOInt( val start: Long ) extends SingleAddressDevice {
	
	val name = "stdio-int"
	
	def readByte( addr: Long ) = io.StdIn.readInt
	
	def writeByte( addr: Long, value: Int ) = print( value )
	
}

class StdIOHex( val start: Long ) extends SingleAddressDevice {
	
	val name = "stdio-hex"
	
	def readByte( addr: Long ) = hex( io.StdIn.readLine )
	
	def writeByte( addr: Long, value: Int ) = print( value.toHexString )
	
}

class JLineInt( val start: Long, reader: ConsoleReader ) extends SingleAddressDevice {
	
	val name = "stdio-int"
	
	def readByte( addr: Long ) = {
		val p = reader.getPrompt
		val res = reader.readLine("").toInt
		
		reader.setPrompt( p )
		res
	}
	
	def writeByte( addr: Long, value: Int ) = print( value )
	
}

class JLineHex( val start: Long, reader: ConsoleReader ) extends SingleAddressDevice {
	
	val name = "stdio-hex"
	
	def readByte( addr: Long ) = {
		val p = reader.getPrompt
		val res = hex( reader.readLine("") )
		
		reader.setPrompt( p )
		res
	}
	
	def writeByte( addr: Long, value: Int ) = print( value.toHexString )
	
}

class RNG( val start: Long ) extends ReadOnlyDevice {
	
	val name = "rng"
	
	def readByte( addr: Long ) = util.Random.nextInt( 0x100 )
	
}

//class VideoRAM( start: Int, keyPress: KeyPress, width: Int, height: Int, square: Int, cpu: CPU, palette: Seq[Int] ) extends RAM( "video", start, start + width*height - 1 ) with Device {
//
//	import javax.swing.BorderFactory._
//	import javax.swing.WindowConstants._
//	import javax.swing.border._
//
//	import scala.swing._
//	import Swing._
//	import scala.swing.event._
//
//	require( start >= 0 )
//	require( width > 0 )
//	require( height > 0 )
//	require( !palette.isEmpty )
//
//	val colors = (palette map {c => new Color(c)}).toArray
//	val panel = new Panel {
//		preferredSize = (width*square, height*square)
//		border = createBevelBorder( BevelBorder.LOWERED )
//
//		if (keyPress ne null) {
//			listenTo( keys )
//			reactions += {
//				case ev: KeyPressed =>
//					keyPress.key = ev.peer.getKeyChar
//				}
//			focusable = true
//			requestFocus
//		}
//
//		override def paintComponent( g: Graphics2D ) {
//			for (x <- 0 until width; y <- 0 until height) {
//				g.setColor( colors(mem(x + y*width)&0x0F) )
//				g.fillRect( x*square, y*square, square, square )
//			}
//		}
//	}
//
//	val frame =
//		new Frame {
//			title = "Video"
//			contents =
//				new BorderPanel {
//					layout(
//						new FlowPanel( FlowPanel.Alignment.Center )(panel)
//					) = BorderPanel.Position.Center
//					layout(
//						new FlowPanel( FlowPanel.Alignment.Center )(
//							new Button( Action("Stop")({
//								cpu.stop
//								println( "stopped from GUI" )
//							}) ) {
//								focusable = false
//							},
//							new Button( Action("Run")({
//								try {
//									cpu.run
//									println( "run from GUI" )
//								} catch {
//									case e: Exception => println( e )
//								}
//							}) ) {
//								focusable = false
//							},
//							new Button( Action("Reset")({
//								try {
//									cpu.reset
//									println( "reset from GUI" )
//								} catch {
//									case e: Exception => println( e )
//								}
//							}) ) {
//								focusable = false
//							} )
//					) = BorderPanel.Position.South
//				}
//
//// 			override def closeOperation {
//// 				cpu.stop
//// 				sys.exit
//// 			}
//
//			pack
//			peer.setDefaultCloseOperation( EXIT_ON_CLOSE )
//		}
//
//	override def init {
//		clear
//		frame.visible = true
//	}
//
//	override def disable {
//		frame.visible = false
//	}
//
//	override def clear = {
//		super.clear
//		panel.repaint
//	}
//
//	override def writeByte( addr: Int, value: Int ) = {
//		super.writeByte( addr, value )
//		panel.repaint
//	}
//
//}
