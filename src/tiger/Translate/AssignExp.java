package tiger.Translate;

import tiger.Tree.MOVE;

public class AssignExp extends Nx {

	Exp left, right;

	AssignExp(Exp l, Exp r) {
		super(new MOVE(l.unEx(), r.unEx()));
		left = l;
		right = r;
	}
}
