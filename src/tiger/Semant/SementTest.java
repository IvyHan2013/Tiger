package tiger.Semant;

import tiger.Absyn.*;
import tiger.Semant.*;
import tiger.parse.*;
import tiger.errormsg.ErrorMsg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SementTest {

	public static void main(String[] args) throws FileNotFoundException {
		String filename = "cuptest.txt";
		ErrorMsg errorMsg = new ErrorMsg(filename);
		InputStream inp = new FileInputStream(filename);
		Yylex lexer = new Yylex(inp, errorMsg);
		parser p = new parser(lexer);
		try {
			java_cup.runtime.Symbol s = p.parse();
			Print print = new Print(System.out);

			print.prExp((Exp) s.value, 0);
			System.out.println();
			Semant smt = new Semant(errorMsg);
			tiger.Frame.Frame mainFrame = new tiger.Mips.Frame(new tiger.Temp.Label("main"),null);
			tiger.Translate.Frag mainFrag = smt.transProg(mainFrame,
					(Exp) s.value);

			System.out.println("*********************over parse & typechecking************************");

			tiger.Tree.Print irTreePrint = new tiger.Tree.Print(System.out);
			tiger.Translate.Frag itFrag = mainFrag;
			for (; itFrag != null; itFrag = itFrag.next)
				if (itFrag instanceof tiger.Translate.ProcFrag) {
					System.out.println("**************************"+((tiger.Translate.ProcFrag) itFrag).frame.name);
					irTreePrint.prStm(((tiger.Translate.ProcFrag) itFrag).body);
					System.out.println("***************test frag***************************");
				}
			
			System.out.println("*********************over IR************************");
			
			for (itFrag=mainFrag; itFrag != null; itFrag = itFrag.next)
				if (itFrag instanceof tiger.Translate.ProcFrag) {
					System.out.println("canonical **********************"+((tiger.Translate.ProcFrag) itFrag).frame.name);
					tiger.Tree.StmList stml,stmlIt;
					stml=tiger.Canon.Canon.linearize(((tiger.Translate.ProcFrag) itFrag).body);
					tiger.Canon.BasicBlocks basicb=new tiger.Canon.BasicBlocks(stml);
					tiger.Canon.TraceSchedule traces=new tiger.Canon.TraceSchedule(basicb);
					for (stmlIt=traces.stms;stmlIt!=null;stmlIt=stmlIt.tail)
						irTreePrint.prStm(stmlIt.head);
					System.out.println("***************test canonical frag***************************");
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
