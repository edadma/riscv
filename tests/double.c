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
println( char* s ) {
	print( s );
	out( '\n' );
}

void
printb( int b ) {
	println( b ? "true" : "false" );
}

void
main() {
    double a = 3.4;
    double b = 5.6;
    double c = 7.8;

	printb( a + b == 9.0 );
//	printb( 3.4 - 5.6 == -2.2 );
//	printb( 3.4 * 5.6 == 19.04 );
//	printb( 3.4 / 5.6 == 0.607142857 );
}