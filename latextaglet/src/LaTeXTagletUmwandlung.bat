%3
cd %1
latex %2
tex4ht %2  
t4ht %2
@echo off
if exist %2.4tc del %2.4tc
if exist %2.xref del %2.xref
if exist %2.aux del %2.aux
if exist %2.log del %2.log
if exist %2.idv del %2.idv
if exist %2.lg del %2.lg
if exist %2.tmp del %2.tmp
if exist %2.dvi del %2.dvi
if exist %2.html del %2.html
if exist %2.css del %2.css
if exist %2.tex del %2.tex
if exist %2.4ct del %2.4ct
if exist %2.ps del zz%2.ps
if exist %2.png del %2.png
rename %21x.png %2.png
exit



