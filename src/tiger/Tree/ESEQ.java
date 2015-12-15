package tiger.Tree;

import tiger.Temp.Temp;
import tiger.Temp.Label;

public class ESEQ extends AbsExp {
	public Stm stm;

	public AbsExp exp;

	public ESEQ(Stm s, AbsExp e) {
		stm = s;
		exp = e;
	}

	public ExpList kids() {
		throw new Error("kids() not applicable to ESEQ");
	}

	public AbsExp build(ExpList kids) {
		throw new Error("build() not applicable to ESEQ");
	}
}
