xquery version "1.0";

let $document := doc("DOCUMENT")
let $title := $document//title/text()
let $body := $document//body/*
let $pages := $document//pb[@type="html"]
let $idxs := index-of($body, $pages[position() = 1])

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
<script language="JavaScript">
<!--
function function1(n) {
	var res;
    res = theJavaFunction(n);
    alert(res);
}
-->
</script> 
<title>{$title}</title>
</head>
<body>
	<h1>{$title}</h1>
	<table>
	<tr><td>
	<h2>{$document//author/text()}</h2>
	</td><td>    </td><td>
	<h2>({$document//extent/text()})</h2>
	</td></tr>
	</table>
	<hr />
	
	<table>
	
	{ for $div in $document//div
		return <tr><td><div class="level_{data($div/@level)}">
				{$div/text()}
			   </div></td><td>
			    <div class="page">{data($div/@page)}</div>
			   </td></tr>	
	}
	</table>	
</body>
</html>