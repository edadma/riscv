void
out( char c ) {
	*((char*) 0x20000) = c;
}

void
main() {
	for (char* p = "Hello world!\n"; *p;)
		out( *p++ );
}