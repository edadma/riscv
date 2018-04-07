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

char*
double2str( double x, char p[] )
{
	int i = 0;
	int k = 0;

	if (x < 0) {
		x = -x;
		p[0] = '-';
		i++;
		k++;
	}

	while (((int) x) > 0) {
		x /= 10;
		i++;
	}

	int n;

	p[i] = '.';
	x *= 10;
	n = (int) x;
	x -= n;

	while (n > 0) {
		if (k == i)
			k++;

		p[k] = n + '0';
		x *= 10;
		n = (int) x;
		x = x - n;
		k++;
	}

	p[k] = '\0';
	return p;
}

void
main() {
    double a = 3.4;
    double b = 5.6;
    double c = 7.8;
	int d = 3;

	printb( a + b == 3.4 + 5.6 );
	printb( a - b == 3.4 - 5.6 );
	printb( a < b );
	printb( a <= b );
	printb( a > b );
	printb( a >= b );
	printb( a != b );
	printb( a * b == 3.4 * 5.6 );
	printb( a / b == 3.4 / 5.6 );

	char buf[20];

	println( double2str(3.5, buf) );
	println( double2str(-3.5, buf) );
	printb( (int) a == 3 );
	printb( (short) a == 3 );
	printb( (char) a == 3 );
	printb( (long) a == 3 );
	printb( (double) d == 3.0 );
	printb( (double) (unsigned int) d == 3.0 );
	printb( (double) (short) d == 3.0 );
	printb( (double) (unsigned short) d == 3.0 );
	printb( (double) (long) d == 3.0 );
	printb( (double) (unsigned long) d == 3.0 );
	printb( (double) (char) d == 3.0 );
	printb( (double) (signed char) d == 3.0 );
}