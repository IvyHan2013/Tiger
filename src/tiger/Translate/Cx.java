package tiger.Translate;

import tiger.Temp.*;
import tiger.Tree.*;

abstract class Cx extends Exp {

	public AbsExp unEx() {
		Temp r = new Temp();
		TEMP rr = new TEMP(r);
		Label t = new Label();
		Label f = new Label();
		//rr=1
		//if (exp!=0)goto T else goto F
		//LABLE f
		//rr=0
		//LABEL t
		//return rr
		return new ESEQ(new SEQ(new MOVE(rr, new CONST(1)), new SEQ(unCx(t, f),
				new SEQ(new LABEL(f), new SEQ(new MOVE(rr, new CONST(0)),
						new LABEL(t))))), rr);
	}

	public Stm unNx() {
		Temp r = new Temp();
		TEMP rr = new TEMP(r);
		Label t = new Label();
		Label f = new Label();
		
		return new SEQ(new MOVE(rr, new CONST(1)), new SEQ(unCx(t, f),
				new SEQ(new LABEL(f), new SEQ(new MOVE(rr, new CONST(0)),
						new LABEL(t)))));
	}

	public abstract Stm unCx(Label t, Label f);
}
