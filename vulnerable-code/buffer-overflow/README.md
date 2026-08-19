# Buffer Overflow — Vulnerable Implementation

## Vulnerability

This example demonstrates unsafe handling of user-controlled input in C.

The program allocates:

```c
char buffer[10];
```

and then reads input using:

```c
gets(buffer);
```

`gets()` does not enforce the size of the destination buffer.

Conceptually:

```text
Oversized Input
      │
      ▼
Fixed-Size Buffer
      │
      ▼
Buffer Boundary Exceeded
      │
      ▼
Adjacent Memory May Be Overwritten
```

Potential consequences include:

- Application crashes
- Memory corruption
- Unexpected behavior
- Control-flow corruption in more advanced exploitation scenarios

## Testing

The original coursework assessed the vulnerability using:

- Oversized input
- Manual fuzz testing
- Compiler warnings
- Static analysis
- Runtime monitoring

## Remediation

The corresponding secure implementation replaces the unsafe input function with a bounded operation:

```c
fgets(buffer, sizeof(buffer), stdin);
```

See:

[Secure implementation](../../secure-code/buffer-overflow/secure.c)

## Security Principle

Never copy or read attacker-controlled data into a fixed-size memory region without enforcing the destination boundary.

> This code is intentionally vulnerable and is provided only for secure-coding education and authorized laboratory testing.
