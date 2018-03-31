package xyz.hyperreal.riscv

import org.scalatest._
import prop.PropertyChecks


class ExamplesNoCompressed extends FreeSpec with PropertyChecks with Matchers {

	/*
      void
      out( char c ) {
        *((char*) 0x20000) = c;
      }

      void
      main() {
        for (char* p = "Hello world!\n"; *p;)
        out( *p++ );
      }
  */
	"hello" in {
		Run(
			"""
				|Hex dump of section '.text':
				|  0x00010080 37010100 ef00c006 73500000 00000000 7.......sP......
				|  0x00010090 00000000 00000000 00000000 00000000 ................
				|  0x000100a0 00000000 00000000 00000000 00000000 ................
				|  0x000100b0 00000000 00000000 00000000 00000000 ................
				|  0x000100c0 130101fe 233c8100 13040102 93070500 ....#<..........
				|  0x000100d0 a307f4fe b7070200 0347f4fe 2380e700 .........G..#...
				|  0x000100e0 13000000 03348101 13010102 67800000 .....4......g...
				|  0x000100f0 130101fe 233c1100 23388100 13040102 ....#<..#8......
				|  0x00010100 b7070100 93878714 2334f4fe 6f00c001 ........#4..o...
				|  0x00010110 833784fe 13871700 2334e4fe 83c70700 .7......#4......
				|  0x00010120 13850700 eff0dff9 833784fe 83c70700 .........7......
				|  0x00010130 e39007fe 13000000 83308101 03340101 .........0...4..
				|  0x00010140 13010102 67800000                   ....g...
				|
				|
				|Hex dump of section '.rodata':
				|  0x00010148 48656c6c 6f20776f 726c6421 0a00     Hello world!..
			""".trim.stripMargin
		) shouldBe "Hello world!"
	}
	
}