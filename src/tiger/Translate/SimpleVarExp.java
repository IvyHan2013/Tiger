package tiger.Translate;

import tiger.Tree.AbsExp;
import tiger.Tree.MEM;
import tiger.Tree.TEMP;

public class SimpleVarExp extends Ex {
	SimpleVarExp(Access acc, Level l) {
		super(null);
		AbsExp ep = new TEMP(l.frame.FP());
		Level par = l;
		for (; par != acc.home; par = par.parent)
			ep = new MEM(ep);
		// debuging
		// System.out.println("*********** "+acc+"\n"+acc.acc);

		exp = acc.acc.exp(ep);
	}
}
