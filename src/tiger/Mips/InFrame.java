package tiger.Mips;

import tiger.Tree.*;

public class InFrame extends tiger.Frame.Access {
	int offset;

	InFrame(int off) {
		super();
		offset = off;
	}

	public AbsExp exp(AbsExp framePtr) {
		return new MEM(new BINOP(BINOP.PLUS, framePtr, new CONST(offset)));
	}
}
