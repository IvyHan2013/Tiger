package tiger.Translate;

import tiger.Tree.*;
import tiger.Temp.*;

public class ForExp extends Nx {
	Access access;

	Exp init, limit, body;

	ForExp(Access acc, Exp lo, Exp hi, Exp bo, Label done) {
		super(null);
		access = acc;
		init = lo;
		limit = hi;
		body = bo;
		AbsExp forIt = new SimpleVarExp(acc, acc.home).unEx();
		AbsExp hlimit = new TEMP(new Temp());
		// ***************************************
		// forit=init;
		// hlimit=limit
		// while (forit<hlimit)
		// do{body;forit:=forit+1}
		// ***************************************

		// stm = new SEQ(new MOVE(forIt, init.unEx()), new SEQ(new MOVE(hlimit,
		// limit.unEx()), new WhileExp(new RelCx(CJUMP.LE, new Ex(forIt),
		// new Ex(hlimit)), new Nx(new SEQ(body.unNx(), new MOVE(forIt,
		// new BINOP(BINOP.PLUS, forIt, new CONST(1))))), done).unNx()));
		stm = new SEQ(new MOVE(forIt, init.unEx()), new WhileExp(new RelCx(
				CJUMP.LE, new Ex(forIt), hi), new Nx(new SEQ(body.unNx(),
				new MOVE(forIt, new BINOP(BINOP.PLUS, forIt, new CONST(1))))),
				done).unNx());

	}
}
