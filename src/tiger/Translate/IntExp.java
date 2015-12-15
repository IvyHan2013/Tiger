package tiger.Translate;

import tiger.Tree.CONST;

public class IntExp extends Ex {
	int value;

	IntExp(int v) {
		super(new CONST(v));
		value = v;

	}
}
