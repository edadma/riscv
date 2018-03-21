void
out( char c ) {
	*((char*) 0x20000) = c;
}

void
main() {
	out( 'a' );
}