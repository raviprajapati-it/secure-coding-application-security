# Secure Coding & Application Security — Vulnerable vs Remediated Implementations

Application-security portfolio demonstrating **vulnerability analysis, insecure coding patterns, secure remediation, and re-testing** across five common software-security weaknesses.

This repository contains paired vulnerable and secure implementations for:

1. **Buffer Overflow**
2. **SQL Injection**
3. **Cross-Site Scripting (XSS)**
4. **Hardcoded Credentials**
5. **Insecure Deserialization**

The project is based on secure-coding coursework in which each vulnerability was:

```text
Identified
    ↓
Analyzed
    ↓
Tested
    ↓
Remediated
    ↓
Re-Tested
```

> The intentionally vulnerable examples are included for secure-development education and authorized laboratory testing only.

---

## Project Objective

Application security is not only about finding vulnerabilities.

A complete secure-development workflow also requires understanding:

- Why the vulnerability exists
- Which trust boundary failed
- How the weakness can affect confidentiality, integrity, or availability
- Which coding practice addresses the actual root cause
- Whether the remediation still preserves legitimate functionality
- Whether the original attack condition can still be reproduced

The central methodology used throughout this repository is:

```text
Vulnerable Code
      │
      ▼
Security Analysis
      │
      ▼
Controlled Validation
      │
      ▼
Secure Implementation
      │
      ▼
Re-Testing
      │
      ▼
Remediation Confirmed
```

---

# Case Study Summary

| Case | Vulnerability | Language | Vulnerable Pattern | Primary Remediation |
| ---: | --- | --- | --- | --- |
| 01 | Buffer Overflow | C | Unbounded `gets()` input | Bounded `fgets()` |
| 02 | SQL Injection | Java | SQL string concatenation | `PreparedStatement` |
| 03 | Cross-Site Scripting | PHP | Raw user input rendered into HTML | `htmlspecialchars()` |
| 04 | Hardcoded Credentials | Python | Secrets embedded in source | Runtime environment configuration |
| 05 | Insecure Deserialization | Java | Unrestricted `readObject()` | `ObjectInputFilter` |

---

# 01 — Buffer Overflow

## Vulnerable Pattern

The vulnerable C implementation allocates a fixed-size buffer:

```c
char buffer[10];
```

and then reads user-controlled input using:

```c
gets(buffer);
```

`gets()` performs no destination-boundary checking.

Conceptually:

```text
Oversized Input
      │
      ▼
Fixed-Size Buffer
      │
      ▼
Boundary Exceeded
      │
      ▼
Memory Corruption
```

Potential consequences include:

- Application crashes
- Corrupted application state
- Unpredictable behavior
- Control-flow manipulation in more advanced scenarios

---

## Secure Implementation

The unsafe function is replaced with:

```c
fgets(buffer, sizeof(buffer), stdin);
```

which enforces a maximum write length.

```text
Input
  │
  ▼
Bounded Read
  │
  ▼
Buffer Capacity Enforced
```

The original coursework reports that oversized input was re-tested after remediation and no crashes or memory corruption were observed.

### Code

- [Vulnerable implementation](vulnerable-code/buffer-overflow/vulnerable.c)
- [Secure implementation](secure-code/buffer-overflow/secure.c)
- [Case-study documentation](vulnerable-code/buffer-overflow/README.md)

### Evidence

![Buffer overflow vulnerable code](evidence/screenshots/01-buffer-overflow-testing.png)

![Buffer overflow remediation](evidence/screenshots/02-buffer-overflow-remediation.png)

---

# 02 — SQL Injection

## Vulnerable Pattern

The vulnerable Java example constructs a query by directly combining SQL syntax with user-controlled values.

```java
String query =
    "SELECT * FROM users WHERE username='"
    + username
    + "' AND password='"
    + password
    + "'";
```

Conceptually:

```text
SQL Syntax
    +
User Data
    │
    ▼
Executable Query
```

An attacker may therefore be able to influence the intended query logic.

Potential consequences include:

- Authentication bypass
- Unauthorized data access
- Database enumeration
- Data modification
- Destructive queries

---

## Secure Implementation

The remediation uses:

```java
PreparedStatement
```

with placeholders:

```java
"SELECT 1 FROM users WHERE username = ? AND password = ?"
```

and separately bound parameters:

