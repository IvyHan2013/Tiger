package tiger.Mips;

import tiger.Tree.AbsExp;
import tiger.Temp.*;

public class InReg extends tiger.Frame.Access {
	Temp temp;

	public InReg() {
		temp = new Temp();
	}

	public AbsExp exp(AbsExp framePtr) {
		return new tiger.Tree.TEMP(temp);
	}
}