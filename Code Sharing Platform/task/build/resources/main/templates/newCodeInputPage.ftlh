<html lang="en">
<head>
    <script>
        function send() {
            let object = {
                "code": document.getElementById("code_snippet").value,
                "time": document.getElementById("time_restriction").value,
                "views": document.getElementById("views_restriction").value
            };

            let json = JSON.stringify(object);

            let xhr = new XMLHttpRequest();
            xhr.open("POST", '/api/code/new', false);
            xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
            xhr.send(json);

            if (xhr.status === 200) {
                alert("Success!");
            }
        }
    </script>
    <title>Create</title>
    </head>
<body>
    <div style="margin-top: 10px">
        <label for="code_snippet"></label>
        <textarea style="width: 90%; height: 50%" id="code_snippet">// write your code here</textarea>
    </div>
    <div style="margin-top: 10px">
        <label for="time_restriction">Time restriction: </label>
        <input id="time_restriction" type="text"/>
    </div>
    <div style="margin-top: 10px">
        <label for="views_restriction">Views restriction:</label>
        <input id="views_restriction" type="text"/>
    </div>
    <button style="margin-top: 10px" id="send_snippet" type="submit" onclick="send()">Submit</button>
</body>
</html>