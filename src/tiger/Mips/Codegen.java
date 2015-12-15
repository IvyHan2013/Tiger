package tiger.Mips;

import tiger.Frame.*;
import tiger.Assem.*;
import tiger.Tree.*;
import tiger.Temp.*;

public class Codegen {
	tiger.Frame.Frame frame;

	InstrList ilist = null, last = null;

	public Codegen(tiger.Frame.Frame f) {
		frame = f;
	}

	TempList L(Temp h, TempList t) {
		return new TempList(h, t);
	}

	LabelList LL(Label l, LabelList t) {
		return new LabelList(l, t);
	}

	void emit(Instr inst) {
		if (last != null)
			last = last.tail = new InstrList(inst, null);
		else
			last = ilist = new InstrList(inst, null);
	}

	Temp munchExp(tiger.Tree.CONST s) {
		Temp r = new Temp();
		emit(new OPER("li `d0," + s.value + "\n", L(r, null), null));
		return r;
	}

	Temp munchExp(tiger.Tree.NAME s) {
		Temp r = new Temp();
		emit(new OPER("la `d0," + s.label.toString() + "\n", L(r, null), null));
		return r;
	}

	Temp munchExp(tiger.Tree.TEMP s) {
		return s.temp;
	}

	Temp munchExp(tiger.Tree.BINOP s) {// undoing

		String op = "";
		Temp r = new Temp();

		switch (s.binop) {
		case tiger.Tree.BINOP.PLUS:
			op = "add";
			break;
		case tiger.Tree.BINOP.MINUS:
			op = "sub";
			break;
		case tiger.Tree.BINOP.AND:
			op = "and";
			break;
		case tiger.Tree.BINOP.OR:
			op = "or";
			break;
		case tiger.Tree.BINOP.MUL:
			op = "mul";
			break;
		case tiger.Tree.BINOP.DIV:
			op = "div";
			break;

		default:
			new Error("MIPS can not support that operation");
		}

		if (s.left instanceof tiger.Tree.CONST
				|| s.right instanceof tiger.Tree.CONST) {// BINOP(OP,CONST(i),e)
			tiger.Tree.CONST c;
			tiger.Tree.AbsExp e;
			if (s.left instanceof tiger.Tree.CONST) {
				c = (tiger.Tree.CONST) s.left;
				e = s.right;
			} else {
				c = (tiger.Tree.CONST) s.right;
				e = s.left;
			}
			if (op == "sub" || op == "mul" || op == "div")
				emit(new OPER(op + " `d0,`s0,`s1\n", L(r, null), L(munchExp(e),
						L(munchExp(c), null))));
			else
				emit(new OPER(op + "i `d0,`s0," + c.value + "\n", L(r, null),
						L(munchExp(e), null)));

		} else
			emit(new OPER(op + " `d0,`s0,`s1\n", L(r, null), L(
					munchExp(s.left), L(munchExp(s.right), null))));

		return r;
	}

	Temp munchExp(tiger.Tree.MEM s) {// undoing
		Temp r = new Temp();
		if (s.exp instanceof BINOP) {
			BINOP bop = (BINOP) s.exp;
			if (bop.left instanceof CONST || bop.right instanceof CONST) {
				// lw $d,c($s)
				CONST c;
				AbsExp e;
				if (bop.left instanceof CONST) {
					c = (CONST) bop.left;
					e = bop.right;
				} else {
					c = (CONST) bop.right;
					e = bop.left;
				}
				emit(new OPER("lw `d0," + c.value + "(`s0)\n", L(r, null), L(
						munchExp(e), null)));
			} else
				emit(new OPER("lw `d0,0(`s0)\n", L(r, null), L(munchExp(s.exp),
						null)));
		} else
			emit(new OPER("lw `d0,0(`s0)\n", L(r, null), L(munchExp(s.exp),
					null)));
		return r;
	}

	int munchArgs(int deep, tiger.Tree.ExpList argu) {// undoing
		if (argu == null)
			return 0;
		int n = munchArgs(deep + 1, argu.tail);
		Temp r = frame.ARGU(deep);

		if (r != null)
			emit(new OPER("add `d0,`s0,$zero\n", L(r, null), L(
					munchExp(argu.head), null)));
		else {
			emit(new OPER("addi `d0,`s0," + -frame.wordSize() + "\n", L(frame
					.SP(), null), L(frame.SP(), null)));
			emit(new OPER("sw `s1,0(`s0)\n", null, L(frame.SP(), L(
					munchExp(argu.head), null))));
		}
		return n + 1;
	}

