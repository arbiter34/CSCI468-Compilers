/*Pascal *like* language*/

%{
/*math.h included for atof and atoi for fancy number printing*/
#include <math.h>
%}

DIGIT    [0-9]
ID       [a-z][a-z0-9]*
EOF      [E][O][F]

%%

{DIGIT}+    {
            printf( "We found an integer: %s (%d)\n", yytext,
                    atoi( yytext ) );
            }


{DIGIT}+"."{DIGIT}+ {
    printf( "We found a float: %s (%g)\n", yytext,
                    atof( yytext) );
}

{DIGIT}+([Ee][+-]?)?{DIGIT}+"."?{DIGIT}+([Ee][+-]?)?{DIGIT}+ {
            printf( "We found a float literal: %s (%10.4f)\n", yytext,
                    atof( yytext ) );
            }


if|then|begin|end|procedure|function        {
            printf( "We found a keyword: %s\n", yytext );
            }

{ID}        printf( "We found an identifier: %s\n", yytext );

"+"|"-"|"*"|"/"   printf( "We found an operator: %s\n", yytext );

"{"[^}\n]*"}"     /* eat up one-line comments */

[ \t\n]+          /* eat up whitespace */


{EOF}     return 43;

.           printf( "We found an unrecognized character: %s\n", yytext );

%%
