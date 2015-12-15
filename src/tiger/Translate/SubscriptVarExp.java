package tiger.Translate;

import tiger.Tree.BINOP;
import tiger.Tree.CONST;
import tiger.Tree.MEM;

public class SubscriptVarExp extends Ex {
	Exp title, ind;

	SubscriptVarExp(Exp e, Exp index, Level l) {
		super(null);
		title = e;
		ind = index;
		exp = new MEM(new BINOP(BINOP.PLUS, e.unEx(), new BINOP(BINOP.MUL,
				index.unEx(), new CONST(l.frame.wordSize()))));
	}

}
