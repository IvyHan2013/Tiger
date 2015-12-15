package tiger.Mips;

import tiger.Assem.*;
import tiger.Frame.*;
import tiger.Temp.*;
import tiger.Translate.ProcFrag;
import tiger.Tree.*;
import tiger.Tree.LABEL;
import tiger.Tree.MOVE;
import tiger.Util.*;

public class Frame extends tiger.Frame.Frame {

	public int offset = 0;

	public int arguoffset = 0;

	public final static Temp zero = new Temp();

	public final static Temp at = new Temp();

	public final static Temp v0 = new Temp();

	public final static Temp v1 = new Temp();

	public final static Temp a0 = new Temp();

	public final static Temp a1 = new Temp();

	public final static Temp a2 = new Temp();

	public final static Temp a3 = new Temp();

	public final static Temp t0 = new Temp();

	public final static Temp t1 = new Temp();

	public final static Temp t2 = new Temp();

	public final static Temp t3 = new Temp();

	public final static Temp t4 = new Temp();

	public final static Temp t5 = new Temp();

	public final static Temp t6 = new Temp();

	public final static Temp t7 = new Temp();

	public final static Temp s0 = new Temp();

	public final static Temp s1 = new Temp();

	public final static Temp s2 = new Temp();

	public final static Temp s3 = new Temp();

	public final static Temp s4 = new Temp();

	public final static Temp s5 = new Temp();

	public final static Temp s6 = new Temp();

	public final static Temp s7 = new Temp();

	public final static Temp t8 = new Temp();

	public final static Temp t9 = new Temp();

	public final static Temp k0 = new Temp();

	public final static Temp k1 = new Temp();

	public final static Temp gp = new Temp();

	public final static Temp sp = new Temp();

	public final static Temp fp = new Temp();

	public final static Temp ra = new Temp();

	public static TempList regist = L(zero, L(at, L(v0, L(v1, L(a0, L(a1,
			L(a2, L(a3, L(t0, L(t1, L(t2, L(t3, L(t4, L(t5,
					L(t6, L(t7, L(s0, L(s1, L(s2, L(s3, L(s4, L(s5, L(s6, L(s7,
							L(t8, L(t9, L(k0, L(k1, L(gp, L(sp, L(fp, L(ra,
									null))))))))))))))))))))))))))))))));

	public static TempList calleeSaves = L(s0, L(s1, L(s2, L(s3, L(s4, L(s5, L(
			s6, L(s7, L(fp, L(ra, null))))))))));

	public static TempList callerSaves = L(t0, L(t1, L(t2, L(t3, L(t4, L(t5, L(
			t6, L(t7, L(t8, L(t9, null))))))))));

	public static TempList argres = L(a0, L(a1, L(a2, L(a3, null))));

	public static TempList specialregs = L(a0, L(a1, L(a2, L(a3, L(v0, L(v1, L(
			zero, L(at, L(k0, L(k1, L(gp, L(sp, L(fp, L(ra, null))))))))))))));

	// public static TempList freeregs = L(s0, L(s1, L(s2, L(s3, L(s4, L(s5,
	// L(s6,
	// L(s7, L(t0, L(t1, L(t2, L(t3, L(t4, L(t5, L(t6, L(t7, L(t8, L(t9,
	// L(v0, L(v1, null))))))))))))))))))));
	public static TempList freeregs = L(s0, L(s1, L(s2, L(s3,
			L(s4, L(s5, L(s6, L(s7, L(v0, L(v1, L(a0, L(a1, L(a2, L(a3, L(t0,
					L(t1, L(t2, L(t3, L(t4, L(t5, L(t6, L(t7,
							L(t8, L(t9, null))))))))))))))))))))))));

	java.util.Dictionary<Temp, String> tempMap = new java.util.Hashtable<Temp, String>();

	java.util.Dictionary<Temp, Temp> sameTemp = new java.util.Hashtable<Temp, Temp>();

	public Frame() {
		super();
		tempMap.put(zero, "$zero");
		tempMap.put(at, "$at");
		tempMap.put(v0, "$v0");
		tempMap.put(v1, "$v1");
		tempMap.put(a0, "$a0");
		tempMap.put(a1, "$a1");
		tempMap.put(a2, "$a2");
		tempMap.put(a3, "$a3");
		tempMap.put(t0, "$t0");
		tempMap.put(t1, "$t1");
		tempMap.put(t2, "$t2");
		tempMap.put(t3, "$t3");
		tempMap.put(t4, "$t4");
		tempMap.put(t5, "$t5");
		tempMap.put(t6, "$t6");
		tempMap.put(t7, "$t7");
		tempMap.put(s0, "$s0");
		tempMap.put(s1, "$s1");
		tempMap.put(s2, "$s2");
		tempMap.put(s3, "$s3");
		tempMap.put(s4, "$s4");
		tempMap.put(s5, "$s5");
		tempMap.put(s6, "$s6");
		tempMap.put(s7, "$s7");
		tempMap.put(t8, "$t8");
		tempMap.put(t9, "$t9");
		tempMap.put(k0, "$k0");
		tempMap.put(k1, "$k1");
		tempMap.put(gp, "$gp");
		tempMap.put(sp, "$sp");
		tempMap.put(fp, "$fp");
		tempMap.put(ra, "$ra");
	}

	public Frame(Label nm, BoolList fms) {
		this();
		name = nm;
		formals = null;
	}

	public Frame newFrame(Label nm, BoolList formals) {
		return new Frame(nm, formals);
	}

	public Access allocLocal(boolean escape) {
		if (escape) {
			return new InFrame(wordSize() * (--offset));
		} else
			return new InReg();
	}

