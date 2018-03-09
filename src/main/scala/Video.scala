package xyz.hyperreal.riscv

import javax.swing.JPanel
import java.awt.Color


class Video( columns: Int, rows: Int ) {

  private val array = Array.fill[Cell]( columns, rows )( Cell(' ', Color.GRAY) )

  case class Cell( char: Char, color: Color )
}