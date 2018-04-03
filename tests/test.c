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

char*
bin2str( char n, int radix, char* buf ) {
	char digits[] = "0123456789ABCDEF";
	char* p = &buf[33];
	char quo = n;

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
printn( char n ) {
	char buf[34];
	char* s = bin2str( n, 16, buf );

	print( s );
}

signed char
myabs( signed char x ) {
  const signed char bit31 = x >> 7;

  return (x ^ bit31) - bit31;
}

void
main() {
	printn( myabs(-5) );
	println();
}