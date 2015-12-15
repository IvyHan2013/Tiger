package tiger.Translate;

import tiger.Tree.BINOP;

public class OpExp extends Ex {
	Exp left, right;

	int op;

	OpExp(int oper, Exp l, Exp r) {
		super(null);
		op = oper;
		left = l;
		right = r;
		exp = new BINOP(op, left.unEx(), right.unEx());
	}

}
