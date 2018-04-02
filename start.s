.align 6
.globl _start
_start:
	lui	 sp, 0x10
	call main
.globl halt
halt:
	csrrwi x0, 0, 0
