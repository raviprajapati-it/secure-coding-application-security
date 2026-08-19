<?php
/*
 * Cross-Site Scripting Remediation
 *
 * SECURE VERSION
 *
 * User-controlled data is encoded before being rendered
 * into an HTML text context.
 */

$name = $_GET['name'] ?? 'Guest';

$safeName = htmlspecialchars(
    $name,
    ENT_QUOTES,
    'UTF-8'
);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Secure XSS Example</title>
</head>
<body>

    <h1>Welcome</h1>

    <p>Welcome, <?php echo $safeName; ?></p>

</body>
</html>
