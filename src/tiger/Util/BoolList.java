package tiger.Util;

public class BoolList {
	public boolean head;

	public BoolList tail;

	public BoolList(boolean h, BoolList t) {
		head = h;
		tail = t;
	}

	public BoolList(tiger.Absyn.FieldList a) {
		tiger.Absyn.FieldList pt;
		for (pt = a; pt != null;) {
			head = pt.escape;
			pt = pt.tail;
			if (pt == null)
				tail = null;
			else
				tail = new BoolList(pt);
		}
	}
}


