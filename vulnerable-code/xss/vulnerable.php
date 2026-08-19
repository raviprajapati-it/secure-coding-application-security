<?php
/*
 * Cross-Site Scripting Demonstration
 *
 * INTENTIONALLY VULNERABLE CODE
 *
 * This example reflects user-controlled input directly into
 * HTML without output encoding.
 *
 * Educational and authorized laboratory use only.
 */

$name = $_GET['name'] ?? 'Guest';
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Vulnerable XSS Example</title>
</head>
<body>

    <h1>Welcome</h1>

    <!-- VULNERABLE:
         User-controlled input is rendered directly into HTML. -->
    <p>Welcome, <?php echo $name; ?></p>

</body>
</html>