```java
statement.setString(1, username);
statement.setString(2, password);
```

This separates:

```text
SQL Structure
```

from:

```text
User-Controlled Data
```

so supplied values are handled as data rather than executable SQL syntax.

### Code

- [Vulnerable implementation](vulnerable-code/sql-injection/VulnerableLogin.java)
- [Secure implementation](secure-code/sql-injection/SecureLogin.java)
- [Case-study documentation](vulnerable-code/sql-injection/README.md)

### Evidence

![SQL injection vulnerable code](evidence/screenshots/03-sql-injection-testing.png)

![SQL injection remediation](evidence/screenshots/04-sql-injection-remediation.png)

---

# 03 — Cross-Site Scripting

## Vulnerable Pattern

The vulnerable PHP implementation directly reflects URL-controlled input into an HTML page:

```php
<p>Welcome, <?php echo $_GET['name']; ?></p>
```

The browser may therefore interpret attacker-controlled input as active page content.

```text
Untrusted Input
      │
      ▼
HTML Response
      │
      ▼
Browser
      │
      ▼
Potential Script Execution
```

Potential impact includes:

- Session theft
- Page manipulation
- Phishing
- User impersonation
- Malicious browser-side actions

---

## Secure Implementation

The remediation applies context-appropriate output encoding:

```php
htmlspecialchars(
    $name,
    ENT_QUOTES,
    'UTF-8'
);
```

The flow becomes:

```text
Untrusted Input
      │
      ▼
Output Encoding
      │
      ▼
HTML-Safe Text
      │
      ▼
Browser
```

The original coursework reports that the malicious test input was rendered as text after remediation and JavaScript execution no longer occurred.

### Code

- [Vulnerable implementation](vulnerable-code/xss/vulnerable.php)
- [Secure implementation](secure-code/xss/secure.php)
- [Case-study documentation](vulnerable-code/xss/README.md)

### Evidence

![XSS vulnerable implementation](evidence/screenshots/05-xss-testing-and-remediation01.png)

![XSS remediation](evidence/screenshots/05-xss-testing-and-remediation02.png)

---

# 04 — Hardcoded Credentials

## Vulnerable Pattern

The vulnerable example embeds authentication material directly in Python source:

```python
USERNAME = "admin"
PASSWORD = "example-password"
```

The problem is the storage location, not the particular demonstration values.

Conceptually:

```text
Source Code
    │
    ├── Application Logic
    │
    └── Authentication Secret
```

Anyone obtaining the source may also obtain the credential.

---

## Security Risks

Hardcoded secrets may be exposed through:

- Source repositories
- Developer workstations
- Backup archives
- Reverse engineering
- Repository misconfiguration
- Accidental publication

They also make credential rotation more difficult.

---

## Secure Implementation

The remediation externalizes configuration from source code:

```python
USERNAME = os.getenv("APP_USERNAME")
PASSWORD = os.getenv("APP_PASSWORD")
```

Conceptually:

```text
Application Code
      │
      ▼
Runtime Configuration
      │
      ▼
Secret
```

The original coursework reports that after remediation no credential values remained in the source code.

### Code

- [Vulnerable implementation](vulnerable-code/hardcoded-credentials/vulnerable.py)
- [Secure implementation](secure-code/hardcoded-credentials/secure.py)
- [Case-study documentation](vulnerable-code/hardcoded-credentials/README.md)

### Evidence

![Hardcoded credentials vulnerable example](evidence/screenshots/06-hardcoded-credentials-vulnerable.png)

![Hardcoded credentials remediation](evidence/screenshots/07-hardcoded-credentials-remediation.png)

> The reusable examples in this repository use demonstration placeholders only. No real reusable secret is intentionally published.

---

# 05 — Insecure Deserialization

## Vulnerable Pattern

The vulnerable Java implementation reconstructs arbitrary serialized objects:

```java
ObjectInputStream ois =
    new ObjectInputStream(inputStream);

Object obj = ois.readObject();
```

without restricting which classes may be instantiated.

Conceptually:

```text
Untrusted Serialized Data
          │
          ▼
     readObject()
          │
          ▼
Object Reconstruction
          │
          ▼
Potential Unsafe Behavior
```

Potential impact may include:

- Unexpected application behavior
- State manipulation
- Denial of service
- Gadget-chain execution
- Arbitrary code execution in vulnerable environments

