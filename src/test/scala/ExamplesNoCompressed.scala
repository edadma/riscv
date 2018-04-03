//@
package xyz.hyperreal.riscv

import org.scalatest._
import prop.PropertyChecks


class ExamplesNoCompressed extends FreeSpec with PropertyChecks with Matchers {

	"hello" in {
		Run( "tests/hello.hex" ) shouldBe "Hello world!"
	}

	"signum" in {
		Run( "tests/signum.hex" ) shouldBe
			"""
				|negative
				|zero
				|positive
			""".trim.stripMargin
	}

	"strcmp" in {
		Run( "tests/strcmp.hex" ) shouldBe
			"""
				|zero
				|zero
				|negative
				|positive
				|zero
				|positive
				|negative
				|positive
				|negative
			""".trim.stripMargin
	}

	"str2bin" in {
		Run( "tests/str2bin.hex" ) shouldBe
			"""
				|yes
				|yes
				|yes
				|yes
				|yes
				|yes
				|yes
				|yes
				|yes
			""".trim.stripMargin
	}

	"bin2str" in {
		Run( "tests/bin2str.hex" ) shouldBe
			"""
				|zero
				|zero
				|zero
				|zero
				|zero
			""".trim.stripMargin
	}

	"armstrong" in {
		Run( "tests/armstrong.hex" ) shouldBe
			"""
				|0
				|1
				|153
				|370
				|371
				|407
			""".trim.stripMargin
	}

	"quicksort" in {
		Run( "tests/quicksort.hex" ) shouldBe
			"""
				|[1, 2, 3, 7, 7, 7, 7, 8, 9, 10]
				|[1, 2, 3, 5, 7, 7, 7, 8, 9, 10]
				|[1, 2, 3, 4, 5, 7, 7, 8, 9, 10]
				|[9, 10]
				|[10]
				|[]
			""".trim.stripMargin
	}

	"quicksort64" in {
		Run( "tests/quicksort64.hex" ) shouldBe
			"""
				|[1, 2, 3, 7, 7, 7, 7, 8, 9, 10]
				|[1, 2, 3, 5, 7, 7, 7, 8, 9, 10]
				|[1, 2, 3, 4, 5, 7, 7, 8, 9, 10]
				|[9, 10]
				|[10]
				|[]
			""".trim.stripMargin
	}

	"quicksort16" in {
		Run( "tests/quicksort16.hex" ) shouldBe
			"""
				|[1, 2, 3, 7, 7, 7, 7, 8, 9, 10]
				|[1, 2, 3, 5, 7, 7, 7, 8, 9, 10]
				|[1, 2, 3, 4, 5, 7, 7, 8, 9, 10]
				|[9, 10]
				|[10]
				|[]
			""".trim.stripMargin
	}

	"quicksort8" in {
		Run( "tests/quicksort8.hex" ) shouldBe
			"""
				|[1, 2, 3, 7, 7, 7, 7, 8, 9, 10]
				|[1, 2, 3, 5, 7, 7, 7, 8, 9, 10]
				|[1, 2, 3, 4, 5, 7, 7, 8, 9, 10]
				|[9, 10]
				|[10]
				|[]
			""".trim.stripMargin
	}

	"bittwiddling" in {
		Run( "tests/bittwiddling.hex" ) shouldBe
			"""
				|5, 0, 5
			""".trim.stripMargin
	}

}