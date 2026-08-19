"""
Hardcoded Credentials Demonstration

INTENTIONALLY VULNERABLE CODE

This example embeds authentication secrets directly in source code.

Educational and authorized laboratory use only.
"""

USERNAME = "admin"
PASSWORD = "example-password"


def authenticate(username: str, password: str) -> bool:
    """
    Compare supplied credentials against values embedded in source code.
    """
    return username == USERNAME and password == PASSWORD


if __name__ == "__main__":
    supplied_user = input("Username: ")
    supplied_password = input("Password: ")

    if authenticate(supplied_user, supplied_password):
        print("Authentication successful.")
    else:
        print("Authentication failed.")
