package tiger.Translate;

public class Level {
	tiger.Frame.Frame frame;

	Level parent;

	public AccessList formals;

	public Level(Level par, tiger.Symbol.Symbol name, tiger.Util.BoolList fmls) {
		frame = par.frame.newFrame(new tiger.Temp.Label(name), fmls);
		parent = par;
	}

	public Level(tiger.Frame.Frame f) {
		frame = f;
	}

	public Access allocLocal(boolean escape) {
		return new Access(this, frame.allocLocal(escape));
	}

	public Access allocArguLocal(boolean escape) {
		return new Access(this, frame.allocArguLocal(escape));
	}
}
