# Insecure Deserialization — Vulnerable vs Restricted Object Handling

## Overview

This case study demonstrates the risk of deserializing attacker-controlled object data without sufficient validation.

Object deserialization reconstructs application objects from a serialized representation.

Conceptually:

```text
Serialized Data
      │
      ▼
Deserializer
      │
      ▼
Object Reconstruction
      │
      ▼
Application Logic
```

If the serialized data is attacker-controlled and the application trusts it automatically, unexpected object types or object behavior may influence application execution.

---

## Vulnerable Pattern

The vulnerable implementation creates an `ObjectInputStream` and immediately calls:

```java
readObject();
```

without restricting the classes that can be deserialized.

Example:

```java
ObjectInputStream ois =
    new ObjectInputStream(inputStream);

Object obj = ois.readObject();
```

The application effectively assumes:

```text
Serialized Input
      =
Trusted Object
```

which is unsafe when the source is untrusted.

---

## Potential Impact

Insecure deserialization can potentially result in:

- Arbitrary code execution
- Application compromise
- Data manipulation
- Unauthorized object creation
- Denial of service
- Integrity loss

The actual impact depends on:

- Available classes
- Application dependencies
- Deserialization behavior
- Accessible gadget chains
- Application privileges

---

## Why Native Object Deserialization Is Risky

Serialized objects may contain more than passive data.

Object reconstruction can trigger:

- Constructors
- Object initialization
- Custom deserialization methods
- Library behavior
- Unexpected method execution

If dangerous classes exist in the application classpath, crafted serialized input may abuse those behaviors.

---

## Secure Implementation

The remediated implementation applies an:

```java
ObjectInputFilter
```

before calling:

```java
readObject();
```

Example:

```java
ObjectInputFilter filter =
    ObjectInputFilter.Config.createFilter(
        "com.myapp.*;java.base/*;!*"
    );

objectInputStream.setObjectInputFilter(filter);
```

This applies an allowlist-oriented policy.

Conceptually:

```text
Serialized Input
       │
       ▼
Object Filter
    /       \
   /         \
Allowed     Rejected
  │
  ▼
Deserialize
```

---

## Why Filtering Helps

A class allowlist reduces the range of objects that the deserializer is permitted to reconstruct.

Instead of accepting arbitrary classes:

```text
Any Serializable Object
```

the application limits deserialization to:

```text
Expected Application Types
```

This reduces exposure to unexpected object behavior and potentially dangerous gadget chains.

---

## Preferred Design

Where possible, applications should avoid native object deserialization for untrusted external input.

Safer alternatives may include data-oriented formats such as:

```text
JSON
```

combined with:

- Explicit schemas
- Type validation
- Field validation
- Size limits
- Integrity checks

The key principle is to deserialize **data**, not arbitrary executable object graphs.

---

## Additional Controls

Defensive measures include:

- Avoid native deserialization where practical
- Apply strict class allowlists
- Validate serialized data provenance
- Apply message integrity controls
- Limit input size and complexity
- Keep dependencies updated
- Reduce application privileges
- Review libraries for dangerous gadget chains
- Perform dependency security analysis

---

## Re-Testing

After remediation, the original testing process should be repeated.

Expected result:

```text
Legitimate Object
      │
      ▼
Allowed by Filter
      │
      ▼
Processed Normally
```

while:

```text
Unexpected / Malicious Object
      │
      ▼
Rejected by Filter
      │
      ▼
No Unsafe Deserialization
```

The original coursework reports that malicious objects were rejected after remediation while legitimate data continued to process correctly.

---

## Files

### Vulnerable

[`VulnerableDeserializer.java`](VulnerableDeserializer.java)

### Secure

[`SecureDeserializer.java`](../../secure-code/insecure-deserialization/SecureDeserializer.java)

---

## Security Principle

> Never deserialize untrusted native objects without strict restrictions on what may be reconstructed.

Prefer data-only formats and explicit validation when possible.

---

> The vulnerable implementation is intentionally insecure and is included only for secure-coding education and authorized laboratory testing.
