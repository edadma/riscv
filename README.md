riscv
=====
[![Build Status](https://www.travis-ci.org/edadma/riscv.svg?branch=master)](https://www.travis-ci.org/edadma/riscv)
[![Coverage Status](https://coveralls.io/repos/github/edadma/riscv/badge.svg?branch=master)](https://coveralls.io/github/edadma/riscv?branch=master)
[![License](https://img.shields.io/badge/license-ISC-blue.svg)](https://opensource.org/licenses/ISC)
[![Version](https://img.shields.io/badge/latest_release-0.1_snapshot_2-orange.svg)](https://www.scala-sbt.org/)

*riscv* is an emulator for the RISC-V ISA (v2.2).  Specifically, RV64ID is currently being emulated (with a few missing instructions).  Emulation for the compressed instruction sets (RV32C, RV64C) is being worked on.  The goal is for RV64GC to be fully supported.


Usage
-----

### Library

Use the following definition to use *riscv* in your Maven project:

	<repository>
	  <id>hyperreal</id>
	  <url>https://dl.bintray.com/edadma/maven</url>
	</repository>

	<dependency>
	  <groupId>xyz.hyperreal</groupId>
	  <artifactId>riscv</artifactId>
	  <version>0.1_snapshot_2</version>
	</dependency>

Add the following to your `build.sbt` file to use *riscv* in your SBT project:

	resolvers += "Hyperreal Repository" at "https://dl.bintray.com/edadma/maven"

	libraryDependencies += "xyz.hyperreal" %% "riscv" % "0.1_snapshot_2"

### Executable

There is a command-line style interface to the emulator which can be used to execute programs. The following requirements need to be met.

- Java 8
- SBT (Simple Build Tool) 1.1.1+

The execute the following

	git clone git://github.com/edadma/riscv.git
	cd riscv
	sbt run


Obligatory "Hello World" Example
--------------------------------

This example assumes that the complete RISC-V toolchain has been built and that the various commands that are provided by the toolchain are on the path.


### Build the C Executable to be Emulated

To run the example, you need to have the `hello.hex` hexdump (executable) file that is to be loaded into emulated ROM. To that end, place the following C "hello world" program into a text file called `hello.c`

	void
	main() {
	  for (char* p = "Hello world!\n"; *p;)
	    *((char*) 0x20000) = *p++;
	}

and place the following assembly start-up code into a text file called `start.s`.

	.align 6
	.globl _start
	_start:
	  lui sp, 0x10
	  call main
	.globl halt
	halt:
	  csrrwi x0, 0, 0

Now enter the command

	riscv64-unknown-elf-gcc -nostdlib -nostartfiles -march=rv64g -o hello start.s hello.c

to compile the little "hello world" program and assemble the start code producing an ELF file called `hello`.  The emulator will have the ability to load ELF files eventually, but for now it loads hexdump files.  So, enter

	riscv64-unknown-elf-readelf -R .text -x .rodata -x .data hello >hello.hex

to get `hello.hex`.


### Using the Emulator as a Library

The following Scala program builds a small emulated RISC-V computer with a "hello world" program in ROM, 64k of RAM (for the stack) and a character output device memory mapped at address 0x20000.

	import xyz.hyperreal.riscv._

	object Example extends App {
	  val cpu =
	    new CPU(
	      new Memory {
	        def init: Unit = {
	          regions.clear
	          add( new StdIOChar(0x20000) )
	          add( new RAM("stack", 0, 0xFFFF) )
	          addHexdump( io.Source.fromFile("hello.hex") )
	        }
	      } )

	  cpu.reset
	  cpu.run
	}

It should print

	Hello world!


### Using the Emulator as a Library

Begin by following the **Usage** instructions. At the emulator command-line, enter

	l hello.hex
	e

It should print

	Hello world!


Building
--------

### Requirements

- Java 8
- SBT 1.1.1+


### Clone and Run the Tests

	git clone git://github.com/edadma/riscv.git
	cd riscv
	sbt test


License
-------

ISC © 2018 Edward Maxedon