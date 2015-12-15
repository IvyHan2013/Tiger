package tiger.Translate;

import tiger.Temp.*;
import tiger.Tree.*;

public class RelCx extends Cx {
	int op;

	Exp left, right;

	RelCx(int o, Exp l, Exp r) {
		op = o;
		left = l;
		right = r;
	}

	public Stm unCx(Label t, Label f) {
		return new CJUMP(op, left.unEx(), right.unEx(), t, f);
	}
}
