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

void
main() {
	print( "asdf" );
}