---

## Secure Implementation

The remediation introduces:

```java
ObjectInputFilter
```

to limit which object types may be reconstructed.

Example:

```java
ObjectInputFilter filter =
    ObjectInputFilter.Config.createFilter(
        "com.myapp.*;java.base/*;!*"
    );

objectInputStream.setObjectInputFilter(filter);
```

Conceptually:

```text
Serialized Data
      │
      ▼
Object Filter
   /       \
Allowed   Rejected
  │
  ▼
Deserialize
```

Where practical, data-only formats with explicit schemas are preferable to native object deserialization for externally controlled input.

### Code

- [Vulnerable implementation](vulnerable-code/insecure-deserialization/VulnerableDeserializer.java)
- [Secure implementation](secure-code/insecure-deserialization/SecureDeserializer.java)
- [Case-study documentation](vulnerable-code/insecure-deserialization/README.md)

### Evidence

![Insecure deserialization vulnerable code](evidence/screenshots/08-insecure-deserialization-vulnerable.png)

![Insecure deserialization remediation](evidence/screenshots/09-insecure-deserialization-remediation.png)

---

# Root-Cause Comparison

The five case studies affect different technologies, but a common pattern appears repeatedly:

```text
Untrusted Data
      │
      ▼
Trusted Too Early
      │
      ▼
Unsafe Operation
      │
      ▼
Security Vulnerability
```

The remediation introduces an explicit security boundary.

| Vulnerability | Missing Boundary | Secure Boundary |
| --- | --- | --- |
| Buffer Overflow | Memory capacity | Bounded input |
| SQL Injection | SQL syntax vs. user data | Parameterized query |
| XSS | HTML code vs. user data | Output encoding |
| Hardcoded Credentials | Source code vs. secret material | Externalized secret configuration |
| Insecure Deserialization | Arbitrary object vs. expected type | Object filtering / schema validation |

---

# Remediation Validation

A major theme of the project is **re-testing**.

The workflow is not:

```text
Find Vulnerability
      ↓
Change Code
      ↓
Done
```

Instead:

```text
Find Vulnerability
      │
      ▼
Establish Reproducible Test
      │
      ▼
Implement Fix
      │
      ▼
Repeat Original Test
      │
      ▼
Confirm Security Condition Changed
```

The original coursework documents successful re-testing across the case studies.

---

# OWASP Alignment

The coursework maps the vulnerabilities to OWASP Top 10 (2021) categories.

| Vulnerability | Coursework Mapping |
| --- | --- |
| Buffer Overflow | A04 — Insecure Design |
| SQL Injection | A03 — Injection |
| Cross-Site Scripting | A03 — Injection |
| Hardcoded Credentials | A07 — Identification and Authentication Failures |
| Insecure Deserialization | A08 — Software and Data Integrity Failures |

These mappings preserve the classification used in the original assessment.

---

# Secure Development Principles

The case studies reinforce several secure-development principles.

## Validate Inputs

Applications should define and enforce expected input constraints.

## Separate Code from Data

User-controlled input should never become executable SQL structure.

## Encode Outputs

Untrusted content should be encoded for its browser output context.

## Use Bounded Operations

Memory operations should enforce destination capacity.

## Protect Secrets

Secrets belong in controlled configuration or dedicated secret-management systems, not application source.

## Restrict Deserialization

Applications should not reconstruct arbitrary native objects from untrusted input.

## Apply Least Privilege

Application and database accounts should receive only the permissions they require.

## Re-Test Security Fixes

A remediation should be validated against the original vulnerable condition.

---

# Secure Software Development Lifecycle

The project places individual code fixes within a broader secure-development lifecycle.

```text
Requirements
     │
     ▼
Secure Design
     │
     ▼
Implementation
     │
     ▼
Security Testing
     │
     ▼
Deployment
     │
     ▼
Monitoring & Maintenance
```

Security should be integrated throughout software development rather than treated as a final-stage activity.

---

# Testing Approaches

The original coursework uses or discusses techniques including:

- Manual code review
- Manual fuzzing
- Compiler warnings
- Static analysis
- Dynamic testing
- HTTP request manipulation
- Browser testing
- OWASP ZAP
- Burp Suite
- Secret scanning
- Dependency analysis
- Re-testing after remediation

No single testing approach identifies every class of vulnerability.

