package tiger.Mips;

import tiger.Semant.Semant;
import tiger.Translate.Translate;
import tiger.parse.parser;

class CodegenTest {

	static tiger.Frame.Frame frame = new tiger.Mips.Frame();

	static void prStmList(tiger.Tree.Print print, tiger.Tree.StmList stms) {
		for (tiger.Tree.StmList l = stms; l != null; l = l.tail)
			print.prStm(l.head);
	}

	static tiger.Assem.InstrList codegen(tiger.Frame.Frame f,
			tiger.Tree.StmList stms) {

		tiger.Assem.InstrList first = null, last = null;
		for (tiger.Tree.StmList s = stms; s != null; s = s.tail) {
			tiger.Assem.InstrList i = f.codegen(s.head);

			if (last == null) {
				first = last = i;
			} else {
				while (last.tail != null)
					last = last.tail;
				last = last.tail = i;
			}
		}
		return first;
	}

	static void emitProc(java.io.PrintStream out, tiger.Translate.ProcFrag f) {
		java.io.PrintStream debug =
		/* new java.io.PrintStream(new NullOutputStream()); */
		out;
		tiger.Temp.TempMap tempmap = new tiger.Temp.CombineMap(f.frame,
				new tiger.Temp.DefaultMap());
		tiger.Tree.Print print = new tiger.Tree.Print(debug, tempmap);
		debug.println("# Before canonicalization: ");
		print.prStm(f.body);
		debug.println("# After canonicalization: ");
		tiger.Tree.StmList stms = tiger.Canon.Canon.linearize(f.body);
		prStmList(print, stms);
		debug.println("# Basic Blocks: ");
		tiger.Canon.BasicBlocks b = new tiger.Canon.BasicBlocks(stms);
		for (tiger.Canon.StmListList l = b.blocks; l != null; l = l.tail) {
			debug.println("#");
			prStmList(print, l.head);
		}
		print.prStm(new tiger.Tree.LABEL(b.done));
		debug.println("# Trace Scheduled: ");
		tiger.Tree.StmList traced = (new tiger.Canon.TraceSchedule(b)).stms;
		prStmList(print, traced);

		tiger.Assem.InstrList instrs = codegen(f.frame, traced);
		debug.println("# Instructions: ");

		for (tiger.Assem.InstrList p = instrs; p != null; p = p.tail)
			debug.print(p.head.format(tempmap));
	}

	public static void main(String args[]) throws java.io.IOException {
		String testTxt = "cuptest.txt";// debuging
		tiger.parse.Runable parseRun = new tiger.parse.Runable(testTxt);
		tiger.Absyn.Exp parseExp = parseRun.Run();
		java.io.PrintStream out = new java.io.PrintStream(System.out);
		// new java.io.FileOutputStream(testTxt + ".s"));
		tiger.Translate.Translate translate = new tiger.Translate.Translate(
				frame);
		Semant semant = new Semant(translate, parseRun.errorMsg);
		tiger.Translate.Frag frags = semant.transProg(translate.mainFrame,
				parseExp);
		for (tiger.Translate.Frag f = frags; f != null; f = f.next)
			if (f instanceof tiger.Translate.ProcFrag)
				emitProc(out, (tiger.Translate.ProcFrag) f);
			else if (f instanceof tiger.Translate.DataFrag)
				out.print(((tiger.Translate.DataFrag) f).data);
		out.close();
	}
}

class NullOutputStream extends java.io.OutputStream {
	public void write(int b) {
	}
}