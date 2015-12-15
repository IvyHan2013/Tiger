package tiger.Translate;

import tiger.Temp.Label;
import tiger.Tree.NAME;

public class StringExp extends Ex {
	String value;

	StringExp(String v, Level l) {
		super(null);
		value=v;
		Label lab = new Label();
		Translate.datafrag = new DataFrag(l.frame.string(lab, value),
				Translate.datafrag);
		exp = new NAME(lab);
	}
}
