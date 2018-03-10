package xyz.hyperreal.riscv


abstract class Instruction {

  def execute( cpu: CPU ): Boolean

}

