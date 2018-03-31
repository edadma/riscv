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
	static char* signs[3] = {
		"negative",
		"zero",
		"positive"
	};

	print( signs[signum(n) + 1] );
	out( '\n' );
}

void
main() {
	prints( -5 );
	prints( 0 );
	prints( 200 );
}