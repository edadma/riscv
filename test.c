#include <stdio.h>


int
main( char** args ) {

	for (char* p = "Hello\n"; *p;)
		putchar( *p++ );

}