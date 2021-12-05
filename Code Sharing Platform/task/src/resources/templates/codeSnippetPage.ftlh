<#-- @ftlvariable name="codeSnippet" type="platform.CodeSnippet" -->

<html lang="en">
<head>
    <link rel="stylesheet" href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/atom-one-dark.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <title>Code</title>
</head>
<body>
    <div>
        <#if codeSnippet.isViewsRestricted()>
            <span id="views_restriction">${codeSnippet.views} more views allowed</span>
            <br>
        </#if>
        <#if codeSnippet.isTimeRestricted()>
            <span id="time_restriction">The code will be available for ${codeSnippet.time} seconds</span>
        </#if>
        <pre id="code_snippet"><code>${codeSnippet.code}</code></pre>
        <span id="load_date">${codeSnippet.date}</span>
    </div>
</body>
</html>