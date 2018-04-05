//#include <stdio.h>
//#define out( c ) putchar( c )

void
out( char c ) {
	*((char*) 0x20000) = c;
}

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

char*
int2str64u( unsigned long n, int radix, char* buf ) {
	char digits[] = "0123456789ABCDEF";
	char* p = &buf[33];
	unsigned long quo = n;

	*p-- = 0;

	while (quo >= radix) {
		*p-- = digits[(quo%radix)];
		quo /= radix;
	}

	*p = digits[quo];

	return p;
}

void
main() {
	char buf[34];

	println( int2str64u(0, 10, buf) );
	println( int2str64u(123, 10, buf) );
	println( int2str64u(0x12AB, 16, buf) );
	println( int2str64u(2000000000, 10, buf) );
	println( int2str64u(20000000000, 10, buf) );
	println( int2str64u(0xF000000000000000, 16, buf) );
}