import tiger.Absyn.Exp;
import tiger.Absyn.Print;
import tiger.Frame.Frame;
import tiger.Semant.Semant;
import tiger.Translate.Frag;
import tiger.errormsg.ErrorMsg;
import tiger.parse.Yylex;
import tiger.parse.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class Driver {

	static tiger.Frame.Frame frame = new tiger.Mips.Frame();

	public static void main(String args[]) throws Exception{
		tiger.Main.Main mainfun=new tiger.Main.Main();
//		mainfun.run(args);
		 String filename[] = {"test9.tig"};
		 mainfun.run(filename);}

}