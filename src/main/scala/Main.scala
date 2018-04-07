//@
package xyz.hyperreal.riscv

import java.io.PrintWriter

import jline.console.ConsoleReader


object Main extends App {
	
	lazy val mach = new Machine
	var enterREPL = true

	Options( args ) {
		case "--help" :: _ =>
			"""
			|RISC-V (v2.2) Emulator v0.1_snapshot_2
			|Usage:  --help      display this help and exit
			|        -l <file>   load hexdump <file> and enter REPL
			|        -le <file>  load hexdump <file> and execute
			""".trim.stripMargin.lines foreach println
			enterREPL = false
			Nil
		case "-l" :: file :: _ =>
			load( file )
			Nil
		case "-le" :: file :: _ =>
			load( file )
			mach.run
			enterREPL = false
			Nil
		case o :: _ if o startsWith "-" =>
			println( "bad option: " + o )
			enterREPL = false
			Nil
		case _ :: t =>
			t
	}

	if (enterREPL)
		REPL

	def load( file: String ) = mach.load( file )

//	def save( file: String ) = emu.save( file )

	def waitUntilRunning = {
		while (!mach.cpu.isRunning) {}
	}

	def waitWhileRunning = {
		while (mach.cpu.isRunning) {}
	}

	def REPL {
		val reader = new ConsoleReader
		val out = new PrintWriter( reader.getTerminal.wrapOutIfNeeded(System.out), true )
		var line: String = null
		var reload = ""

		reader.setBellEnabled( false )
		reader.setPrompt( "> " )

		mach.reregister( "_stdioInt_",
			(p: String, mem: Memory, cpu: CPU) => {
				mem add new JLineInt( hex(p), reader )
			} )
		mach.reregister( "_stdioHex_",
			(p: String, mem: Memory, cpu: CPU) => {
				mem add new JLineHex( hex(p), reader )
			} )

		def registers = {
			mach.cpu.fetch
			mach.cpu.disassemble
			mach.cpu.registers
		}

		def dump( start: Int, lines: Int ) = out.println( mach.dump(start, lines) )

//		def disassemble( start: Int, lines: Int ) = out.println( mach.disassemble(start, lines) )

//		def printBreakpoints = out.println( mach.breakpoints map {case (b, l) => hexShort(b) + (if (l != "") "/" + l else "")} mkString " " )

		def runAndWait {
			mach.run
			waitUntilRunning
			waitWhileRunning
			registers
		}

		out.println( "RISC-V (v2.2) Emulator v0.1_snapshot_2" )
		out.println( "Type 'help' for list of commands." )
		out.println

		def interp( command: String ) {
			val com = command.trim split "\\s+" toList

			try {
				com match {
//					case List( "breakpoint"|"b" ) =>
//						printBreakpoints
//					case List( "breakpoint"|"b", "--" ) =>
//						mach.clearBreakpoints
//						printBreakpoints
//					case List( "breakpoint"|"b", bp ) if bp startsWith "-" =>
//						mach.clearBreakpoint( mach.target(bp drop 1) )
//						printBreakpoints
//					case List( "breakpoint"|"b", bp ) =>
//						mach.setBreakpoint( mach.target(bp) )
//						printBreakpoints
//					case List( "disassemble"|"u", addr )  =>
//						disassemble( mach.target( addr ), 15 )
//					case List( "disassemble"|"u" )  =>
//						disassemble( -1, 15 )
					case List( "clear"|"c", addr1, addr2 ) =>
						for (i <- hex( addr1 ) until hex( addr2 ))
							mach.mem.programByte( i, 0 )
					case List( "clear"|"c" ) =>
						mach.mem.clearRAM
					case List( "drop"|"dr", region ) =>
						mach.mem.remove( region )
						out.println( mach.mem )
					case List( "dump"|"d", addr ) =>
						dump( mach.target(addr), 10 )
					case List( "dump"|"d" ) =>
						dump( -1, 10 )
					case List( "execute"|"e", addr ) =>
						mach.cpu.pc = mach.target( addr )
						mach.run
					case List( "execute"|"e" ) =>
						mach.run
//					case List( "execute&wait"|"ew", addr ) =>
//						mach.cpu.pc = mach.target( addr )
//						runAndWait
					case List( "execute&wait"|"ew" ) =>
						runAndWait
					case List( "help"|"h" ) =>
						"""
						|breakpoint (b) <addr>*           set/clear breakpoint at <addr>
						|disassemble (u) [<addr>*]        print disassembled code at <addr> or where left off
						|clear (c) [<addr1>* <addr2>*]    clear RAM, optionally from <addr1> up to but not including <addr2>
						|drop (dr) <region>               drop memory <region>
						|dump (d) [<addr>*]               print memory at <addr> or where left off
						|execute (e) [<addr>*]            execute instructions starting from current PC or <addr>
						|execute&wait (ew) [<addr>*]      execute instructions starting from current PC or <addr> and wait to finish
						|help (h)                         print this summary
						|load (l) <file>                  clear ROM, load SREC <file>, and reset CPU
						|memory (m)                       print memory map
						|memory (m) <addr>* <data>*...    write <data> (space separated bytes) to memory at <addr>
						|quit (q)                         exit the REPL
						|registers (r)                    print CPU registers
						|registers (r) <reg> <val>*       set CPU <reg>ister to <val>ue
						|reload (rl)                      redo last 'load' or 'assemble' command
						|reset (re)                       reset CPU registers setting PC from reset vector
						|step (s) [<addr>*]               execute only next instruction at current PC or <addr>
						|stop (st)                        stop code execution
						|save (sa) <file>                 save all ROM contents to SREC file
						|symbols (sy)                     print symbol table
						|symbols (sy) <symbol> <val>*     add <symbol> with associated <val>ue to symbol table
						|trace (t) on/off                 turn CPU trace on or off
						|* can either be a hexadecimal value or label (optionally followed by a colon)
						""".trim.stripMargin.lines foreach out.println
					case List( "load"|"l", file ) =>
						reload = command
						load( file )
					case ("memory"|"m") :: addr :: data =>
						val addr1 = mach.target( addr )

						for ((d, i) <- data map mach.target zipWithIndex)
							mach.program( addr1 + i, d )

						dump( addr1, (data.length + addr1%16)/16 + 1 )
					case List( "memory"|"m" ) =>
						out.println( mach.mem )
					case List( "quit"|"q" ) =>
//						mach.stop
						mach.mem.removeDevices
						sys.exit
//					case List( "registers"|"r", reg, value ) =>
//						val n = mach.target( value )
//
//						reg.toLowerCase match {
//							case "a" => mach.cpu.A = n
//							case "x" => mach.cpu.X = n
//							case "y" => mach.cpu.Y = n
//							case "sp" => mach.cpu.SP = n
//							case "pc" => mach.cpu.PC = n
//							case "n" => mach.cpu.set( N, n )
//							case "v" => mach.cpu.set( V, n )
//							case "b" => mach.cpu.set( B, n )
//							case "d" => mach.cpu.set( D, n )
//							case "i" => mach.cpu.set( I, n )
//							case "z" => mach.cpu.set( Z, n )
//							case "c" => mach.cpu.set( C, n )
//						}
//
//						registers
					case List( "registers"|"r" ) =>
						mach.cpu.registersAll
					case List( "reload"|"rl" ) =>
						interp( reload )
					case List( "reset"|"re" ) =>
						mach.reset
						registers
					case List( "step"|"s", addr ) =>
						mach.cpu.pc = mach.target( addr )
						mach.step
						registers
					case List( "step"|"s" ) =>
						mach.step
						registers
//					case List( "stop"|"st" ) =>
//						mach.stop
//						waitWhileRunning
//						registers
					case List( "symbols"|"sy", symbol, value ) =>
						mach.symbols += (symbol -> mach.target( value ))
					case List( "symbols"|"sy" ) =>
						out.println( "name            value segment" )
						out.println( "----            ----- -------" )
						for ((s, v) <- mach.symbols.toList sortBy (_._1))
							v match {
								case str: String => out.printf( "%-15s %-5s\n", s, '"' + str + '"' )
								case addr: Int =>
									val seg =
										mach.segments get addr match {
											case None =>
												if (mach.segments isEmpty)
													""
												else {
													val range = mach.segments.range( mach.segments.min._1, addr )

													if (range isEmpty)
														""
													else {
														val (base, (name, len)) = range.max

														if (addr < base + len)
															name
														else
															""
													}
												}
											case Some( (name, _) ) => name
										}

									out.printf( "%-15s %-5s %s\n", s, hexShort(addr), seg )
							}
					case List( "trace"|"t", "on" ) =>
						mach.cpu.trace = true
					case List( "trace"|"t", "off" ) =>
						mach.cpu.trace = false
					case Nil|List( "" ) =>
					case _ => out.println( "error interpreting command" )
				}
			}
			catch
			{
				case e: Exception =>
//					out.println( e )
					e.printStackTrace( out )
			}
		}
		
		while ({line = reader.readLine; line != null}) {
			interp( line )
			out.println
		}
	}
}