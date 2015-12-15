package tiger.FindEscape;

public class VarEscape extends Escape {
	tiger.Absyn.VarDec vd;

	VarEscape(int d, tiger.Absyn.VarDec v) {
		depth = d;
		vd = v;
		vd.escape = false;
	}

	void setEscape() {
		vd.escape = true;
	}
}
