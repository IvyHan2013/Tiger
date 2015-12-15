package tiger.Translate;

import tiger.Tree.MOVE;
import tiger.Tree.TEMP;

public class VarDecExp extends Nx {

	public Exp ini;

	public Access access;

	VarDecExp(Access acc, Exp init) {
		super(new MOVE(acc.acc.exp(new TEMP(acc.home.frame.FP())), init.unEx()));
		ini = init;
		access = acc;
	}

}
