package tiger.Translate;

import tiger.Temp.*;
import tiger.Tree.*;

public class Ex extends Exp {
	AbsExp exp;

	Ex(AbsExp e) {
		exp = e;
	}

	public AbsExp unEx() {
		return exp;
	}

	public Stm unNx() {
		return new EXP(exp);
	}

	public Stm unCx(Label t, Label f) {
		return new CJUMP(CJUMP.NE, exp,
				new CONST(0), t, f);
	}
}
