package tiger.Semant;

import tiger.Types.*;

public class VarEntry extends Entry {
	tiger.Translate.Access access;

	Type ty;

	boolean isForIt = false;

	tiger.Translate.Exp init;

	VarEntry(tiger.Translate.Exp e, Type t) {
		ty = t;
		init = e;
		access = null;
	}

	VarEntry(tiger.Translate.Access acc, tiger.Translate.Exp e, Type t) {
		ty = t;
		init = e;
		access = acc;
	}
}
