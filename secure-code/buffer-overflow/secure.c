/*
 * Buffer Overflow Remediation
 *
 * SECURE VERSION
 *
 * The vulnerable unbounded input operation has been replaced
 * with a bounded input function.
 */

#include <stdio.h>
#include <string.h>

#define BUFFER_SIZE 10

int main(void)
{
    char buffer[BUFFER_SIZE];

    printf("Enter input: ");

    /*
     * SAFER:
     * fgets() limits the number of bytes written to the buffer.
     */
    if (fgets(buffer, sizeof(buffer), stdin) == NULL)
    {
        fprintf(stderr, "Input error.\n");
        return 1;
    }

    /*
     * Remove the trailing newline when present.
     */
    buffer[strcspn(buffer, "\n")] = '\0';

    printf("You entered: %s\n", buffer);

    return 0;
}
