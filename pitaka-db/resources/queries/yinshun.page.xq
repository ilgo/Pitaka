xquery version "1.0";

let $document := doc("%s")
let $current := %s
let $next := %s
let $title := $document//title/text()
let $body := $document//body/*
let $pages := $document//pb[@type="html"]
let $idx1 := index-of($body, $pages[position() = 1])
let $idxs := insert-before($idx1, count($idx1)+1, count($body))

return

<html>
<head>
<link href="yinshun.css" rel="stylesheet" type="text/css" />
<title>{$title}</title>
</head>
<body>
	<h1>{$title}</h1>
	NAVIGATION
	<hr />
	{$body[position() = ($idxs[$current] to $idxs[$next])]}
	<hr/>
	NAVIGATION
</body>
</html>