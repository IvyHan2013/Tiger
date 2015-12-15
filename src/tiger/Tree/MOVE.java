package tiger.Tree;

import tiger.Temp.Temp;
import tiger.Temp.Label;

public class MOVE extends Stm {
	public AbsExp dst, src;

	public MOVE(AbsExp d, AbsExp s) {
		dst = d;
		src = s;
	}

	public ExpList kids() {
		if (dst instanceof MEM)
			return new ExpList(((MEM) dst).exp, new ExpList(src, null));
		else
			return new ExpList(src, null);
	}

	public Stm build(ExpList kids) {
		if (dst instanceof MEM)
			return new MOVE(new MEM(kids.head), kids.tail.head);
		else
			return new MOVE(dst, kids.head);
	}
}
