#define MAX_BUFFER 1024


/* BB_init sets the size of the circular bounded buffer */
int BB_init(int size);

/* BB_put puts one character in the next open buffer position */
void BB_put(char *s);

/* BB_get returns the next character from the buffer */
void BB_get(char *s);



