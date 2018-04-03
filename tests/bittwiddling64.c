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
bin2str( long n, int radix, char* buf ) {
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
printn( long n ) {
	char buf[34];
	char* s = bin2str( n, 16, buf );

	print( s );
}

long
abs( long x ) {
  const long bit31 = x >> 63;

  return (x ^ bit31) - bit31;
}

long
modifyBit( long x, unsigned char position, int newState ) {
  long mask = 1 << position;
  long state = newState;

  return (x & ~mask) | (-state & mask);
}

long
flipBit( long x, unsigned char position ) {
  long mask = 1 << position;

  return x ^ mask;
}

long
isNegative( long n ) {
	return (long)((unsigned long) n >> 63);
}

void
main() {
	printn( abs(5) );
	print( ", " );
	printn( abs(0) );
	print( ", " );
	printn( abs(-5) );
	println();

	printn( modifyBit(0, 5, 0) );//0
	print( ", " );
	printn( modifyBit(0, 5, 1) );//0x20
	print( ", " );
	printn( modifyBit(0x77, 5, 0) );//0x57
	print( ", " );
	printn( modifyBit(0x77, 5, 1) );//0x77
	println();

	printn( flipBit(0, 5) );//20
	print( ", " );
	printn( flipBit(0x77, 5) );//57
	println();

	printn( isNegative(0) );
	print( ", " );
	printn( isNegative(-5) );
	println();
}