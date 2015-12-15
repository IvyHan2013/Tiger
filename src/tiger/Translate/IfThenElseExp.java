package tiger.Translate;

import tiger.Tree.*;
import tiger.Temp.*;

public class IfThenElseExp extends Exp {

	Exp cond, th, el;

	Label t = new Label();

	Label f = new Label();

	IfThenElseExp(Exp cc, Exp aa, Exp bb) {
		cond = cc;
		th = aa;
		el = bb;
	}

	public AbsExp unEx() {

		Stm condtest = cond.unCx(t, f);
		Label out = new Label();
		TEMP r = new TEMP(new Temp());
		Stm thcase = new SEQ(new LABEL(t), new SEQ(new MOVE(r, th.unEx()),
				new JUMP(out)));
		Stm elCase = new SEQ(new LABEL(f), new SEQ(new MOVE(r, el.unEx()),
				new JUMP(out)));
		return new ESEQ(new SEQ(condtest, new SEQ(thcase, new SEQ(elCase,
				new LABEL(out)))), r);
	}

	public Stm unNx() {

		Stm condtest = cond.unCx(t, f);
		Label out = new Label();
		Stm thcase = new SEQ(new LABEL(t), new SEQ(th.unNx(), new JUMP(out)));
		Stm elCase = new SEQ(new LABEL(f), new SEQ(el.unNx(), new JUMP(out)));
		return new SEQ(condtest, new SEQ(thcase,
				new SEQ(elCase, new LABEL(out))));
	}

	public Stm unCx(Label tt, Label ff) {
		return new CJUMP(CJUMP.NE, unEx(), new CONST(0), tt, ff);
	}

}
