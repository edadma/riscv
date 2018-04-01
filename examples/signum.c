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

void
main() {
	prints( -5 );
	prints( 0 );
	prints( 200 );
}