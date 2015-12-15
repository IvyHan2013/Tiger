package tiger.Translate;

import tiger.Temp.Label;
import tiger.Tree.JUMP;
import tiger.Tree.LABEL;
import tiger.Tree.SEQ;
import tiger.Tree.Stm;

public class WhileExp extends Nx {

	Exp test, body;

	WhileExp(Exp t, Exp b, Label done) {
		super(null);
		test = t;
		body = b;
		Label testLabel = new Label();
		Label bodyLabel = new Label();
		Stm testjump = test.unCx(bodyLabel, done);

		// ************************************
		// testLabel:
		// if test bodyLabel
		// else done
		// bodyLabel:
		// Body
		// testLabel
		// done:
		// ************************************

		stm = new SEQ(new LABEL(testLabel), new SEQ(testjump, new SEQ(
				new LABEL(bodyLabel), new SEQ(body.unNx(), new SEQ(new JUMP(
						testLabel), new LABEL(done))))));

	}

}
