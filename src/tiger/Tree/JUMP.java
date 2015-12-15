package tiger.Tree;

import tiger.Temp.Temp;
import tiger.Temp.Label;

public class JUMP extends Stm {
	public AbsExp exp;

	public tiger.Temp.LabelList targets;

	public JUMP(AbsExp e, tiger.Temp.LabelList t) {
		exp = e;
		targets = t;
	}

	public JUMP(tiger.Temp.Label target) {
		this(new NAME(target), new tiger.Temp.LabelList(target, null));
	}

	public ExpList kids() {
		return new ExpList(exp, null);
	}

	public Stm build(ExpList kids) {
		return new JUMP(kids.head, targets);
	}
}
