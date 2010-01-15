xquery version "1.0";

let $document := doc("DOCUMENT")
let $title := $document//title/text()
let $body := $document//body/*
let $pages := $document//pb[@type="html"]
let $divs := $document//div

return
<data>
<title>{$title}</title>
<author>{$document//author/text()}</author>
<extent>{$document//extent/text()}</extent>
{$pages}
{$divs}
</data>