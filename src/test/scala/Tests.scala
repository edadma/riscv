//@
package xyz.hyperreal.riscv

import java.io.File

import scala.sys.process._


object Tests extends App {

	val dir = "tests"

	for (t <- new File( dir ).list.toList filter (_ endsWith ".c") map (_ dropRight 2) map (dir + '/' + _)) {
		println( s"building $t..." )
		s"riscv64-unknown-elf-gcc -Wl,-Tbss,1000000 -nostdlib -nostartfiles -march=rv64g -o $t start.s $t.c".!
		(s"riscv64-unknown-elf-readelf -R .text -x .rodata -x .data $t" #> new File( s"$t.ghex" )).!
		s"riscv64-unknown-elf-gcc -Wl,-Tbss,1000000 -nostdlib -nostartfiles -o $t start.s $t.c".!
		(s"riscv64-unknown-elf-readelf -R .text -x .rodata -x .data $t" #> new File( s"$t.hex" )).!
		new File( t ).delete
	}

}