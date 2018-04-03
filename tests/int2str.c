void
out( char c ) {
	*((char*) 0x20000) = c;
}

void
print( char* s ) {
	while (*s)
		out( *s++ );

	out( '\n' );
}

int
signum( int n ) {
	return n == 0 ? 0 : n < 0 ? -1 : 1;
}

void
prints( int n ) {
	static char* signs[] = {
		"negative",
		"zero",
		"positive"
	};

	print( signs[signum(n) + 1] );
}

char*
int2str( int n, int radix, char* buf ) {
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

int
strcmp( char *s1, char *s2 ) {
	unsigned char c1, c2;

	do {
		c1 = (unsigned char) *s1++;
		c2 = (unsigned char) *s2++;

		if (c1 == '\0')
			return c1 - c2;
	} while (c1 == c2);

	return c1 - c2;
}

void
main() {
	char buf[34];

	prints( strcmp(int2str(0, 10, buf), "0") );
	prints( strcmp(int2str(123, 10, buf), "123") );
	prints( strcmp(int2str(0x12AB, 16, buf), "12AB") );
	prints( strcmp(int2str(-123, 10, buf), "-123") );
	prints( strcmp(int2str(-0x12AB, 16, buf), "-12AB") );
}