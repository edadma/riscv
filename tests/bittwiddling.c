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
bin2str( int n, int radix, char* buf ) {
	char digits[] = "0123456789ABCDEF";
	char* p = &buf[33];
	int quo = n;

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
printn( int n ) {
	char buf[34];
	char* s = bin2str( n, 16, buf );

	print( s );
}

int
abs( int x ) {
  const int bit31 = x >> 31;

  return (x ^ bit31) - bit31;
}

int
modifyBit( int x, unsigned char position, int newState ) {
  int mask = 1 << position;
  int state = (int) newState;

  return (x & ~mask) | (-state & mask);
}

int
flipBit( int x, unsigned char position ) {
  int mask = 1 << position;

  return x ^ mask;
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
}