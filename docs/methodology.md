# Secure Coding & Application Security Methodology

## Overview

This project examines common software-security vulnerabilities through a structured secure-development workflow.

Rather than stopping at vulnerability identification, each case study follows the complete cycle:

```text
Vulnerable Implementation
        │
        ▼
Security Analysis
        │
        ▼
Controlled Testing
        │
        ▼
Impact Assessment
        │
        ▼
Secure Implementation
        │
        ▼
Re-Testing
        │
        ▼
Remediation Validation
```

The objective is to demonstrate both **offensive understanding of software vulnerabilities** and the **defensive engineering practices required to remediate them**.

---

# Vulnerabilities Examined

The project contains five primary application-security case studies:

| Case | Vulnerability | Security Area |
| ---: | --- | --- |
| 01 | Buffer Overflow | Memory Safety |
| 02 | SQL Injection | Input Handling / Database Security |
| 03 | Cross-Site Scripting (XSS) | Web Application Security |
| 04 | Hardcoded Credentials | Authentication / Secret Management |
| 05 | Insecure Deserialization | Software & Data Integrity |

The vulnerabilities are examined individually so that the vulnerable design, security impact, remediation, and validation can be clearly compared.

---

# Assessment Methodology

Each vulnerability follows the same general methodology.

## 1. Vulnerable Implementation

A deliberately insecure implementation is examined to understand the root cause of the vulnerability.

Questions considered include:

- What security assumption failed?
- Which input or data is attacker-controlled?
- Which trust boundary is crossed?
- Which insecure API or coding pattern is involved?
- What security control is missing?

---

## 2. Vulnerability Analysis

The vulnerable code is reviewed to determine how the weakness occurs.

The analysis considers:

```text
Input
  │
  ▼
Application Logic
  │
  ▼
Unsafe Operation
  │
  ▼
Security Impact
```

This stage focuses on understanding the vulnerability at code level rather than treating scanner output as sufficient evidence.

---

## 3. Controlled Testing

The vulnerability is tested in a controlled development or laboratory environment.

Depending on the case study, testing techniques include:

- Manual input manipulation
- Fuzz testing
- HTTP request modification
- Static analysis
- Dynamic testing
- Browser testing
- Dependency analysis
- Security scanning

Testing is intended to confirm that the vulnerable condition can produce an observable security impact.

---

## 4. Impact Assessment

The potential consequences of successful exploitation are evaluated.

Relevant security properties include:

### Confidentiality

Can unauthorized users access protected information?

### Integrity

Can an attacker modify application data or execution?

### Availability

Can the vulnerability cause crashes or denial of service?

### Authentication

Can security controls or login mechanisms be bypassed?

### Authorization

Can an attacker obtain capabilities beyond their intended permissions?

---

## 5. Secure Implementation

The vulnerable implementation is then replaced or redesigned using a safer development pattern.

The remediation is selected based on the underlying root cause rather than simply blocking one demonstration payload.

Examples include:

```text
Unsafe input function
        ↓
Bounded input function

Dynamic SQL construction
        ↓
Parameterized query

Raw HTML output
        ↓
Context-aware output encoding

Embedded password
        ↓
External secret configuration

Unrestricted deserialization
        ↓
Restricted / validated deserialization
```

---

## 6. Re-Testing

After remediation, the original testing methodology is repeated.

This is an important part of the project because changing code does not automatically prove that the vulnerability has been removed.

The secure-development cycle therefore becomes:

```text
Identify
   ↓
Exploit / Validate
   ↓
Remediate
   ↓
Re-Test
```

A remediation is considered effective only when the original vulnerability condition can no longer be reproduced while legitimate functionality remains available.

---

# Case Study 01 — Buffer Overflow

## Security Problem

The buffer-overflow example demonstrates unsafe memory handling in C.

The vulnerable implementation uses an unbounded input function against a fixed-size stack buffer.

Conceptually:

```text
User Input
    │
    ▼
Fixed Buffer
    │
    ▼
Input Exceeds Capacity
    │
    ▼
Adjacent Memory Overwritten
```

Potential consequences include:

- Application crashes
- Memory corruption
- Unpredictable behavior
- Control-flow manipulation
- Arbitrary code execution in more advanced scenarios

---

## Testing Approach

The vulnerability is assessed using techniques including:

- Oversized input
- Manual fuzzing
- Compiler warnings
- Static analysis
- Runtime observation

The objective is to determine whether input length can exceed the allocated memory boundary.

---

## Secure Coding Approach

The unsafe input operation is replaced with a bounded alternative.

Conceptually:

```c
fgets(buffer, sizeof(buffer), stdin);
```

