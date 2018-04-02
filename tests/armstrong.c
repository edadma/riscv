void
out( char c ) {
	*((char*) 0x20000) = c;
}

//#include <stdio.h>
//
//void
//out( char c ) {
//	putchar( c );
//}

void
print( char* s ) {
	while (*s)
		out( *s++ );

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

char*
armstrong( int n ) {
	static char buf[34];
	char* s = bin2str( n, 10, buf );
	int sum = 0;

	for (char* p = s; *p;) {
		int d = *p++ - '0';

		sum += d*d*d;
	}

	return sum == n ? s : (char*) 0;
}

void
main() {
	for (int n = 0; n <= 999; n++) {
		char* p = armstrong( n );

		if (p)
			print( p );
	}
}