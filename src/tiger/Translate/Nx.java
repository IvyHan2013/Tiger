package tiger.Translate;

import tiger.Temp.*;

public class Nx extends Exp {
	tiger.Tree.Stm stm;

	Nx(tiger.Tree.Stm s) {
		stm = s;
	}

	public tiger.Tree.AbsExp unEx() {
		return null;
	}

	public tiger.Tree.Stm unNx() {
		return stm;
	}

	public tiger.Tree.Stm unCx(Label t, Label l) {
		return null;
	}
}