This limits the amount of data written into the destination buffer.

Additional defensive measures include:

- Input-length validation
- Compiler warnings
- Stack-protection mechanisms
- Address Space Layout Randomization
- Safer APIs

---

## Validation

The remediated application is retested using oversized input.

The expected secure behavior is:

```text
Oversized Input
      │
      ▼
Length Restriction
      │
      ▼
Safe Truncation / Rejection
      │
      ▼
No Memory Corruption
```

---

# Case Study 02 — SQL Injection

## Security Problem

SQL injection occurs when untrusted user input is incorporated directly into executable database queries.

A vulnerable pattern resembles:

```text
User Input
    │
    ▼
String Concatenation
    │
    ▼
SQL Statement
    │
    ▼
Database
```

If input can modify SQL syntax, an attacker may be able to alter the intended query logic.

Potential impact includes:

- Authentication bypass
- Unauthorized data access
- Data modification
- Database enumeration
- Destructive database operations

---

## Testing Approach

Testing can include:

- Manual input manipulation
- HTTP request modification
- Web proxy analysis
- Automated security scanning

The purpose is to determine whether user-controlled data is interpreted as SQL syntax rather than application data.

---

## Secure Coding Approach

The vulnerable dynamic query is replaced using a parameterized query.

Conceptually:

```java
PreparedStatement stmt = conn.prepareStatement(
    "SELECT * FROM users WHERE username=? AND password=?"
);

stmt.setString(1, user);
stmt.setString(2, pass);
```

The SQL structure and user-controlled values are handled separately.

---

## Validation

The same malicious input used against the vulnerable implementation is repeated after remediation.

Expected result:

```text
Malicious Input
      │
      ▼
Prepared Statement
      │
      ▼
Input Treated as Data
      │
      ▼
Query Logic Unchanged
```

---

# Case Study 03 — Cross-Site Scripting

## Security Problem

Cross-Site Scripting occurs when untrusted data is inserted into browser-rendered content without appropriate output encoding.

A vulnerable flow can be represented as:

```text
User Input
    │
    ▼
Application Response
    │
    ▼
Browser Interprets Input as Code
    │
    ▼
JavaScript Execution
```

Potential consequences include:

- Session theft
- Page manipulation
- Phishing
- User impersonation
- Malicious redirects
- Unauthorized browser-side actions

---

## Testing Approach

The vulnerability is assessed using:

- Manual script injection
- Browser testing
- Developer tools
- Web security scanning

The objective is to determine whether attacker-controlled content can execute within the application's trusted origin.

---

## Secure Coding Approach

The remediation uses context-aware output encoding.

For example:

```php
echo htmlspecialchars(
    $_GET['name'],
    ENT_QUOTES,
    'UTF-8'
);
```

This ensures special characters are rendered as content rather than interpreted as executable HTML or JavaScript.

---

## Additional Controls

Relevant defensive controls include:

- Context-aware encoding
- Input validation
- Content Security Policy
- Secure templating frameworks
- Appropriate cookie security controls

---

## Validation

The original test input is repeated after remediation.

Expected behavior:

```text
Script Input
    │
    ▼
Output Encoding
    │
    ▼
Rendered as Text
    │
    ▼
No JavaScript Execution
```

---

# Case Study 04 — Hardcoded Credentials

## Security Problem

Hardcoded credentials occur when secrets such as usernames, passwords, API keys, or tokens are embedded directly into source code.

Example insecure pattern:

```python
USERNAME = "admin"
PASSWORD = "example-password"
```

This creates several risks:

- Exposure through source repositories
- Reverse engineering
- Accidental disclosure
- Difficult credential rotation
- Secret reuse
- Unauthorized access

> Any credential values used in this repository are demonstration placeholders and must not be interpreted as real credentials.

---

## Testing Approach

Hardcoded credentials can be identified using:

- Manual code review
- Static analysis
- Secret scanning
- Repository inspection

---

## Secure Coding Approach

Secrets are externalized from application code.

For example:

```python
import os

USERNAME = os.getenv("APP_USERNAME")
PASSWORD = os.getenv("APP_PASSWORD")
```

The application obtains configuration from the runtime environment rather than storing credentials directly in source code.

---

## Production Considerations

Environment variables improve separation between code and secrets, but production environments may require dedicated secret-management systems.

Examples include:

- Enterprise vaults
- Cloud secret managers
- Automated credential rotation
- Workload identities
- Short-lived credentials

These are production recommendations and are not necessarily components of the original coursework implementation.

---

## Validation

After remediation, source-code review should confirm that no plaintext credentials remain embedded in the application.

---

# Case Study 05 — Insecure Deserialization

