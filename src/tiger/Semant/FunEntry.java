package tiger.Semant;

import tiger.Types.*;

public class FunEntry extends Entry {
	RECORD formals;

	Type result;

	tiger.Translate.Level level;

	tiger.Temp.Label label;

	boolean sysFun = false;

	public FunEntry(RECORD f, Type r) {
		formals = f;
		result = r;
	}

	public FunEntry(RECORD f, Type r, boolean sys) {
		formals = f;
		result = r;
		sysFun = sys;
	}

	public FunEntry(tiger.Translate.Level v, tiger.Temp.Label l, RECORD f,
			Type r) {
		formals = f;
		result = r;
		level = v;
		label = l;
	}

	public FunEntry(tiger.Translate.Level v, tiger.Temp.Label l, RECORD f,
			Type r, boolean sys) {
		formals = f;
		result = r;
		level = v;
		label = l;
		sysFun = sys;
	}
}
