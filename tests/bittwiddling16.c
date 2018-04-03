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
bin2str( short n, int radix, char* buf ) {
	char digits[] = "0123456789ABCDEF";
	char* p = &buf[33];
	short quo = n;

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
printn( short n ) {
	char buf[34];
	char* s = bin2str( n, 16, buf );

	print( s );
}

short
myabs( short x ) {
  const short bit31 = x >> 15;

  return (x ^ bit31) - bit31;
}

short
modifyBit( short x, unsigned char position, int newState ) {
  short mask = 1 << position;
  short state = newState;

  return (x & ~mask) | (-state & mask);
}

short
flipBit( short x, unsigned char position ) {
  short mask = 1 << position;

  return x ^ mask;
}

short
isNegative( short n ) {
	return (short)((unsigned short) n >> 15);
}

void
main() {
	printn( myabs(5) );
	print( ", " );
	printn( myabs(0) );
	print( ", " );
	printn( myabs(-5) );
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