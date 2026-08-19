# Hardcoded Credentials — Vulnerable vs Secure Secret Handling

## Overview

This case study demonstrates the risk of storing credentials directly inside application source code.

A vulnerable implementation contains authentication secrets such as:

```python
USERNAME = "admin"
PASSWORD = "example-password"
```

The problem is not the specific placeholder values.

The problem is that the credential material exists inside the source code itself.

---

## Vulnerable Pattern

Hardcoded secrets may include:

```text
Passwords
API Keys
Database Credentials
Access Tokens
Encryption Keys
Cloud Credentials
```

When embedded directly in source code, the secret becomes coupled to the application.

Conceptually:

```text
Source Code
    │
    ├── Application Logic
    │
    └── Credentials
```

Anyone who gains access to the code may therefore gain access to the secret.

---

## Security Risks

Hardcoded credentials introduce several risks.

### Source-Code Exposure

Secrets may become visible through:

- Public repositories
- Private repository compromise
- Source-code archives
- Developer workstations
- Backups

### Reverse Engineering

Secrets embedded in compiled or packaged applications may sometimes be recovered through static or dynamic analysis.

### Credential Rotation

Changing a hardcoded credential often requires modifying and redeploying the application.

### Secret Reuse

A leaked credential may provide access to additional services if the same secret is reused elsewhere.

---

## Detection

Hardcoded credentials can be identified using:

- Manual code review
- Static analysis
- Secret scanning
- Repository scanning
- CI/CD security checks

A secure development workflow should detect secrets before they are committed to source control.

---

## Secure Implementation

The remediated implementation removes credentials from the source code.

Instead, values are obtained at runtime:

```python
import os

USERNAME = os.getenv("APP_USERNAME")
PASSWORD = os.getenv("APP_PASSWORD")
```

Conceptually:

```text
Source Code
    │
    ▼
Environment Variable Reference
    │
    ▼
Runtime Configuration
    │
    ▼
Secret
```

This separates:

```text
Application Logic
```

from:

```text
Credential Material
```

---

## Why Externalization Helps

Moving secrets outside source code provides several advantages:

- Credentials can change without editing application code
- Secrets do not need to appear in repositories
- Different environments can use different credentials
- Deployment systems can inject configuration at runtime
- Secret scanning becomes easier to enforce

---

## Re-Testing

After remediation, the codebase should be reviewed again.

Expected result:

```text
Source Review
     │
     ▼
Search for Credentials
     │
     ▼
No Plaintext Secret Found
```

The original coursework reports that after remediation no credentials remained present in the application source.

---

## Environment Variables Are Not a Complete Secret-Management Strategy

Environment variables improve separation between source code and secrets, but they are not automatically secure in every environment.

Production applications may require:

- Centralized secret-management platforms
- Short-lived credentials
- Automated rotation
- Workload identity
- Access-control policies
- Secret-audit logging

Examples include enterprise vaults and cloud-native secret-management services.

These are production recommendations rather than claims about the original coursework implementation.

---

## Repository Safety

This repository does not contain real reusable credentials.

Any values shown in vulnerable examples are demonstration placeholders.

Do not commit:

```text
Real passwords
API tokens
Cloud keys
Database credentials
Private keys
Session secrets
```

to source control.

---

## Files

### Vulnerable

[`vulnerable.py`](vulnerable.py)

### Secure

[`secure.py`](../../secure-code/hardcoded-credentials/secure.py)

---

## Security Principle

> Secrets are configuration, not source code.

Authentication material should be stored and managed separately from application logic.

---

> The vulnerable implementation is intentionally insecure and is included only for secure-coding education and authorized laboratory testing.