	Temp munchExp(tiger.Tree.CALL s) {// undoing

		tiger.Mips.Frame mf = (tiger.Mips.Frame) frame;
		int l = munchArgs(0, s.args.tail);

		// save static link
		emit(new OPER("addi `d0,`s0," + -frame.wordSize() + "\n", L(frame.SP(),
				null), L(frame.SP(), null)));
		emit(new OPER("sw `s1,0(`s0)\n", null, L(frame.SP(), L(
				munchExp(s.args.head), null))));

		emit(new OPER("jal  " + ((NAME) s.func).label + "\n", null, null));
		// mf.callerSaves, mf.argres));

		// unmunchArgs
		if (l <= 4)
			emit(new OPER("addi `d0,`s0," + frame.wordSize() + "\n", L(frame
					.SP(), null), L(frame.SP(), null)));
		else
			emit(new OPER("addi `d0,`s0," + (l - 4 + 1) * frame.wordSize()
					+ "\n", L(frame.SP(), null), L(frame.SP(), null)));
		return frame.RV();
	}

	Temp munchExp(tiger.Tree.ESEQ s) {
		// undoing
		// can`t reacheable
		return null;
	}

	Temp munchExp(AbsExp s) {
		if (s instanceof tiger.Tree.CONST)
			return munchExp((tiger.Tree.CONST) s);
		if (s instanceof tiger.Tree.NAME)
			return munchExp((tiger.Tree.NAME) s);
		if (s instanceof tiger.Tree.MEM)
			return munchExp((tiger.Tree.MEM) s);
		if (s instanceof tiger.Tree.TEMP)
			return munchExp((tiger.Tree.TEMP) s);
		if (s instanceof tiger.Tree.BINOP)
			return munchExp((tiger.Tree.BINOP) s);
		if (s instanceof tiger.Tree.CALL)
			return munchExp((tiger.Tree.CALL) s);
		if (s instanceof tiger.Tree.ESEQ)
			return munchExp((tiger.Tree.ESEQ) s);
		return null;
	}

	void munchStm(tiger.Tree.SEQ s) {// SEQ(a,b)
		munchStm(s.left);
		munchStm(s.right);
	}

	void munchStm(tiger.Tree.MOVE s) {// undoing

		if (s.dst instanceof TEMP) { // MOVE(TEMP(i),e2)
			emit(new OPER("add  `d0, `s0,  $zero\n", L(((TEMP) s.dst).temp,
					null), L(munchExp(s.src), null)));
			// saveReg(((TEMP) s.dst).temp);
			return;
		}

		if (s.src instanceof MEM) {// MOVE(MEM(e1),MEM(e2))
			Temp t0 = new Temp(), t1 = new Temp(), t2 = new Temp();
			emit(new OPER("add `d0,`s0, $zero\n", L(t0, null), L(
					munchExp(((MEM) s.src).exp), null)));
			emit(new OPER("lw `d0,0(`s0)\n", L(t1, null), L(t0, null)));
			emit(new OPER("add `d0,`s0,  $zero\n", L(t2, null), L(
					munchExp(((MEM) s.dst).exp), null)));
			emit(new OPER("sw `s1,0(`s0)\n", null, L(t2, L(t1, null))));
			return;
		}

		MEM mdst = (MEM) s.dst;

		if (mdst.exp instanceof BINOP) {
			// MOVE(BINOP(PLUS,e1,const(i)),e2)
			// MOVE(BINOP(PLUS,const(i),e1),e2)
			BINOP bop = (BINOP) mdst.exp;
			if (bop.binop != bop.PLUS) {
				new Error("mem must be plus\n");
				return;
			}

			if (bop.left instanceof CONST || bop.right instanceof CONST) {
				AbsExp e1;
				CONST c;
				if (bop.left instanceof CONST) {
					c = (CONST) bop.left;
					e1 = bop.right;
				} else {
					c = (CONST) bop.right;
					e1 = bop.left;
				}
				Temp t0 = new Temp(), t1 = new Temp(), t2 = new Temp();
				emit(new OPER("addi `d0,`s0," + c.value + "\n", L(t0, null), L(
						munchExp(e1), null)));
				emit(new OPER("add `d0,`s0, $zero\n", L(t1, null), L(
						munchExp(s.src), null)));
				emit(new OPER("sw `s1,0( `s0)\n", null, L(t0, L(t1, null))));
				return;
			}

			Temp t0 = new Temp(), t1 = new Temp();
			emit(new OPER("add `d0,`s0,$zero\n", L(t0, null), L(
					munchExp(mdst.exp), null)));
			emit(new OPER("add `d0,`s0,$zero\n", L(t1, null), L(
					munchExp(s.src), null)));
			emit(new OPER("sw `s1,0(`s0)\n", null, L(t0, L(t1, null))));
		}

		if (mdst.exp instanceof CONST) {// MOVE(MEM(CONST(i)),e)
			CONST c = (CONST) mdst.exp;
			emit(new OPER("sw `s0," + c.value + "($zero)\n", null, L(
					munchExp(s.src), null)));
			return;
		}

		Temp t0 = new Temp(), t1 = new Temp();// MOVE(MEM(e1),e2)
		emit(new OPER("add `d0,`s0,$zero\n", L(t0, null), L(munchExp(mdst.exp),
				null)));
		emit(new OPER("add `d0,`s0,$zero\n", L(t1, null), L(munchExp(s.src),
				null)));
		emit(new OPER("sw `s1,0(`s0)\n", null, L(t0, L(t1, null))));
	}

