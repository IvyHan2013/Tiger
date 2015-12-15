/* JFlex example: part of Tiger language lexer specification */
package tiger.parse;
import tiger.errormsg.ErrorMsg;
import java_cup.runtime.*;

/**
 * This class is a Tiger lexer.
 */
%%
%unicode
%cup
%line
%column
%char

%{

private void newline() {
  errorMsg.newline(yychar);
}

private void err(int pos, String s) {
	errorMsg.error(pos, s);
}

private void err(String s) {
	err(yychar, s);
}

private java_cup.runtime.Symbol tok(int kind) {    
	return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), null);
}

private java_cup.runtime.Symbol tok(int kind, Object value) {    
	return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}


public Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg = e;
}

private ErrorMsg errorMsg;

  StringBuffer string = new StringBuffer();
  int commentstate=0;

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }

%}    

LineTerminator = \r\n|\r|\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

Identifier = [:jletter:] [:jletterdigit:]*
DecIntegerLiteral =[0-9]+
%state STRING
%state COMMENT

%%

/* keywords */
<YYINITIAL> "array"              { return tok(sym.ARRAY); }
<YYINITIAL> "break"              { return tok(sym.BREAK); }
<YYINITIAL> "do"	             { return tok(sym.DO); }
<YYINITIAL> "else"               { return tok(sym.ELSE); }
<YYINITIAL> "end"                { return tok(sym.END); }
<YYINITIAL> "for"                { return tok(sym.FOR); }
<YYINITIAL> "function"           { return tok(sym.FUNCTION); }
<YYINITIAL> "if"                 { return tok(sym.IF); }
<YYINITIAL> "in"                 { return tok(sym.IN); }
<YYINITIAL> "let"                { return tok(sym.LET); }
<YYINITIAL> "nil"                { return tok(sym.NIL); }
<YYINITIAL> "of"                 { return tok(sym.OF); }
<YYINITIAL> "then"               { return tok(sym.THEN); }
<YYINITIAL> "to"                 { return tok(sym.TO); }
<YYINITIAL> "type"               { return tok(sym.TYPE); }
<YYINITIAL> "var"                { return tok(sym.VAR); }
<YYINITIAL> "while"              { return tok(sym.WHILE); }


<YYINITIAL> {
  /* identifiers */ 
  {Identifier}                   { return tok(sym.ID, yytext()); }

  /* number */ 
  {DecIntegerLiteral}            { return tok(sym.INT, new Integer(yytext())); }

  /* comment */ 
  "/*"				 { commentstate=1; yybegin(COMMENT); }

  /* string */ 
  \"      	                 { string.setLength(0); yybegin(STRING); }

  /* operators */
  "("                            { return tok(sym.LPAREN); }
  ")"                            { return tok(sym.RPAREN); }
  "{"                            { return tok(sym.LBRACE); }
  "}"                            { return tok(sym.RBRACE); }
  "["                            { return tok(sym.LBRACK); }
  "]"                            { return tok(sym.RBRACK); }
  ";"                            { return tok(sym.SEMICOLON); }
  ","                            { return tok(sym.COMMA); }
  "."                            { return tok(sym.DOT); }
  "+"                            { return tok(sym.PLUS); }
  "-"                            { return tok(sym.MINUS); }
  "*"                            { return tok(sym.TIMES); }
  "/"                            { return tok(sym.DIVIDE); }
  "&"                            { return tok(sym.AND); }
  "|"                            { return tok(sym.OR);}
  ":="                           { return tok(sym.ASSIGN); }
  "="	                         { return tok(sym.EQ); }
  "<="                           { return tok(sym.LE); }
  ">="                           { return tok(sym.GE); }
  ">"                            { return tok(sym.GT); }
  "<"                            { return tok(sym.LT); }
  ":"							 { return tok(sym.COLON); }
  "<>"                           { return tok(sym.NEQ); }	
  
  /* lineterminal */
  {LineTerminator}				 { newline(); }
 
  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
  
}

<COMMENT> {
  "/*"				 { commentstate++; }
  "*/"    			 { commentstate--; if (commentstate==0) yybegin(YYINITIAL); }
  <<EOF>>			 { throw new Error("Illegal comment <no end of comment>"); }
  [^]   			 { /* ignore */ }
}

<STRING> {
  \"                             { yybegin(YYINITIAL); 
                                   return tok(sym.STRING,string.toString()); }
  [^\n\"\\]+                     { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }
  \\\"                            { string.append('\"'); }
  \\                             { string.append('\\'); }
  \\[0-3]?[0-9][0-9]             { char val = (char) Integer.parseInt(yytext().substring(1),8);
                        				   string.append( val ); }
  <<EOF>>			 { throw new Error("Illegal string <no end of string>"); }
}
/* error fallback */
[^]                             { throw new Error("Illegal character <"+
                                                    yytext()+">"); }