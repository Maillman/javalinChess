## Javalin Notes
### JaCoCo Code Coverage
I have run this through the AutoGrader on the Javalin branch and have noticed something regarding code coverage.
It seems that JaCoCo only counts methods (not constructors) that have if blocks or try/catch blocks.
If there aren't any if or try/catch blocks in a method, then it won't count it towards branch coverage.
Hence, why the ClearService's clear method doesn't count nor any of the memoryDAO methods,
as they don't have those blocks and the code will just run through the as normal.

This honestly makes things quite frustrating and very confusing,
as it seems arbitrary until further research.
