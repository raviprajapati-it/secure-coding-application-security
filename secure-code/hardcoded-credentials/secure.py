"""
Hardcoded Credentials Remediation

SECURE VERSION

Credentials are loaded from the runtime environment rather than
being embedded directly in source code.
"""

import os
import secrets


def get_required_secret(name: str) -> str:
    """
    Load a required environment variable.

    The application exits cleanly if the secret has not been supplied
    through the runtime environment.
    """
    value = os.getenv(name)

    if not value:
        raise RuntimeError(
            f"Required environment variable '{name}' is not configured."
        )

    return value


def authenticate(username: str, password: str) -> bool:
    configured_username = get_required_secret("APP_USERNAME")
    configured_password = get_required_secret("APP_PASSWORD")

    return (
        secrets.compare_digest(username, configured_username)
        and secrets.compare_digest(password, configured_password)
    )


if __name__ == "__main__":
    supplied_user = input("Username: ")
    supplied_password = input("Password: ")

    try:
        authenticated = authenticate(
            supplied_user,
            supplied_password,
        )
    except RuntimeError as error:
        print(f"Configuration error: {error}")
        raise SystemExit(1)

    if authenticated:
        print("Authentication successful.")
    else:
        print("Authentication failed.")
