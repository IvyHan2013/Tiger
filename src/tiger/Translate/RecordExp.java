package tiger.Translate;

import tiger.Temp.Temp;
import tiger.Tree.*;

public class RecordExp extends Ex {

	int size;

	ExpList el;

	RecordExp(ExpList eel, int si, Level l) {
		super(null);
		size = si;
		el = eel;
		Temp r = new Temp();
		Stm alloc = new MOVE(new TEMP(r), new CALL(new NAME(
				Translate.allocRecord), new tiger.Tree.ExpList(new CONST(0),
				new tiger.Tree.ExpList(new CONST(l.frame.wordSize() * size),
						null))));

		if (size == 0)
			exp = new ESEQ(alloc, new TEMP(r));

		SEQ seq = null, tmpseq = null;
		seq = tmpseq = new SEQ(alloc, new MOVE(new MEM(new BINOP(BINOP.PLUS,
				new TEMP(r), new CONST(0 * l.frame.wordSize()))), el.head
				.unEx()));
		int i;
		for (el = el.tail, i = 1; el != null; el = el.tail, i++) {
			tmpseq.right = new SEQ(tmpseq.right, new MOVE(
					new MEM(new BINOP(BINOP.PLUS, new TEMP(r), new CONST(i
							* l.frame.wordSize()))), el.head.unEx()));
			tmpseq = (SEQ) tmpseq.right;
		}
		exp = new ESEQ(seq, new TEMP(r));
	}

}
