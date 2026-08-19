# SQL Injection — Vulnerable vs Secure Query Handling

## Overview

This case study demonstrates how dynamically constructing SQL statements from user-controlled input can introduce SQL injection vulnerabilities.

The insecure implementation combines:

```text
SQL Syntax
    +
User Input
```

inside the same query string.

Conceptually:

```text
User Input
    │
    ▼
String Concatenation
    │
    ▼
SQL Query
    │
    ▼
Database
```

If attacker-controlled input can modify the structure of the query, application logic may be altered.

---

## Vulnerable Pattern

The vulnerable implementation uses:

```java
String query =
    "SELECT * FROM users WHERE username='"
    + username
    + "' AND password='"
    + password
    + "'";
```

The application cannot reliably distinguish between:

```text
Application SQL
```

and:

```text
User-Controlled Data
```

because both become part of the same SQL statement.

---

## Potential Impact

SQL injection can potentially result in:

- Authentication bypass
- Unauthorized database access
- Sensitive-data disclosure
- Data modification
- Database enumeration
- Destructive database operations

The actual impact depends on the permissions assigned to the application's database account.

---

## Secure Implementation

The remediated version uses:

```java
PreparedStatement
```

with placeholders:

```java
"SELECT 1 FROM users WHERE username = ? AND password = ?"
```

and binds the values separately:

```java
statement.setString(1, username);
statement.setString(2, password);
```

Conceptually:

```text
SQL Structure
      │
      ├─────────────┐
      │             │
      ▼             ▼
Prepared Query   User Data
      │             │
      └──────┬──────┘
             ▼
          Database
```

The database receives a predefined SQL structure with separately bound values.

---

## Why Parameterization Works

Parameterized queries prevent user-controlled values from being interpreted as part of the SQL statement's executable structure.

Instead:

```text
Input
  │
  ▼
Bound Parameter
  │
  ▼
Treated as Data
```

This addresses the root cause rather than attempting to block individual malicious strings.

---

## Re-Testing

The original coursework repeated injection testing after remediation.

The expected result is:

```text
Injection Attempt
       │
       ▼
PreparedStatement
       │
       ▼
Input Treated as Data
       │
       ▼
Query Logic Unchanged
       │
       ▼
Authentication Bypass Fails
```

Successful re-testing demonstrates that the vulnerability condition has been removed while legitimate application behavior remains available.

---

## Additional Defensive Controls

Parameterized queries are the primary control for this vulnerability.

Additional production controls include:

- Least-privileged database accounts
- Input validation
- Secure error handling
- Database activity monitoring
- Application security testing
- Secure code review
- Appropriate web application firewall rules

These controls complement parameterization rather than replace it.

---

## Authentication Design Note

The simplified coursework example compares a supplied password within a database query.

A production authentication system should normally use:

```text
Password
   │
   ▼
Strong Password Hashing
   │
   ▼
Stored Password Hash
```

using an appropriate password-hashing algorithm and secure authentication design.

This is a production recommendation rather than a claim about the original demonstration implementation.

---

## Files

### Vulnerable

[`VulnerableLogin.java`](VulnerableLogin.java)

### Secure

[`SecureLogin.java`](../../secure-code/sql-injection/SecureLogin.java)

---

## Security Principle

> Never construct executable SQL statements by concatenating untrusted input.

Keep SQL structure and user-controlled values separate through parameterized queries.

---

> The vulnerable implementation is intentionally insecure and is provided solely for secure-coding education and authorized laboratory testing.
