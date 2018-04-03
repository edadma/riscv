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
printn( signed char n ) {
	char buf[34];
	char* s = bin2str( n, 16, buf );

	print( s );
}

signed char
myabs( signed char x ) {
  const signed char bit31 = x >> 7;

  return (x ^ bit31) - bit31;
}

signed char
modifyBit( signed char x, unsigned char position, int newState ) {
  signed char mask = 1 << position;
  signed char state = newState;

  return (x & ~mask) | (-state & mask);
}

signed char
flipBit( signed char x, unsigned char position ) {
  signed char mask = 1 << position;

  return x ^ mask;
}

signed char
isNegative( signed char n ) {
	return (signed char)((unsigned char) n >> 7);
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