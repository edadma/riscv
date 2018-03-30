package xyz.hyperreal.riscv

import java.io.{ByteArrayOutputStream, PrintStream}

import org.scalatest._
import prop.PropertyChecks


class Tests extends FreeSpec with PropertyChecks with Matchers {

	def capture( code: => Unit ) = {
		val buf = new ByteArrayOutputStream

		Console.withOut( new PrintStream(buf) )( code )
		buf.toString.trim
	}

	val cpu =
		new CPU(
			new Memory {
				def init: Unit = {
					removeDevices
					regions.clear
					removeROM
					add( new StdIOChar(0x20000) )
					add( new RAM("ram", 0, 0xFFFF) )
				}
			} )

	"tests" in {
		cpu.reset
		cpu.memory.init
		cpu.memory.addHexdump( "hello.hex" )

		capture( cpu.run ) shouldBe "Hello world!"
	}
	
}