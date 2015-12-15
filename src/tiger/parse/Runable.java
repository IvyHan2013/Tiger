package tiger.parse;

import java.io.*;

import tiger.Absyn.Exp;
import tiger.Absyn.Print;
import tiger.Semant.Semant;
import tiger.errormsg.ErrorMsg;

public class Runable {
	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public String filename = "cuptest.txt";

	public String subfilename = "cuptest";

	public ErrorMsg errorMsg = new ErrorMsg(filename);

	public boolean errorFlag;

	public Runable() {
	}

	public Runable(String file) {
		filename = file;
	}

	public Exp Run() throws FileNotFoundException {
		errorFlag = false;
		int i;
		subfilename = filename;
		for (i = filename.length() - 1; i >= 0; i--)
			if (subfilename.charAt(i) == '.')
				break;
		if (i > 0)
			subfilename = subfilename.substring(0, i);
		InputStream inp = new FileInputStream(filename);
		Yylex lexer = new Yylex(inp, errorMsg);
		parser p = new parser(lexer);
		java_cup.runtime.Symbol s = null;
		PrintStream out = new java.io.PrintStream(new java.io.FileOutputStream(filename
		+ ".abs"));
		System.out.println("/******************Syntactic Analysis********************/");
		try {
			s = p.parse();
			errorFlag = errorMsg.anyErrors;

			 Print print = new Print(out);
			 print.prExp((Exp) s.value, 0);
			 Semant smt = new Semant(errorMsg);
			 //smt.transProg((Exp) s.value);

			 if (!errorMsg.anyErrors) {
			 System.out.println("parsed successfully, " + subfilename
			 + ".abs has been generated.");
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (Exp) s.value;

		// return p;
	}
}
