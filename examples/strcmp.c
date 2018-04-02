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

	prints( strcmp("", "") );
	prints( strcmp("a", "a") );
	prints( strcmp("", "abc") );
	prints( strcmp("abc", "") );
	prints( strcmp("abc", "abc") );
	prints( strcmp("abc", "ab") );
	prints( strcmp("ab", "abc") );
	prints( strcmp("bc", "abc") );
	prints( strcmp("abc", "bc") );
}