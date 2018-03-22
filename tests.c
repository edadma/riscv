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
convert( int n, int radix, char* buf ) {
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

//int
//bin( char* n, int radix ) {
//	char digits[] = "0123456789ABCDEF";
//	char* p = n;
//	int result = 0;
//	char c;
//
//	while (c = *p++) {
//
//	}
//
//	return result;
//}

int
indexOf( char c, char* s ) {
	char* p = s;
	char ch;

	for (int i = 0; ch = *p++; i++)
		if (ch == c)
			return i;

	return -1;
}

void
main() {
	char buf[34];	// sign + 32 bits + 0
	char s[] = "This is a test.";

	print( convert(indexOf('x', s), 10, buf) );
}