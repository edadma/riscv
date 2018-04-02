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

}