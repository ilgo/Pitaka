xquery version "1.0";

let $document := doc("%s")
let $current := %s
let $next := %s
let $prev := %s
let $title := $document//title/text()
let $body := $document//body/*
let $pages := $document//pb[@type="html"]
let $idx1 := index-of($body, $pages[position() = 1])
let $idxs := insert-before($idx1, count($idx1)+1, count($body))

return

<html>
<head>
<style>
<!--
body { 
 font : 18pt "AR PL UKai TW";  
}
h1 {
 color : red;
}
h2 {
 color : blue;
}
div {
	font : 18pt "AR PL UKai TW";
}
.page {
	text-indent : 2em;
	text-align : right;
}
.level_1 {
	text-indent : 1em;
}
.level_2 {
	text-indent : 2em;
}
.level_3 {
	text-indent : 3em;
}
.level_4 {
	text-indent : 4em;
}
.level_5 {
	text-indent : 5em;
}
.level_6 {
	text-indent : 6em;
}

-->
</style>
<title>{$title}</title>
</head>
<body>
	<h1>{$title}</h1>
	<table>
	<tr><td>Previous Page</td></tr>
	<tr><td>Contents</td></tr>
	<tr><td>Next Page</td></tr>
	</table>
	<hr />
	{$body[position() = ($idxs[$current] to $idxs[$next])]}
	<hr/>
	<table>
	<tr><td>Previous Page</td></tr>
	<tr><td>Contents</td></tr>
	<tr><td>Next Page</td></tr>
	</table>
</body>
</html>