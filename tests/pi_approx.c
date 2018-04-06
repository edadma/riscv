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

char*
double2str( double x )
{
	static char p[35];
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

double
bbp( int iterations ) {
	long den = 1;
	double sum = 0;

	for (int k = 0; k <= iterations - 1; k++) {
		double k8 = 8*k;

		sum += 1.0/den*(4/(k8 + 1) - 2/(k8 + 4) - 1/(k8 + 5) - 1/(k8 + 6));
		den *= 16;
	}

	return sum;
}

void
main() {
	println( double2str(bbp(10)) );
}