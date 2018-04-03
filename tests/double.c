void
out( char c ) {
	*((char*) 0x20000) = c;
}

//#include <stdio.h>
//#define out( c ) putchar( c )

void
print( char* s ) {
	while (*s)
		out( *s++ );
}

void
println() {
	out( '\n' );
}

void
printb( int b ) {
	print( b ? "true" : "false" );
	println();
}

void
main() {
	printb( 3.4 + 5.6 == 9.0 );
}