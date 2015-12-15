package tiger.Translate;

import tiger.Tree.*;

public class DecList extends Nx {

	ExpList elist;

	DecList(ExpList el) {
		super(null);
		elist = el;
		ExpList tmp = el;
		if (el == null) {
			stm = new EXP(new CONST(0));
			return;
		}
		if (el.tail == null) {
			stm = el.head.unNx();
			return;
		}
		Stm es = null, estmp = null;
		es = estmp = new SEQ(tmp.head.unNx(), tmp.tail.head.unNx());
		for (tmp = tmp.tail.tail; tmp != null; tmp = tmp.tail) {
			((SEQ) estmp).right = new SEQ(((SEQ) estmp).right, tmp.head.unNx());
			estmp = ((SEQ) estmp).right;
		}
		stm = es;
	}
}
