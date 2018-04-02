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
sort( short a[], int left, int right ) {
  if (right > left) {
	int i = left;
	int j = right;
	short tmp = 0;
	short p = a[right];

	do {
	  while (a[i] < p)
		i++;

	  while (a[j] > p)
		j--;

	  if (i <= j) {
		tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
		i++;
		j--;
	  }
	} while (i <= j);

	if (left < j)
	  sort( a, left, j );

	if (i < right)
	  sort( a, i, right );
  }
}

void
run( short array[], int size ) {
	sort( array, 0, size - 1 );
	print( "[" );

	for (int i = 0; i < size; i++) {
		char buf[34];
		char* s = bin2str( array[i], 10, buf );

		print( s );

		if (i < size - 1)
			print( ", " );
	}

	println( "]" );
}

void
main() {
	run( (short[]){10, 9, 8, 7, 7, 7, 7, 3, 2, 1}, 10 );
	run( (short[]){10, 9, 8, 7, 7, 5, 7, 3, 2, 1}, 10 );
	run( (short[]){10, 9, 8, 7, 5, 7, 4, 3, 2, 1}, 10 );
	run( (short[]){10, 9}, 2 );
	run( (short[]){10}, 1 );
	run( (short[]){}, 0 );
}