void
out( char c ) {
	*((char*) 0x20000) = c;
}

void
main() {
	for (char* p = "Hello\n"; *p;)
		out( *p++ );
}