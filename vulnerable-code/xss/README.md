# Cross-Site Scripting (XSS) — Vulnerable vs Secure Output Handling

## Overview

This case study demonstrates a reflected Cross-Site Scripting vulnerability caused by rendering attacker-controlled input directly into an HTML response.

The vulnerable flow is:

```text
User Input
    │
    ▼
Application Response
    │
    ▼
Browser
    │
    ▼
Input Interpreted as HTML / JavaScript
```

If the application does not encode output correctly, attacker-controlled content may execute in the security context of the affected website.

---

## Vulnerable Pattern

The vulnerable implementation reflects the value of:

```php
$_GET['name']
```

directly into the page.

Example:

```php
<p>Welcome, <?php echo $_GET['name']; ?></p>
```

The application does not distinguish between:

```text
User Data
```

and:

```text
Executable Browser Content
```

---

## Security Impact

Cross-Site Scripting can potentially enable:

- Session theft
- Client-side content manipulation
- Phishing
- Malicious redirects
- Unauthorized user actions
- Credential theft
- Browser-based social engineering

The exact impact depends on the application's authentication model, browser protections, and session configuration.

---

## Secure Implementation

The remediated version applies output encoding:

```php
htmlspecialchars(
    $name,
    ENT_QUOTES,
    'UTF-8'
);
```

Conceptually:

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

Special characters are represented as data rather than executable markup.

---

## Why Output Encoding Works

Output encoding converts characters with special meaning in HTML into safe representations for the current output context.

The browser therefore receives:

```text
Text
```

rather than:

```text
Executable HTML / JavaScript
```

when the value is inserted into an HTML text context.

---

## Re-Testing

The original coursework repeated the same malicious input after remediation.

Expected behavior:

```text
Script Payload
      │
      ▼
htmlspecialchars()
      │
      ▼
Encoded Output
      │
      ▼
Displayed as Text
      │
      ▼
No JavaScript Execution
```

The coursework reports that the test payload was rendered as plain text and no JavaScript executed after remediation.

---

## Additional Defensive Controls

Output encoding is the primary defense for this example.

Additional controls include:

- Context-aware output encoding
- Server-side input validation
- Content Security Policy
- Secure template frameworks
- `HttpOnly` session cookies
- Appropriate `SameSite` cookie configuration
- Security testing of dynamic output

These controls complement output encoding rather than replace it.

---

## Context Matters

The encoding function must match the context in which data is rendered.

For example:

```text
HTML text
HTML attribute
JavaScript
CSS
URL
```

may each require different handling.

This example specifically addresses untrusted input placed into an **HTML text context**.

---

## Files

### Vulnerable

[`vulnerable.php`](vulnerable.php)

### Secure

[`secure.php`](../../secure-code/xss/secure.php)

---

## Security Principle

> Never render untrusted input into browser content without applying the correct output encoding for the destination context.

---

> The vulnerable implementation is intentionally insecure and is included only for secure-coding education and authorized laboratory testing.
