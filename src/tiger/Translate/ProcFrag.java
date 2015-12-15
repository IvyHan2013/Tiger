package tiger.Translate;

import tiger.Tree.*;
import tiger.Frame.*;

public class ProcFrag extends Frag {
	public Stm body;

	public Frame frame;



	public ProcFrag(Stm b, Frame f, Frag n) {
		body = b;
		frame = f;
		next = n;
	}
}