	public Access allocArguLocal(boolean escape) {
		arguoffset++;
		if (arguoffset <= 4)
			return new InFrame(wordSize() * (--offset));
		else
			return new InFrame(wordSize() * (arguoffset - 4));
	}

	public int Offset() {
		return offset;
	}

	public Temp FP() {
		return fp;
	}

	public Temp SP() {
		return sp;
	}

	public Temp RV() {
		return v0;
	}

	public Temp RA() {
		return ra;
	}

	public Temp ARGU(int n) {
		if (n > 3)
			return null;
		TempList it;
		for (it = argres; n > 0; it = it.tail, n--)
			;

		// debuging
		// System.out.println(n + " Cmoe here~~~~~~~~~~~~~~~~~~~~~~~~~~ "
		// + it.head + " " + isSpecial(it.head) + " "
		// + tempMap.get(it.head));

		return it.head;
	}

	public TempList freeRegs() {
		return freeregs;
	}

	public TempList registers() {
		return regist;
	}

	public boolean isSpecial(Temp t, TempList tl) {
		// TempList it = specialregs;

		// debuging
		TempList it = tl;

		for (; it != null; it = it.tail)
			if (it.head == t)
				return true;
		return false;
	}

	public String tempMap(Temp temp) {
		if (isSpecial(temp, regist))
			return tempMap.get(temp);
		return null;

	}

	public int wordSize() {
		return 4;
	}

	public AbsExp externalCall(String func, ExpList args) {
		return new CALL(new NAME(new Label(func)), new ExpList(new CONST(0),
				args));// virtual static link
	}

	public String string(Label lab, String lit) {
		return ".data\n" + lab + ":\n.word " + lit.length() + "\n.asciiz \""
				+ lit + "\"\n" + ".align 2\n.text\n";
	}

	public Stm procEntryExit1(Stm body) {
		int i;
		TempList it;

		// page 176-4
		Stm saveArgu = null;
		if (arguoffset != 0) {
			saveArgu = new MOVE(new MEM(new BINOP(BINOP.PLUS, new TEMP(FP()),
					new CONST(-1 * wordSize()))), new TEMP(argres.head));
			for (i = 2, it = argres.tail; i <= arguoffset && it != null; i++, it = it.tail)
				saveArgu = new SEQ(
						saveArgu,
						new MOVE(new MEM(new BINOP(BINOP.PLUS, new TEMP(FP()),
								new CONST(-i * wordSize()))), new TEMP(it.head)));
		}

		return saveArgu == null ? body : new SEQ(saveArgu, body);
	}

	public InstrList procEntryExit2(InstrList body) {
		// return append(body, new InstrList(new OPER("",
		// null, calleeSaves), null));

		TempList it;
		int i;

		// page 176-5
		InstrList prelogue = new InstrList(new OPER(
				"\n#Begin to save calleeSaves!!\n", null, null), null);
		prelogue = append(prelogue, new InstrList(new OPER("addi `d0,`s0,"
				+ offset * wordSize() + "\n", L(SP(), null), L(SP(), null)),
				null));
		for (it = calleeSaves, i = 1; it != null; it = it.tail, i++)
			prelogue = append(prelogue, new InstrList(new OPER("sw `s1," + -i
					* wordSize() + "(`s0)\n", null, L(SP(), L(it.head, null))),
					null));
		i--;
		prelogue = append(prelogue, new InstrList(new OPER("addi `d0,`s0," + -i
				* wordSize() + "\n", L(SP(), null), L(SP(), null)), null));
		prelogue = append(prelogue, new InstrList(new OPER(
				"#calleeSaves have been saved!\n\n", null, null), null));

		InstrList inlogue = append(new InstrList(new OPER("add `d0,`s0,"
				+ -(-i + offset) * wordSize() + "\n", L(FP(), null), L(SP(),
				null)), null), body);
		// InstrList inlogue = append(append(new InstrList(new OPER("add
		// `d0,`s0,"
		// + -(-i + offset) * wordSize() + "\n", L(FP(), null), L(SP(),
		// null)), null), body), new InstrList(new OPER("", null,
		// callerSaves), null));

		// page 176-8
		InstrList epilogue = new InstrList(new OPER(
				"\n#Begin to fetch calleeSaves!\n", null, null), new InstrList(
				new OPER("addi `d0,`s0," + i * wordSize() + "\n",
						L(SP(), null), L(SP(), null)), null));
		for (it = calleeSaves, i = 1; it != null; it = it.tail, i++)
			epilogue = append(epilogue, new InstrList(new OPER("lw `d0," + -i
					* wordSize() + "(`s0)\n", L(it.head, null), L(SP(), null)),
					null));
		epilogue = append(epilogue, new InstrList(new OPER("addi `d0,`s0,"
				+ -offset * wordSize() + "\n", L(SP(), null), L(SP(), null)),
				null));
		epilogue = append(epilogue, new InstrList(new OPER(
				"#calleeSaves have been fetched!\n\n", null, null), null));

		// debuging
		// return body;

		return append(append(prelogue, append(inlogue, epilogue)),
				new InstrList(new OPER("jr `s0\n", null, L(RA(), null)), null));
	}

	public Proc procEntryExit3(InstrList body) {
		return new tiger.Frame.Proc("\n\n\n.text\n#FUN_BEGIN "
				+ name.toString() + "\n" + name.toString() + ":\n", body,
				"#FUN_END " + name.toString() + "\n\n\n");
	}

	public InstrList codegen(Stm stm) {
		return (new Codegen(this)).codegen(stm);
	}
}
