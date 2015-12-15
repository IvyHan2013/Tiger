package tiger.Translate;

import tiger.Temp.*;
import tiger.Util.*;
import tiger.Tree.*;

public class Translate {
	public static Frag datafrag = null;

	public static Label malloc = new Label("mollac");

	public static Label initArray = new Label("initArray");
	
	public static Label allocRecord=new Label("allocRecord");

	public static tiger.Frame.Frame mainFrame = new tiger.Mips.Frame();

	public Translate() {
	}

	public Translate(tiger.Frame.Frame f) {
		mainFrame = f;
	}

	public Exp emptyExp() {
		return new EmptyExp();
	}

	public Exp simpleVar(Access acc, Level l) {
		return new SimpleVarExp(acc, l);
	}

	public Exp fieldVar(Exp e, int size, Level l) {
		return new FieldVarExp(e, size, l);
	}

	public Exp seqExp(ExpList el, Level l) {
		return new SeqExp(el);
	}

	public Exp subscriptVar(Exp e, Exp index, Level l) {
		return new SubscriptVarExp(e, index, l);
	}

	public Exp nilExp(Level l) {
		return new NilExp();
	}

	public Exp intExp(int value, Level l) {
		return new IntExp(value);
	}

	public Exp stringExp(String value, Level l) {
		return new StringExp(value, l);
	}

	public Exp callExp(ExpList el, Level caller, Level callee, boolean sysFun) {
		return new CallExp(el, caller, callee, sysFun);
	}

	public Exp opExp(int op, Exp left, Exp right, Level l) {
		return new OpExp(op, left, right);
	}

	public Exp relExp(int op, Exp left, Exp right, Level l) {
		return new RelCx(op, left, right);
	}

	public Exp recordExp(ExpList el, int size, Level l) {
		return new RecordExp(el, size, l);
	}

	public Exp assignExp(Exp left, Exp right, Level l) {
		return new AssignExp(left, right);
	}

	public Exp ifExp(Exp test, Exp th, Exp el) {
		return new IfThenElseExp(test, th, el);
	}

	public Exp whileExp(Exp test, Exp body, Label done, Level l) {
		return new WhileExp(test, body, done);
	}

	public Exp forExp(Access acc, Exp lo, Exp hi, Exp bo, Label done, Level l) {
		return new ForExp(acc, lo, hi, bo, done);
	}

	public Exp breakExp(Label outl, Level l) {
		return new BreakExp(outl);
	}

	public Exp letExp(Exp dec, Exp body) {
		return new LetExp(dec, body);
	}

	public Exp arrayExp(Exp index, Exp init, Level l) {
		return new ArrayExp(index, init);
	}

	public Exp varDec(Access acc, Exp init, Level l) {
		return new VarDecExp(acc, init);
	}

	public Exp decList(ExpList el, Level l) {
		return new DecList(el);
	}

	public void procEntryExit(Exp body, Level l, boolean exResult) {
		if (!exResult)
			datafrag = new ProcFrag(l.frame.procEntryExit1(body.unNx()),
					l.frame, datafrag);
		else
			datafrag = new ProcFrag(l.frame.procEntryExit1(new MOVE(new TEMP(
					l.frame.RV()), body.unEx())), l.frame, datafrag);
	}

	public Frag getResult() {
		return datafrag;
	}
}
