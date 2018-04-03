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
double2str( double x, char p[] )
{
	int i = 0;

	while (((int) x) > 0) {
		x /= 10;
		i++;
	}

	int n;
	int k = 0;

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
}

void
main() {
  char a[20];

  double2str( 3.4, a );
  println( a );
}