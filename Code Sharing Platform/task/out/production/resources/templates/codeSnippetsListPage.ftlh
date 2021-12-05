<#-- @ftlvariable name="codeSnippets" type="java.util.List<platform.CodeSnippet>" -->

<html lang="en">
<head>
    <link rel="stylesheet"
          target="_blank" href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/atom-one-dark.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <title>Latest</title>
</head>
<body>
    <#list codeSnippets as codeSnippet>
        <div>
            <pre><code>${codeSnippet.code}</code></pre>
            <span>${codeSnippet.date}</span>
        </div>
    </#list>
</body>
</html>