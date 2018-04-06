//@
package xyz.hyperreal.riscv

import org.scalatest._
import prop.PropertyChecks


class ExamplesNormal extends FreeSpec with PropertyChecks with Matchers {

	"hello" in {
		Run( "tests/hello.ghex" ) shouldBe "Hello world!"
	}

	"pi_approx" in {
		Run( "tests/pi_approx.ghex" ) shouldBe "3.141592653589791339641124"
	}

	"signum" in {
		Run( "tests/signum.ghex" ) shouldBe
			"""
				|negative
				|zero
				|positive
			""".trim.stripMargin
	}

	"strcmp" in {
		Run( "tests/strcmp.ghex" ) shouldBe
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

	"str2int" in {
		Run( "tests/str2int.ghex" ) shouldBe
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

	"int2str" in {
		Run( "tests/int2str.ghex" ) shouldBe
			"""
				|zero
				|zero
				|zero
				|zero
				|zero
			""".trim.stripMargin
	}

	"int2stru" in {
		Run( "tests/int2stru.ghex" ) shouldBe
			"""
				|zero
				|zero
				|zero
				|zero
			""".trim.stripMargin
	}

	"int2str64" in {
		Run( "tests/int2str64.ghex" ) shouldBe
			"""
				|0
				|123
				|12AB
				|2000000000
				|20000000000
				|-123
				|-12AB
				|-2000000000
				|-20000000000
			""".trim.stripMargin
	}

	"int2str64u" in {
		Run( "tests/int2str64u.ghex" ) shouldBe
			"""
				|0
				|123
				|12AB
				|2000000000
				|20000000000
				|F000000000000000
			""".trim.stripMargin
	}

	"armstrong" in {
		Run( "tests/armstrong.ghex" ) shouldBe
			"""
				|0
				|1
				|153
				|370
				|371
				|407
			""".trim.stripMargin
	}

	"armstrong64" in {
		Run( "tests/armstrong64.ghex" ) shouldBe
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
		Run( "tests/quicksort.ghex" ) shouldBe
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
		Run( "tests/quicksort64.ghex" ) shouldBe
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
		Run( "tests/quicksort16.ghex" ) shouldBe
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
		Run( "tests/quicksort8.ghex" ) shouldBe
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
		Run( "tests/bittwiddling.ghex" ) shouldBe
			"""
				|5, 0, 5
				|0, 20, 57, 77
				|20, 57
				|0, 1
				|1, 0
			""".trim.stripMargin
	}

	"bittwiddling8" in {
		Run( "tests/bittwiddling8.ghex" ) shouldBe
			"""
				|5, 0, 5
				|0, 20, 57, 77
				|20, 57
				|0, 1
			""".trim.stripMargin
	}

	"bittwiddling16" in {
		Run( "tests/bittwiddling16.ghex" ) shouldBe
			"""
				|5, 0, 5
				|0, 20, 57, 77
				|20, 57
				|0, 1
			""".trim.stripMargin
	}

	"bittwiddling64" in {
		Run( "tests/bittwiddling64.ghex" ) shouldBe
			"""
				|5, 0, 5
				|0, 20, 57, 77
				|20, 57
				|0, 1
				|1, 0
			""".trim.stripMargin
	}

	"bittwiddling64u" in {
		Run( "tests/bittwiddling64u.ghex" ) shouldBe
			"""
				|0, 20, 57, 77
				|20, 57
				|1, 0
			""".trim.stripMargin
	}

	"float64" in {
		Run( "tests/float64.ghex" ) shouldBe
			"""
				|true
				|true
				|true
				|true
				|false
				|false
				|true
				|true
				|true
				|3.5
				|-3.5
			""".trim.stripMargin
	}

}