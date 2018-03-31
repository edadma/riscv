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
indexOf( char c, char* s ) {
	char* p = s;
	char ch;

	for (int i = 0; ch = *p++; i++)
		if (ch == c)
			return i;

	return -1;
}

int
str2bin( char* n, int radix ) {
	char digits[] = "0123456789ABCDEF";
	char* p = n;
	int result = 0;
	char c;
	int neg = 0;

	if (*p == '-') {
		neg = 1;
		p++;
	}

	while (c = *p++) {
		int idx = indexOf( c, digits );

		if (idx == -1 || idx >= radix) {
			print( "invalid number" );
			halt();
		}

		result = result*radix + idx;
	}

	if (neg)
		result = -result;

	return result;
}

void
main() {
	int n = str2bin( "123", 10 );

	if (n == 123)
		print( "yes" );
	else
		print( "no" );
}