/*
 * Buffer Overflow Demonstration
 *
 * INTENTIONALLY VULNERABLE CODE
 *
 * This program is included for secure-coding education and
 * controlled laboratory testing only.
 *
 * Vulnerability:
 *   Unbounded input is written into a fixed-size stack buffer.
 */

#include <stdio.h>
#include <string.h>

void vulnerable(void)
{
    char buffer[10];

    printf("Enter input: ");

    /*
     * UNSAFE:
     * gets() performs no bounds checking.
     *
     * Input larger than the destination buffer can overwrite
     * adjacent stack memory.
     */
    gets(buffer);

    printf("You entered: %s\n", buffer);
}

int main(void)
{
    vulnerable();
    return 0;
}
