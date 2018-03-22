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
convert( int n, char* buf ) {
	char* p = buf + 12;
	int quo = n;

	if (n < 0)
		quo = -quo;

	*p-- = 0;

	while (quo >= 10) {
		*p-- = (quo%10) + '0';
		quo /= 10;
	}

	*p = quo + '0';

	if (n < 0)
		*--p = '-';

	return p;
}

void
main() {
	char buf[20];

	print( convert(-123, buf) );
}