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
int2str64( long n, int radix, char* buf ) {
	char digits[] = "0123456789ABCDEF";
	char* p = &buf[33];
	long quo = n;

	if (n < 0)
		quo = -quo;

	*p-- = 0;

	while (quo >= radix) {
		*p-- = digits[(quo%radix)];
		quo /= radix;
	}

	*p = digits[quo];

	if (n < 0)
		*--p = '-';

	return p;
}

void
main() {
	char buf[34];

	println( int2str64(0, 10, buf) );
	println( int2str64(123, 10, buf) );
	println( int2str64(0x12AB, 16, buf) );
	println( int2str64(2000000000, 10, buf) );
	println( int2str64(20000000000, 10, buf) );
	println( int2str64(-123, 10, buf) );
	println( int2str64(-0x12AB, 16, buf) );
	println( int2str64(-2000000000, 10, buf) );
	println( int2str64(-20000000000, 10, buf) );
}