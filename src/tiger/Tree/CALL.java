package tiger.Tree;

import tiger.Temp.Temp;
import tiger.Temp.Label;

public class CALL extends AbsExp {
	public AbsExp func;

	public ExpList args;

	public CALL(AbsExp f, ExpList a) {
		func = f;
		args = a;
	}

	public ExpList kids() {
		return new ExpList(func, args);
	}

	public AbsExp build(ExpList kids) {
		return new CALL(kids.head, kids.tail);
	}

}
