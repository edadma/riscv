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

	"signum" in {
		Run(
			"""
				|Hex dump of section '.text':
				|  0x000100c0 37010100 ef00c017 73500000 73500000 7.......sP..sP..
				|  0x000100d0 00000000 00000000 00000000 00000000 ................
				|  0x000100e0 00000000 00000000 00000000 00000000 ................
				|  0x000100f0 00000000 00000000 00000000 00000000 ................
				|  0x00010100 130101fe 233c8100 13040102 93070500 ....#<..........
				|  0x00010110 a307f4fe b7070200 0347f4fe 2380e700 .........G..#...
				|  0x00010120 13000000 03348101 13010102 67800000 .....4......g...
				|  0x00010130 130101fd 23341102 23308102 13040103 ....#4..#0......
				|  0x00010140 233ca4fc 833784fd 2334f4fe 6f00c001 #<...7..#4..o...
				|  0x00010150 833784fe 13871700 2334e4fe 83c70700 .7......#4......
				|  0x00010160 13850700 eff0dff9 833784fe 83c70700 .........7......
				|  0x00010170 e39007fe 1305a000 eff09ff8 13000000 ................
				|  0x00010180 83308102 03340102 13010103 67800000 .0...4......g...
				|  0x00010190 130101fe 233c8100 13040102 93070500 ....#<..........
				|  0x000101a0 2326f4fe 8327c4fe 9b870700 63800702 #&...'......c...
				|  0x000101b0 8327c4fe 9b870700 63d60700 9307f0ff .'......c.......
				|  0x000101c0 6f000001 93071000 6f008000 93070000 o.......o.......
				|  0x000101d0 13850700 03348101 13010102 67800000 .....4......g...
				|  0x000101e0 130101fe 233c1100 23388100 13040102 ....#<..#8......
				|  0x000101f0 93070500 2326f4fe 8327c4fe 13850700 ....#&...'......
				|  0x00010200 eff01ff9 93070500 9b871700 1b870700 ................
				|  0x00010210 b7170100 13173700 9387872a b307f700 ......7....*....
				|  0x00010220 83b70700 13850700 eff09ff0 13000000 ................
				|  0x00010230 83308101 03340101 13010102 67800000 .0...4......g...
				|  0x00010240 130101ff 23341100 23308100 13040101 ....#4..#0......
				|  0x00010250 1305b0ff eff0dff8 13050000 eff05ff8 .............._.
				|  0x00010260 1305800c eff0dff7 13000000 83308100 .............0..
				|  0x00010270 03340100 13010101 67800000          .4......g...
				|
				|
				|Hex dump of section '.rodata':
				|  0x00010280 6e656761 74697665 00000000 00000000 negative........
				|  0x00010290 7a65726f 00000000 706f7369 74697665 zero....positive
				|  0x000102a0 00                                  .
				|
				|
				|Hex dump of section '.data':
				|  0x000112a8 80020100 00000000 90020100 00000000 ................
				|  0x000112b8 98020100 00000000                   ........
			""".trim.stripMargin
		) shouldBe
			"""
				|negative
				|zero
				|positive
			""".trim.stripMargin
	}
	
}