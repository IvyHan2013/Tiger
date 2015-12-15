package tiger.Tree;

public class TEMP extends AbsExp {
	public tiger.Temp.Temp temp;

	public TEMP(tiger.Temp.Temp t) {
		temp = t;
	}

	public ExpList kids() {
		return null;
	}

	public AbsExp build(ExpList kids) {
		return this;
	}
}
