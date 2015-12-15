package tiger.Translate;

import tiger.Tree.BINOP;
import tiger.Tree.CONST;
import tiger.Tree.MEM;

public class FieldVarExp extends Ex {
	Exp title;

	int si;

	FieldVarExp(Exp e, int size, Level l) {
		super(null);
		title = e;
		si = size;
		exp = (new MEM(new BINOP(BINOP.PLUS, e.unEx(), new CONST(l.frame
				.wordSize()
				* size))));
	}
}
