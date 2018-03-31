void
out( char c ) {
	*((char*) 0x20000) = c;
}

void
print( char* s ) {
	for (char* p = s; *p;)
		out( *p++ );

	out( '\n' );
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

	if (strcmp("abc", "abc") == 0)
}