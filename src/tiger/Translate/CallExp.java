package tiger.Translate;

import tiger.Tree.AbsExp;
import tiger.Tree.CALL;
import tiger.Tree.MEM;
import tiger.Tree.NAME;
import tiger.Tree.TEMP;
import tiger.Temp.*;

class CallExp extends Ex {

	Temp rv;

	ExpList el;

	CallExp(ExpList eel, Level caller, Level callee, boolean sysFun) {
		super(null);

		// System.out.println(eel+" "+caller+" "+callee);

		rv = caller.frame.RV();
		el = eel;
		tiger.Tree.ExpList argu = null, tail = null;
		if (el != null) {
			argu = tail = new tiger.Tree.ExpList(el.head.unEx(), null);
			for (el = el.tail; el != null; el = el.tail) {
				tail.tail = new tiger.Tree.ExpList(el.head.unEx(), null);
				tail = tail.tail;
			}
		}

		if (sysFun)// system function ,no static
			exp = caller.frame.externalCall(callee.frame.name.toString(), argu);
		else {
			tiger.Tree.AbsExp sl;
			if (caller == callee.parent)
				sl = new TEMP(caller.frame.FP());
			else {
				sl = new TEMP(caller.frame.FP());
				Level ite;
				for (ite = caller; ite != callee.parent; ite = ite.parent)
					sl = new MEM(sl);
			}
			exp = new CALL(new NAME(callee.frame.name), new tiger.Tree.ExpList(
					sl, argu));
		}
	}
}