	void munchStm(tiger.Tree.LABEL lab) { // LABEL(lab)
		emit(new tiger.Assem.LABEL(lab.label.toString() + ":\n", lab.label));
	}

	void munchStm(tiger.Tree.CJUMP s) {// undoing

		Temp t0 = new Temp(), t1 = new Temp();
		emit(new OPER("add `d0,`s0,$zero\n", L(t0, null), L(munchExp(s.left),
				null)));
		emit(new OPER("add `d0,`s0,$zero\n", L(t1, null), L(munchExp(s.right),
				null)));

		Temp rs = t0, rt = t1;
		switch (s.relop) {
		case tiger.Tree.CJUMP.EQ:
			emit(new OPER("beq `s0,`s1,`j0\n", null, L(rs, L(rt, null)), LL(
					s.iftrue, null)));
			break;
		case tiger.Tree.CJUMP.NE:
			emit(new OPER("bne `s0,`s1,`j0\n", null, L(rs, L(rt, null)), LL(
					s.iftrue, null)));
			break;
		case tiger.Tree.CJUMP.LT:
			emit(new OPER("blt `s0,`s1,`j0\n", null, L(rs, L(rt, null)), LL(
					s.iftrue, null)));
			break;
		case tiger.Tree.CJUMP.LE:
			emit(new OPER("ble `s0,`s1,`j0\n", null, L(rs, L(rt, null)), LL(
					s.iftrue, null)));
			break;
		case tiger.Tree.CJUMP.GT:
			emit(new OPER("bgt `s0,`s1,`j0\n", null, L(rs, L(rt, null)), LL(
					s.iftrue, null)));
			break;
		case tiger.Tree.CJUMP.GE:
			emit(new OPER("bge `s0,`s1,`j0\n", null, L(rs, L(rt, null)), LL(
					s.iftrue, null)));
			break;
		}
	}

	void munchStm(tiger.Tree.JUMP s) {// undoing
		if (s.exp instanceof NAME)
			emit(new OPER("j `j0\n", null, null, s.targets));
		else if (s.exp instanceof TEMP)
			emit(new OPER("jr `d0\n", L(frame.RA(), null), null));
	}

	void munchStm(tiger.Tree.EXP s) {
		munchExp(s.exp);
		// undoing
	}

	void munchStm(Stm s) {
		if (s instanceof tiger.Tree.MOVE)
			munchStm((tiger.Tree.MOVE) s);
		if (s instanceof tiger.Tree.EXP)
			munchStm((tiger.Tree.EXP) s);
		if (s instanceof tiger.Tree.JUMP)
			munchStm((tiger.Tree.JUMP) s);
		if (s instanceof tiger.Tree.CJUMP)
			munchStm((tiger.Tree.CJUMP) s);
		if (s instanceof tiger.Tree.SEQ)
			munchStm((tiger.Tree.SEQ) s);
		if (s instanceof tiger.Tree.LABEL)
			munchStm((tiger.Tree.LABEL) s);
	}

	public InstrList codegen(Stm s) {
		InstrList l;
		munchStm(s);
		l = ilist;
		ilist = last = null;
		return l;
	}
}
