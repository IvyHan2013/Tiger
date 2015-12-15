package tiger.parse;

import tiger.Absyn.*;
import tiger.Semant.*;
import tiger.errormsg.ErrorMsg;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CupTest {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String filename = "cuptest.txt";
		ErrorMsg errorMsg = new ErrorMsg(filename);
		InputStream inp = new FileInputStream(filename);
		Yylex lexer = new Yylex(inp, errorMsg);
		parser p = new parser(lexer);
		try {
			java_cup.runtime.Symbol s=p.parse();
			Print print=new Print(System.out);
			print.prExp((Exp)s.value, 0);
			System.out.println();
			Semant smt=new Semant(errorMsg);
//			smt.transProg((Exp)s.value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
