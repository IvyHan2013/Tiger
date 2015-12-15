package tiger.Tree;

import tiger.Temp.Temp;
import tiger.Temp.Label;

public class NAME extends AbsExp {
	public Label label;

	public NAME(Label l) {
		label = l;
	}

	public ExpList kids() {
		return null;
	}

	public AbsExp build(ExpList kids) {
		return this;
	}
}
