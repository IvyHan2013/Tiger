package tiger.Translate;

import tiger.Temp.*;
import tiger.Tree.*;

public class LetExp extends Exp {

	Exp dec, exp;

	LetExp(Exp d, Exp b) {
		dec = d;
		exp = b;
	}

	public AbsExp unEx() {
		return new ESEQ(dec.unNx(), exp.unEx());
	}

	public Stm unNx() {
		return new SEQ(dec.unNx(), exp.unNx());
	}

	public Stm unCx(Label t, Label f) {
		return new CJUMP(CJUMP.NE, unEx(), new CONST(0), t, f);
	}
}