## Security Problem

Deserialization reconstructs application objects from serialized data.

If an application deserializes attacker-controlled data without sufficient restrictions, malicious objects may influence program execution.

Conceptually:

```text
Untrusted Serialized Data
          │
          ▼
     Deserializer
          │
          ▼
Object Construction
          │
          ▼
Unexpected Code Path
```

Potential consequences include:

- Arbitrary command execution
- Application compromise
- Data manipulation
- Denial of service

---

## Testing Approach

Testing includes:

- Reviewing deserialization logic
- Identifying attacker-controlled serialized input
- Dependency analysis
- Controlled malicious-object testing

---

## Secure Coding Approach

The preferred defensive strategy is to avoid unsafe native object deserialization where practical.

Where deserialization is required, controls can include:

- Explicit class allowlists
- Object input filters
- Schema validation
- Integrity checks
- Safer data-only formats

An example Java control is:

```java
ObjectInputFilter filter =
    ObjectInputFilter.Config.createFilter("com.myapp.*");

ois.setObjectInputFilter(filter);
```

This restricts which object types may be reconstructed.

---

## Validation

The remediated implementation should demonstrate that:

```text
Legitimate Data
      │
      ▼
Accepted

Malicious / Unauthorized Object
      │
      ▼
Rejected
```

---

# Secure Development Principles

The five case studies demonstrate several recurring secure-development principles.

## Validate Inputs

Applications should define what input is acceptable rather than attempting to identify every possible malicious value.

## Encode Outputs

Data entering an output context should be encoded for that specific context.

## Separate Code from Data

Parameterized queries prevent user-controlled data from becoming executable SQL syntax.

## Use Memory-Safe Operations

Bounded APIs and appropriate memory protections reduce memory-corruption risk.

## Protect Secrets

Credentials should not be embedded in source code.

## Minimize Trust

Serialized data, user input, database values, and external resources should not automatically be considered trustworthy.

## Apply Least Privilege

Applications and service accounts should receive only the permissions required for their intended function.

## Re-Test Security Fixes

A security fix should be validated against the original vulnerability condition.

---

# Secure Development Lifecycle

The project extends individual vulnerability remediation into a broader development lifecycle.

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

Security should be integrated throughout development rather than added only after vulnerabilities are discovered.

---

# Requirements Phase

Security requirements should be defined alongside functional requirements.

Examples include:

- Authentication requirements
- Authorization requirements
- Data-protection requirements
- Input constraints
- Logging requirements
- Regulatory obligations

---

# Design Phase

The design stage should consider:

- Trust boundaries
- Threat modeling
- Data flows
- Privilege boundaries
- Authentication architecture
- Secret management
- Failure modes

Threat-modeling approaches such as STRIDE can help systematically identify potential security weaknesses.

---

# Implementation Phase

Developers should:

- Follow secure coding standards
- Avoid unsafe APIs
- Use framework security features
- Apply input validation
- Use parameterized queries
- Encode outputs
- Protect credentials
- Handle errors securely

---

# Testing Phase

Security testing should combine multiple techniques.

```text
Code Review
    +
Static Analysis
    +
Dynamic Testing
    +
Dependency Analysis
    +
Penetration Testing
```

No single technique identifies every class of vulnerability.

---

# Deployment & Maintenance

Security continues after application release.

Relevant controls include:

- Secure configuration
- Patch management
- Dependency updates
- Logging
- Monitoring
- Vulnerability management
- Incident response
- Periodic security reassessment

---

# Evidence Standard

This repository distinguishes between:

## Vulnerable Examples

Intentionally insecure code used to explain and test a vulnerability.

## Secure Examples

Remediated implementations demonstrating safer coding patterns.

## Testing Evidence

Evidence showing the vulnerable condition and/or successful remediation validation.

## Production Recommendations

Additional controls that would strengthen a real deployment but were not necessarily implemented in the original academic exercise.

This distinction prevents recommendations from being represented as completed implementation work.

---

# Ethical Use

All vulnerable code and testing examples in this repository are intended for:

- Secure-development education
- Defensive application-security training
- Authorized laboratory testing
- Technical portfolio demonstration

They should not be used to attack applications or systems without explicit authorization.

---

# Key Skills Demonstrated

This project demonstrates practical understanding of:

- Secure coding
- Application security
- Vulnerability analysis
- Buffer overflows
- Memory safety
- SQL injection
- Parameterized queries
- Cross-Site Scripting
- Output encoding
- Credential security
- Secret management
- Insecure deserialization
- Static analysis
- Dynamic testing
- Security remediation
- Re-testing
- OWASP-aligned security principles
- Secure Software Development Lifecycle
