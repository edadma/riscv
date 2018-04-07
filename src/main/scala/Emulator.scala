package xyz.hyperreal.riscv

import scala.collection.immutable.TreeMap
import scala.collection.mutable.HashMap


class Emulator {
	
	val mem =
		new Memory {
			def init {
				removeDevices
				regions.clear
				add( new StdIOChar(0x20000) )
				add( new RAM("stack", 0, 0xFFFF) )
				add( new RAM("bss", 0x1000000, 0x100FFFF) )
				add( new ROM("program", 0x10000, 0x1FFFF) )
			}
		}
	val cpu = new CPU( mem )

	private val registry = new HashMap[String, (String, Memory, CPU) => Unit]
	
	register( "_stdioChar_", (p: String, mem: Memory, cpu: CPU) => mem add new StdIOChar( hex(p) ) )
	register( "_stdioInt_", (p: String, mem: Memory, cpu: CPU) => mem add new StdIOInt( hex(p) ) )
	register( "_stdioHex_", (p: String, mem: Memory, cpu: CPU) => mem add new StdIOHex( hex(p) ) )
	register( "_rng_", (p: String, mem: Memory, cpu: CPU) => mem add new RNG( hex(p) ) )
//	register( "_video_",
//		(p: String, mem: Memory, cpu: CPU) => {
//			val parms = p split ","
//			val kp = hex(parms(1))
//			val kpd = if (kp == 0) null else new KeyPress( kp )
//
//			if (kpd ne null)
//				mem add kpd
//
//			mem add new VideoRAM( hex(parms(0)), kpd, hex(parms(2)), hex(parms(3)), hex(parms(4)), cpu, (for (i <- 5 to 20) yield hex(parms(i))).toIndexedSeq )
//		} )
	register( "_ram_",
		(p: String, mem: Memory, cpu: CPU) => {
			mem.removeRAM
			
			val block = """(\p{XDigit}+)\-(\p{XDigit}+)"""r
			
			for ((m, ind) <- block findAllMatchIn p zipWithIndex)
				mem add new RAM( "main" + ind, hex(m group 1), hex(m group 2) )
		}	)
	register( "_rom_",
		(p: String, mem: Memory, cpu: CPU) => {
			mem.removeROM
			
			val block = """(\p{XDigit}+)\-(\p{XDigit}+)"""r
			
			for ((m, ind) <- block findAllMatchIn p zipWithIndex)
				mem add new ROM( "main" + ind, hex(m group 1), hex(m group 2) )
		}	)
	
	var dumpcur: Long = 0
	var discur: Long = 0
	var symbols = Map[String, Any]()
	var reverseSymbols = Map[Any, String]()
	var segments = TreeMap[Int, (String, Int)]()
	
	def register( name: String, installer: (String, Memory, CPU) => Unit ) {
		if (registry contains name)
			sys.error( "device installer already registered: " + name )
			
		registry(name) = installer
	}
	
	def deregister( name: String ) {
		if (!(registry contains name))
			sys.error( "device installer not registered: " + name )
			
		registry -= name
	}

	def reregister( name: String, installer: (String, Memory, CPU) => Unit ) {
		if (!(registry contains name))
			sys.error( "device installer not registered: " + name )

		registry(name) = installer
	}

	def run = cpu.run
	
	def reset = {
		cpu.reset
		discur = mem.code
	}
	
	def step = cpu.step
	
//	def stop = cpu.stop
	
	def readByte( addr: Long ) = mem.readByte( addr )
	
	def readWord( addr: Long ) = mem.readShort( addr )
	
	def program( addr: Long, b: Int ) = mem.programByte( addr, b )

	def display( label: String ) =
		label indexOf '.' match {
			case -1 => label
			case dot => label substring dot
		}

//	def reference( target: Int, zp: Boolean ) =
//		reverseSymbols get target match {
//			case None => "$" + (if (zp) hexByte( target ) else hexWord( target ))
//			case Some( l ) => display( l )
//		}

	def target( ref: String ) =
		if (isHex( ref ))
			hex( ref )
		else
			-1
//			symbols get (if (ref endsWith ":") ref dropRight 1 else ref) match {
//				case Some( t: Int ) => t
//				case None => sys.error( "unknown label: " + ref )
//				case Some( s ) => sys.error( "symbol not an integer: " + s )
//			}

//	def disassemble( start: Long, lines: Int ): String = {
//		val buf = new StringBuilder
//		var addr =
//			if (start == -1)
//				discur
//			else
//				start
//
//		for (_ <- 1 to lines) {
//			if (!mem.memory( addr ))
//				return buf.toString
//
//			val opcode = mem.readByte( addr )
//
//			val label =
//				(reverseSymbols get addr match {
//					case None => ""
//					case Some( l ) => display( l )
//				})
//
////			if (cpu.breakpoints( addr ))
////				buf append Console.CYAN_B
//
//			buf append( hexLong(addr) + "  " + hexByte(opcode) + " " )
//			addr += 1
//
//
//					for (i <- 0 until size)
//						buf append( hexByte(cpu.readByte(addr + i)) + " " )
//
//					addr += size
//
//					buf append( " "*((2 - size)*3 + 2) )
//					buf append( label + " "*(15 - label.length + 1) )
//					buf append( mnemonic.toUpperCase + " " )
//					buf append( display )
//
//			buf append( Console.RESET )
//			buf += '\n'
//		}
//
//		discur = addr
//		buf.toString dropRight 1
//	}

	def load( file: String ) {
		if (cpu.isRunning)
			sys.error( "can't load while running" )

		mem.removeROM
		mem.reset
		mem.addHexdump( io.Source.fromFile(file) )
		discur = mem.code
//		clearBreakpoints
		reset
	}

//	def save( file: String ) = 	SREC.write( mem, new File(file), file.getBytes.toVector )
	
	def dump( start: Long, lines: Int ) = {
		val buf = new StringBuilder
		val addr =
			if (start == -1)
				dumpcur - dumpcur%16
			else
				start - start%16
		
		def printByte( b: Option[Int] ) =
			if (b isEmpty)
				buf.append( "-- " )
			else
				buf.append( "%02x ".format(b.get&0xFF).toUpperCase )
		
		def printChar( c: Option[Int] ) = buf.append( if (c.nonEmpty && ' ' <= c.get && c.get <= '~') c.get.asInstanceOf[Char] else '.' )
		
		def read( addr: Long ) =
			if (mem.addressable( addr ) && mem.memory( addr ))
				Some( mem.readByte(addr) )
			else
				None
		
		for (line <- addr until ((addr + 16*lines) min 0x10000) by 16) {
			buf.append( "%8x  ".format(line).toUpperCase )
			
			for (i <- line until ((line + 16) min 0x10000)) {
				if (i%16 == 8)
					buf.append( ' ' )
					
				printByte( read(i) )
			}
			
			val bytes = ((line + 16) min 0x10000) - line
			
			buf.append( " "*(((16 - bytes)*3 + 1 + (if (bytes < 9) 1 else 0)).toInt) )
			
			for (i <- line until ((line + 16) min 0x10000))
				printChar( read(i) )
				
			buf += '\n'
		}
		
		dumpcur = (addr + 16*8) min 0x10000
		buf.toString dropRight 1
	}
	
//	def clearBreakpoints = cpu.breakpoints = Set[Int]()
//
//	def setBreakpoint( addr: Int ) = cpu.breakpoints += addr
//
//	def clearBreakpoint( addr: Int ) = cpu.breakpoints -= addr
//
//	def breakpoints = cpu.breakpoints.toList map (b => (b, (if (reverseSymbols contains b) reverseSymbols(b) else "")))
}