<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>${login}'s account</title>
</head>
<body>
    <h1>${login}</h1>
    <hr>
    ${rating}

    <form action="/account" method="post">
        <input readonly hidden name="logout" value="true" />
        <button type="submit">Logout</button>
    </form>
</body>
</html>