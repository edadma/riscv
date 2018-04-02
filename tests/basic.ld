SECTIONS
{
  . = 0x10000;
  .text : { *(.text) }
  .data : { *(.data) }
  . = 0x10000000;
  .bss : { *(.bss) }
}
