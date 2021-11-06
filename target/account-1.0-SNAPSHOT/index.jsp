<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form class="form" action="/login" method="post">
    <div>
        <label class="label" for="login">Login</label>
        <input class="login" type="text" name="login" id="login" />
    </div>
    <div>
        <label class="label" for="password">Password</label>
        <input class="password" type="password" id="password" name="password" />
    </div>
    <div>
        <button class="submit" type="submit">Login</button>
        <button  class="reset" type="reset">Reset</button>
    </div>
</form>
</body>
</html>