A stronger application-security program combines multiple complementary techniques.

---

# Evidence vs. Portfolio Code

This repository intentionally separates two forms of material.

## Original Coursework Evidence

Located under:

```text
evidence/screenshots/
```

These screenshots preserve selected evidence from the original assessment.

## Clean Portfolio Examples

Located under:

```text
vulnerable-code/
secure-code/
```

These implementations make each vulnerable/secure comparison easier to inspect directly through GitHub.

The clean examples preserve the same root security concepts while improving readability and organization.

---

# Repository Structure

```text
secure-coding-application-security/
├── README.md
├── LICENSE
│
├── docs/
│   └── methodology.md
│
├── evidence/
│   ├── README.md
│   └── screenshots/
│       ├── 01-buffer-overflow-testing.png
│       ├── 02-buffer-overflow-remediation.png
│       ├── 03-sql-injection-testing.png
│       ├── 04-sql-injection-remediation.png
│       ├── 05-xss-testing-and-remediation01.png
│       ├── 05-xss-testing-and-remediation02.png
│       ├── 06-hardcoded-credentials-vulnerable.png
│       ├── 07-hardcoded-credentials-remediation.png
│       ├── 08-insecure-deserialization-vulnerable.png
│       └── 09-insecure-deserialization-remediation.png
│
├── vulnerable-code/
│   ├── buffer-overflow/
│   ├── sql-injection/
│   ├── xss/
│   ├── hardcoded-credentials/
│   └── insecure-deserialization/
│
└── secure-code/
    ├── buffer-overflow/
    ├── sql-injection/
    ├── xss/
    ├── hardcoded-credentials/
    └── insecure-deserialization/
```

---

# Documentation

| Resource | Description |
| --- | --- |
| [Secure Coding Methodology](docs/methodology.md) | Assessment and remediation methodology |
| [Evidence Walkthrough](evidence/README.md) | Original coursework evidence |
| [Buffer Overflow](vulnerable-code/buffer-overflow/README.md) | Memory-safety case study |
| [SQL Injection](vulnerable-code/sql-injection/README.md) | Database-query security |
| [XSS](vulnerable-code/xss/README.md) | Browser output security |
| [Hardcoded Credentials](vulnerable-code/hardcoded-credentials/README.md) | Secret-management case study |
| [Insecure Deserialization](vulnerable-code/insecure-deserialization/README.md) | Object deserialization security |

---

# Skills Demonstrated

This project demonstrates practical understanding of:

- Secure Coding
- Application Security
- Vulnerability Analysis
- Security Remediation
- Security Re-Testing
- C Memory Safety
- Buffer Overflows
- Java
- SQL Injection
- Parameterized Queries
- PHP
- Cross-Site Scripting
- Output Encoding
- Python
- Secret Management
- Insecure Deserialization
- Object Filtering
- Static Analysis
- Dynamic Testing
- Burp Suite
- OWASP ZAP
- Secure SDLC
- OWASP Top 10

---

# Production Considerations

The code examples are intentionally small and designed to isolate specific vulnerability classes.

Production applications would typically require additional controls such as:

- Centralized authentication
- Strong password hashing
- Formal secret-management platforms
- Dependency scanning
- SAST
- DAST
- Software Composition Analysis
- CI/CD security gates
- Content Security Policy
- Centralized logging
- Secure error handling
- Runtime monitoring
- Security code-review processes

These are production recommendations rather than claims about the original coursework implementation.

---

# Scope & Limitations

This repository represents secure-coding and application-security laboratory work.

It should not be interpreted as evidence of:

- Production application deployment
- Production penetration testing
- Formal security certification
- Enterprise DevSecOps implementation
- Production WAF operation
- Real credential compromise
- Third-party application exploitation

The project focuses on understanding vulnerable patterns and demonstrating safer alternatives.

---

# Ethical Use

The intentionally vulnerable code is included solely for:

- Secure-development education
- Defensive application-security learning
- Authorized laboratory testing
- Technical portfolio demonstration

Do not use these examples to attack systems without explicit authorization.

---

## Author

**Ravi Prajapati**

Cybersecurity | Application Security | Enterprise IT | Security Operations

[LinkedIn](https://www.linkedin.com/in/ravi-prajapati-it) · [GitHub](https://github.com/raviprajapati-it)
