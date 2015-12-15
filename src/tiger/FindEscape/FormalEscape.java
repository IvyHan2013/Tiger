package tiger.FindEscape;

public class FormalEscape extends Escape {
	tiger.Absyn.FieldList fl;

	FormalEscape(int d, tiger.Absyn.FieldList f) {
		depth = d;
		fl = f;
		fl.escape = false;
	}

	void setEscape() {
		fl.escape = true;
	}
}
