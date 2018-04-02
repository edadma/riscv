void
main() {
  for (char* p = "Hello world!\n"; *p;)
    *((char*) 0x20000) = *p++;
}