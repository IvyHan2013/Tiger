package tiger.FindEscape;

import tiger.Symbol.*;
import tiger.Absyn.*;

public class FindEscape {
	Table escEnv = new Table();

	// *********************************traverseVar

	void traverseVar(int depth, SimpleVar sv) {
		Escape ve = (Escape) escEnv.get(sv.name);
		if (depth > ve.depth)
			ve.setEscape();
	}

	void traverseVar(int depth, FieldVar fv) {
		traverseVar(depth, fv.var);
	}

	void traverseVar(int depth, SubscriptVar sv) {
		traverseVar(depth, sv.var);
	}

	void traverseVar(int depth, Var v) {
		if (v instanceof SimpleVar)
			traverseVar(depth, (SimpleVar) v);
		if (v instanceof FieldVar)
			traverseVar(depth, (FieldVar) v);
		if (v instanceof SubscriptVar)
			traverseVar(depth, (SubscriptVar) v);
	}

	// *********************************traverseExp

	void traverseExp(int depth, VarExp ve) {
		traverseVar(depth, ve.var);
	}

	void traverseExp(int depth, NilExp ne) {// do nothing
	}

	void traverseExp(int depth, IntExp ie) {// do nothing
	}

	void traverseExp(int depth, StringExp se) {// do nothing
	}

	void traverseExp(int depth, CallExp ce) {
		ExpList el = ce.args;
		for (; el != null; el = el.tail)
			traverseExp(depth, el.head);
	}

	void traverseExp(int depth, OpExp oe) {
		traverseExp(depth, oe.left);
		traverseExp(depth, oe.right);
	}

	void traverseExp(int depth, RecordExp re) {
		FieldExpList fel = re.fields;
		for (; fel != null; fel = fel.tail)
			traverseExp(depth, fel.init);
	}

	void traverseExp(int depth, SeqExp se) {
		ExpList el = se.list;
		for (; el != null; el = el.tail)
			traverseExp(depth, el.head);
	}

	void traverseExp(int depth, AssignExp ae) {
		traverseVar(depth, ae.var);
		traverseExp(depth, ae.exp);
	}

	void traverseExp(int depth, IfExp ie) {
		traverseExp(depth, ie.test);
		traverseExp(depth, ie.thenclause);
		if (ie.elseclause != null)
			traverseExp(depth, ie.elseclause);
	}

	void traverseExp(int depth, WhileExp we) {
		traverseExp(depth, we.test);
		traverseExp(depth, we.body);
	}

	void traverseExp(int depth, ForExp fe) {
		escEnv.beginScope();
		traverseDec(depth, fe.var);
		traverseExp(depth, fe.hi);
		traverseExp(depth, fe.body);
		escEnv.endScope();
	}

	void traverseExp(int depth, BreakExp be) {// do nothing
	}

	void traverseExp(int depth, LetExp ve) {
		escEnv.beginScope();
		DecList dl = ve.decs;
		for (; dl != null; dl = dl.tail)
			traverseDec(depth, dl.head);
		traverseExp(depth, ve.body);
		escEnv.endScope();
	}

	void traverseExp(int depth, ArrayExp ve) {
		traverseExp(depth,ve.size);
		traverseExp(depth,ve.init);
	}

	void traverseExp(int depth, Exp e) {
		if (e instanceof VarExp)
			traverseExp(depth, (VarExp) e);
		if (e instanceof NilExp)
			traverseExp(depth, (NilExp) e);
		if (e instanceof IntExp)
			traverseExp(depth, (IntExp) e);
		if (e instanceof StringExp)
			traverseExp(depth, (StringExp) e);
		if (e instanceof CallExp)
			traverseExp(depth, (CallExp) e);
		if (e instanceof OpExp)
			traverseExp(depth, (OpExp) e);
		if (e instanceof RecordExp)
			traverseExp(depth, (RecordExp) e);
		if (e instanceof SeqExp)
			traverseExp(depth, (SeqExp) e);
		if (e instanceof AssignExp)
			traverseExp(depth, (AssignExp) e);
		if (e instanceof IfExp)
			traverseExp(depth, (IfExp) e);
		if (e instanceof WhileExp)
			traverseExp(depth, (WhileExp) e);
		if (e instanceof ForExp)
			traverseExp(depth, (ForExp) e);
		if (e instanceof BreakExp)
			traverseExp(depth, (BreakExp) e);
		if (e instanceof LetExp)
			traverseExp(depth, (LetExp) e);
		if (e instanceof ArrayExp)
			traverseExp(depth, (ArrayExp) e);
	}

	// *********************************traverseDec

	void traverseDec(int depth, VarDec vd) {
		escEnv.put(vd.name, new VarEscape(depth, vd));
		traverseExp(depth, vd.init);
	}

	void traverseDec(int depth, FunctionDec fd) {
		escEnv.beginScope();
		FieldList fl = fd.params;
		for (; fl != null; fl = fl.tail)
			escEnv.put(fl.name, new FormalEscape(depth + 1, fl));
		traverseExp(depth + 1, fd.body);
		escEnv.endScope();
		if (fd.next != null)
			traverseDec(depth, fd);
	}

	void traverseDec(int depth, Dec d) {
		if (d instanceof VarDec)
			traverseDec(depth, (VarDec) d);
		if (d instanceof FunctionDec)
			traverseDec(depth, (FunctionDec) d);
		// else do nothing
	}

	public FindEscape(Exp e) {
		traverseExp(0, e);
	}
}
