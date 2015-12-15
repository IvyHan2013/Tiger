package tiger.Translate;

import tiger.Tree.JUMP;
import tiger.Temp.*;

public class BreakExp extends Nx {
	Label out;

	BreakExp(Label o) {
		super(new JUMP(o));
		out = o;

	}
}
