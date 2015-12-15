package tiger.Translate;

import tiger.Temp.Label;
import tiger.Tree.*;

public class SeqExp extends Exp {// undoing

	ExpList elist;

	SeqExp(ExpList el) {
		elist = el;
	}

	public AbsExp unEx() {
		if (elist == null)
			return new CONST(0);
		if (elist.tail == null) {
			if (exable(elist.head))
				return elist.head.unEx();
			else
				return new ESEQ(elist.head.unNx(), new CONST(0));
		}
		ExpList tmp;
		tmp = elist;
		Exp ended = tmp.tail.head;
		Stm es = null, estmp = null;
		es = estmp = new SEQ(tmp.head.unNx(), null);
		for (tmp = tmp.tail; tmp.tail != null; tmp = tmp.tail) {
			((SEQ) estmp).right = new SEQ(tmp.head.unNx(), null);
			estmp = ((SEQ) estmp).right;
			ended = tmp.tail.head;
		}
		if (exable(ended)) {
			((SEQ) estmp).right = new EXP(new CONST(0));
			return new ESEQ(es, ended.unEx());
		} else {
			((SEQ) estmp).right = ended.unNx();
			return new ESEQ(es, new CONST(0));
		}
	}

	public Stm unNx() {
		if (elist == null)
			return new EXP(new CONST(0));
		if (elist.tail == null)
			return elist.head.unNx();

		ExpList tmp;
		tmp = elist;
		Stm es = null, estmp = null;
		es = estmp = new SEQ(tmp.head.unNx(), null);
		for (tmp = tmp.tail; tmp.tail != null; tmp = tmp.tail) {
			((SEQ) estmp).right = new SEQ(tmp.head.unNx(), null);
			estmp = ((SEQ) estmp).right;
		}
		((SEQ) estmp).right = tmp.head.unNx();
		return es;
	}

	public Stm unCx(Label t, Label f) {
		return new CJUMP(CJUMP.NE, unEx(), new CONST(0), t, f);
	}

	boolean exable(Exp a) {
		return (a instanceof Ex); // || (a instanceof IfThenElseExp);
	}
}
