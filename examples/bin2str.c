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

int
strcmp( char *s1, char *s2 )
{
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

	if (bin2str(0x12AB, 16, buf) == 0)
}