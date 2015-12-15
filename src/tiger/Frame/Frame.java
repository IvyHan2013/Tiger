package tiger.Frame;

import tiger.Temp.*;
import tiger.Tree.*;
import tiger.Util.BoolList;
import tiger.Assem.*;

public abstract class Frame implements TempMap {
	public Label name;

	public AccessList formals;

	public static final Label error = new Label("error");

	public static Label getError() {
		return error;
	}

	abstract public Frame newFrame(Label name, BoolList formals);

	abstract public Access allocLocal(boolean escape);

	abstract public Access allocArguLocal(boolean escape);
	
	abstract public int Offset();

	abstract public Temp FP();

	abstract public Temp SP();

	abstract public Temp RV();

	abstract public Temp RA();
	
	abstract public Temp ARGU(int n);
	
	abstract public TempList freeRegs();
	
	abstract public TempList registers();

	abstract public String tempMap(Temp temp);

	abstract public int wordSize();

	abstract public AbsExp externalCall(String func, ExpList args);

	abstract public String string(Label lab, String lit);

	abstract public Stm procEntryExit1(Stm body);

	abstract public InstrList procEntryExit2(InstrList body);

	abstract public Proc procEntryExit3(InstrList body);

	abstract public InstrList codegen(Stm stm);

	// abstract public Temp newTemp();

	static public InstrList append(InstrList a, InstrList b) {
		if (a == null)
			return b;
		if (b == null)
			return a;
		InstrList p;
		for (p = a; p.tail != null; p = p.tail)
			;
		p.tail = b;
		return a;
	}

	static public TempList L(Temp h, TempList t) {
		return new TempList(h, t);
	}

	// abstract public static String programTail();
}