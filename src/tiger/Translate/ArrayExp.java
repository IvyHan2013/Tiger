package tiger.Translate;

import tiger.Tree.*;
import tiger.Temp.*;

public class ArrayExp extends Ex {

	Exp index, init;

	ArrayExp(Exp id, Exp ini) {
		super(null);
		index = id;
		init = ini;
		exp = new CALL(new NAME(Translate.initArray), new tiger.Tree.ExpList(
				new tiger.Tree.CONST(0), new tiger.Tree.ExpList(index.unEx(),
						new tiger.Tree.ExpList(init.unEx(), null))));
	}

}
