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
bin2str( unsigned long n, int radix, char* buf ) {
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
printn( unsigned long n ) {
	char buf[34];
	char* s = bin2str( n, 16, buf );

	print( s );
}

long
modifyBit( unsigned long x, unsigned char position, unsigned long newState ) {
  unsigned long mask = 1lu << position;
  unsigned long state = newState;

  return (x & ~mask) | (-state & mask);
}

long
flipBit( unsigned long x, unsigned char position ) {
  unsigned long mask = 1lu << position;

  return x ^ mask;
}

int
bit( unsigned long x, int n ) {
	return (x >> n)&1;
}

void
main() {
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

	printn( bit(5, 2) );
	print( ", " );
	printn( bit(5, 1) );
	println();
}