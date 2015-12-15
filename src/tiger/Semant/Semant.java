package tiger.Semant;

import tiger.Translate.*;
import tiger.errormsg.*;
import tiger.Types.*;
import tiger.Temp.*;
import tiger.Util.*;

public class Semant {
	Env env;

	public static ForState loopflag;

	public static Level globalLevZero;

	public static Translate translate = new Translate();

	public static INT newint = new INT();

	public static STRING newstring = new STRING();

	public static NIL newnil = new NIL();

	public static VOID newvoid = new VOID();

	public static RECORD newrecord = new RECORD(tiger.Symbol.Symbol
			.symbol("123noID"), newvoid, null);

	tiger.Tree.Print irPrint = new tiger.Tree.Print(System.out);

	public Semant(ErrorMsg err) {
		this(new Env(err));
	}

	public Semant(Translate tr, ErrorMsg err) {
		this(err);
		translate = tr;
	}

	Semant(Env e) {
		loopflag = new ForState();
		env = e;
	}

	private void error(int pos, String msg) {
		env.errorMsg.error(pos, msg);
	}

	private boolean checkTyEq(Type e1, Type e2) {
		// System.out.println("\n!!!!!!!!!!!!!!!!"+e1+"\n!!!!!!!!!!!!!!!!"+e2);
		boolean flag = e1.coerceTo(e2);
		return (flag || (e1 instanceof NIL) || (e2 instanceof NIL))
				&& (!((e1 instanceof NIL) && (e2 instanceof NIL)));
	}

	private boolean checkArray(ExpTy et, int pos) {
		if (!(et.ty instanceof ARRAY)) {
			error(pos, "array required");
			return false;
		}
		return true;
	}

	private boolean checkInt(ExpTy et, int pos) {
		if (!(et.ty instanceof INT)) {
			error(pos, "integer required");
			return false;
		}
		return true;
	}

	private boolean checkName(ExpTy et, int pos) {
		if (!(et.ty instanceof NAME)) {
			error(pos, "name required");
			return false;
		}
		return true;
	}

	private boolean checkNil(ExpTy et, int pos) {
		if (!(et.ty instanceof NIL)) {
			error(pos, "nil required");
			return false;
		}
		return true;
	}

	private boolean checkRecord(ExpTy et, int pos) {
		if (!(et.ty instanceof RECORD)) {
			error(pos, "record required");
			return false;
		}
		return true;
	}

	private boolean checkString(ExpTy et, int pos) {
		if (!(et.ty instanceof STRING)) {
			error(pos, "string required");
			return false;
		}
		return true;
	}

	private boolean checkVoid(ExpTy et, int pos) {
		if (!(et.ty instanceof VOID)) {
			error(pos, "void required");
			return false;
		}
		return true;
	}

	/*
	 * Beging transVar Translate ExpTy transVar
	 */

	ExpTy transVar(Level lev, int pos, tiger.Symbol.Symbol n) {
		Entry x = (Entry) env.venv.get(n);
		if (x instanceof VarEntry) {
			VarEntry ent = (VarEntry) x;
			return new ExpTy(translate.simpleVar(ent.access, lev), ent.ty);
		} else {
			error(pos, "\"" + n + "\" undefined variable");
			return new ExpTy(translate.emptyExp(), newvoid);
		}
	}

	ExpTy transVar(Level lev, tiger.Absyn.SimpleVar v) {
		return transVar(lev, v.pos, v.name);
	}

	RECORD getField(RECORD now, tiger.Symbol.Symbol name) {
		if (now == null)
			return null;
		if (now.fieldName.equals(name))
			return now;
		return getField(now.tail, name);
	}

	ExpTy transVar(Level lev, tiger.Absyn.FieldVar v) {
		if (v.var == null)
			return transVar(lev, v.pos, v.field);
		ExpTy par = transVar(lev, v.var);

		// System.out.println("!!!!!!!!!!!!!! "+((NAME)par.ty).name);

		if (!checkRecord(par, v.pos))
			return new ExpTy(null, newvoid);
		RECORD pre = (RECORD) par.ty;
		RECORD now = getField(pre, v.field);
		if (now == null) {
			error(v.pos, "\"" + v.field + "\" undefined field variable");
			return new ExpTy(translate.emptyExp(), newvoid);
		}
		int size = 0;
		for (now = pre; !now.fieldName.equals(v.field); now = now.tail)
			size++;
		return new ExpTy(translate.fieldVar(par.exp, size, lev), now.fieldType);
	}

	ExpTy transVar(Level lev, tiger.Absyn.SubscriptVar v) {
		ExpTy ty = transVar(lev, v.var);
		ExpTy index = transExp(lev, v.index);
		checkInt(index, v.pos);
		if (checkArray(ty, v.pos))
			return new ExpTy(translate.subscriptVar(ty.exp, index.exp, lev),
					((ARRAY) ty.ty).element);
		else
			return new ExpTy(translate.emptyExp(), newvoid);
	}

	ExpTy transVar(Level lev, tiger.Absyn.Var v) {
		if (v instanceof tiger.Absyn.SimpleVar)
			return transVar(lev, (tiger.Absyn.SimpleVar) v);
		if (v instanceof tiger.Absyn.FieldVar)
			return transVar(lev, (tiger.Absyn.FieldVar) v);
		if (v instanceof tiger.Absyn.SubscriptVar)
			return transVar(lev, (tiger.Absyn.SubscriptVar) v);
		throw new Error("transVar");
	}

	/* End transVar */

	/*
	 * Begin transExp Translate ExpTy transExp
	 */

	ExpTy transExp(Level lev, tiger.Absyn.VarExp e) {
		return transVar(lev, e.var);
	}

	ExpTy transExp(Level lev, tiger.Absyn.NilExp e) {
		return new ExpTy(translate.nilExp(lev), newnil);
	}

	ExpTy transExp(Level lev, tiger.Absyn.IntExp e) {
		return new ExpTy(translate.intExp(e.value, lev), newint);
	}

	ExpTy transExp(Level lev, tiger.Absyn.StringExp e) {
		return new ExpTy(translate.stringExp(e.value, lev), newstring);
	}

	ExpTy transExp(Level lev, tiger.Absyn.CallExp e) {

		Entry x = (Entry) env.venv.get(e.func);
		if (!(x instanceof FunEntry)) {
			error(e.pos, "\"" + e.func + "\" undefined function");
			return new ExpTy(translate.emptyExp(), newvoid);
		}
		FunEntry fx = (FunEntry) x;
		tiger.Absyn.ExpList it;
		RECORD IT;
		ExpList el = null, tmpel = null;
		if (fx.formals != newrecord)
			for (it = e.args, IT = fx.formals; it != null || IT != null; it = it.tail, IT = IT.tail) {
				if (it == null || IT == null) {
					error(e.pos, "\"" + e.func + "\" argument number mismatch");
					return new ExpTy(translate.emptyExp(), newvoid);
				}
				ExpTy arg = transExp(lev, it.head);
				if (tmpel == null)
					el = tmpel = new ExpList(arg.exp, null);
				else {
					tmpel.tail = new ExpList(arg.exp, null);
					tmpel = tmpel.tail;
				}
				if (!(checkTyEq(arg.ty, IT.fieldType)))
					error(e.pos, "\"" + e.func + "\" argument mismatch");
			}
		return new ExpTy(translate.callExp(el, lev, fx.level, fx.sysFun),
				fx.result);
	}

	ExpTy transExp(Level lev, tiger.Absyn.OpExp e) {
		ExpTy eleft = transExp(lev, e.left);
		ExpTy eright = transExp(lev, e.right);
		switch (e.oper) {
		case tiger.Absyn.OpExp.PLUS:
			checkInt(eleft, e.left.pos);
			checkInt(eright, e.right.pos);
			return new ExpTy(translate.opExp(tiger.Tree.BINOP.PLUS, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.MINUS:
			checkInt(eleft, e.left.pos);
			checkInt(eright, e.right.pos);
			return new ExpTy(translate.opExp(tiger.Tree.BINOP.MINUS, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.MUL:
			checkInt(eleft, e.left.pos);
			checkInt(eright, e.right.pos);
			return new ExpTy(translate.opExp(tiger.Tree.BINOP.MUL, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.DIV:
			checkInt(eleft, e.left.pos);
			checkInt(eright, e.right.pos);
			return new ExpTy(translate.opExp(tiger.Tree.BINOP.DIV, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.EQ:
			if (!checkTyEq(eleft.ty, eright.ty))
				error(e.pos, "compare need same type");
			return new ExpTy(translate.relExp(tiger.Tree.CJUMP.EQ, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.NE:
			if (!checkTyEq(eleft.ty, eright.ty))
				error(e.pos, "compare need same type");
			return new ExpTy(translate.relExp(tiger.Tree.CJUMP.NE, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.LT:
			if (!checkTyEq(eleft.ty, eright.ty))
				error(e.pos, "compare need same type");
			if (!((eleft.ty instanceof INT) || (eleft.ty instanceof STRING)))
				error(e.pos, "order compare need integer or string tryp");
			return new ExpTy(translate.relExp(tiger.Tree.CJUMP.LT, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.LE:
			if (!checkTyEq(eleft.ty, eright.ty))
				error(e.pos, "compare need same type");
			if (!((eleft.ty instanceof INT) || (eleft.ty instanceof STRING)))
				error(e.pos, "order compare need integer or string tryp");
			return new ExpTy(translate.relExp(tiger.Tree.CJUMP.LE, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.GT:
			if (!checkTyEq(eleft.ty, eright.ty))
				error(e.pos, "compare need same type");
			if (!((eleft.ty instanceof INT) || (eleft.ty instanceof STRING)))
				error(e.pos, "order compare need integer or string tryp");
			return new ExpTy(translate.relExp(tiger.Tree.CJUMP.GT, eleft.exp,
					eright.exp, lev), newint);
		case tiger.Absyn.OpExp.GE:
			if (!checkTyEq(eleft.ty, eright.ty))
				error(e.pos, "compare need same type");
			if (!((eleft.ty instanceof INT) || (eleft.ty instanceof STRING)))
				error(e.pos, "order compare need integer or string tryp");
			return new ExpTy(translate.relExp(tiger.Tree.CJUMP.GE, eleft.exp,
					eright.exp, lev), newint);
		}
		throw new Error("transExp(Level lev,OpExp)");
	}

	ExpTy transExp(Level lev, tiger.Absyn.RecordExp e) {
		Type x = (Type) env.tenv.get(e.typ);
		if (x == null) {
			error(e.pos, "\"" + e.typ + "\" type undefined");
			return new ExpTy(translate.emptyExp(), newvoid);
		}
		// System.out.println("Wrong: "+((NAME)x).name);
		x = x.actual();
		if (!(x instanceof RECORD)) {
			error(e.pos, "\"" + "\" record undefined");
			return new ExpTy(translate.emptyExp(), newvoid);
		}
		RECORD rx = (RECORD) x;
		int size = 0;
		ExpList el = null, tmpel = null;
		if (rx != newrecord) {
			tiger.Absyn.FieldExpList it;
			for (it = e.fields; it != null || rx != null; it = it.tail, rx = rx.tail) {
				if (it == null || rx == null) {
					error(e.pos, "\"" + e.fields + "\" field number mismatch");
					return new ExpTy(translate.emptyExp(), newvoid);
				}
				if (!(rx.fieldName.equals(it.name))) {
					error(e.pos, "\"" + e.fields + "\" element \"" + it.name
							+ "\" undefined");
					return new ExpTy(translate.emptyExp(), newvoid);
				}
				size++;
				ExpTy init = transExp(lev, it.init);
				if (tmpel == null)
					el = tmpel = new ExpList(init.exp, null);
				else {
					tmpel.tail = new ExpList(init.exp, null);
					tmpel = tmpel.tail;
				}
				if (!(checkTyEq(init.ty, rx.fieldType))) {
					error(e.pos, "\"" + it.name + "\" type mismatch");
					return new ExpTy(translate.emptyExp(), newvoid);
				}
			}
		}
		// debuging;
		//System.out.println(rx+" "+ size);
		
		return new ExpTy(translate.recordExp(el, size, lev), x);
	}

	ExpTy transExp(Level lev, tiger.Absyn.SeqExp e) {
		tiger.Absyn.ExpList it;
		ExpTy now = new ExpTy(translate.emptyExp(), newvoid);
		ExpList el = null, tmpel = null;
		for (it = e.list; it != null; it = it.tail) {
			now = transExp(lev, it.head);
			if (tmpel == null)
				el = tmpel = new ExpList(now.exp, null);
			else {
				tmpel.tail = new ExpList(now.exp, null);
				tmpel = tmpel.tail;
			}
		}
		return new ExpTy(translate.seqExp(el, lev), now.ty);
	}

	ExpTy transExp(Level lev, tiger.Absyn.AssignExp e) {
		ExpTy lvalue = transVar(lev, e.var);
		if (lvalue.ty instanceof VOID) {
			error(e.pos, "assign need a var lvalue");
			return new ExpTy(translate.emptyExp(), newvoid);
		}
		if (e.var instanceof tiger.Absyn.SimpleVar)
			if (((VarEntry) env.venv.get(((tiger.Absyn.SimpleVar) e.var).name)).isForIt) {
				error(e.var.pos, "FOR iterator can not be changed");
				return new ExpTy(translate.emptyExp(), newvoid);
			}
		ExpTy exp = transExp(lev, e.exp);
		if (!(checkTyEq(lvalue.ty, exp.ty)))
			error(e.pos, "assign need the same type");
		return new ExpTy(translate.assignExp(lvalue.exp, exp.exp, lev), newvoid);
	}

	ExpTy transExp(Level lev, tiger.Absyn.IfExp e) {
		ExpTy test = transExp(lev, e.test);
		checkInt(test, e.test.pos);
		if (e.elseclause == null) {
			ExpTy ethen = transExp(lev, e.thenclause);
			if (!(checkTyEq(ethen.ty, newvoid))) {
				error(e.pos, "if_then must return VOID");
			}
			return new ExpTy(translate.ifExp(test.exp, ethen.exp, translate
					.emptyExp()), newvoid);
		} else {
			ExpTy ethen = transExp(lev, e.thenclause);
			ExpTy eelse = transExp(lev, e.elseclause);
			if (checkTyEq(ethen.ty, eelse.ty))
				return new ExpTy(translate
						.ifExp(test.exp, ethen.exp, eelse.exp), ethen.ty);
			else {
				error(e.pos, "if_then_else must return the same type");
				return new ExpTy(translate.emptyExp(), newvoid);
			}
		}
	}

	ExpTy transExp(Level lev, tiger.Absyn.WhileExp e) {
		ExpTy test = transExp(lev, e.test);
		checkInt(test, e.test.pos);
		Label done = new Label();
		loopflag.inFor(done);
		ExpTy ebody = transExp(lev, e.body);
		loopflag.outFor();
		if (!(checkTyEq(ebody.ty, newvoid)))
			error(e.pos, "while must return VOID");
		return new ExpTy(translate.whileExp(test.exp, ebody.exp, done, lev),
				newvoid);
	}

	ExpTy transExp(Level lev, tiger.Absyn.ForExp e) {
		env.venv.beginScope();
		VarDecExp deci = (VarDecExp) transDec(lev, e.var);
		VarEntry ei = (VarEntry) env.venv.get(e.var.name);
		ei.isForIt = true;
		if (!(ei.ty instanceof INT))
			error(e.pos, "iterator must be integer in the ForExp");
		ExpTy hi = transExp(lev, e.hi);
		checkInt(hi, e.pos);
		Label done = new Label();
		loopflag.inFor(done);
		ExpTy body = transExp(lev, e.body);
		loopflag.outFor();
		env.venv.endScope();
		return new ExpTy(translate.forExp(deci.access, deci.ini, hi.exp,
				body.exp, done, lev), newvoid);
	}

	ExpTy transExp(Level lev, tiger.Absyn.BreakExp e) {
		if (!loopflag.isFor())
			error(e.pos, "BREAK must in any loop");
		return new ExpTy(translate.breakExp(loopflag.getLabel(), lev), newvoid);
	}

	ExpTy transExp(Level lev, tiger.Absyn.LetExp e) {// undoing
		env.venv.beginScope();
		env.tenv.beginScope();
		ExpList el = null, tmpel = null;
		for (tiger.Absyn.DecList p = e.decs; p != null; p = p.tail) {
			Exp deci = transDec(lev, p.head);
			if (tmpel == null)
				el = tmpel = new ExpList(deci, null);
			else {
				tmpel.tail = new ExpList(deci, null);
				tmpel = tmpel.tail;
			}
		}
		ExpTy et = transExp(lev, e.body);
		env.venv.endScope();
		env.tenv.endScope();
		return new ExpTy(translate.letExp(translate.decList(el, lev), et.exp),
				et.ty);
	}

	ExpTy transExp(Level lev, tiger.Absyn.ArrayExp e) {
		ExpTy size = transExp(lev, e.size);
		checkInt(size, e.pos);
		ExpTy init = transExp(lev, e.init);
		Type etyp = (Type) env.tenv.get(e.typ);
		if (etyp == null) {
			error(e.pos, "\"" + e.typ + "\" type undefined");
			return new ExpTy(translate.emptyExp(), newvoid);
		}
		etyp = etyp.actual();
		if (!(etyp instanceof ARRAY)) {
			error(e.pos, "array required");
			return new ExpTy(translate.emptyExp(), newvoid);
		}
		ARRAY atyp = (ARRAY) etyp;
		if (!(checkTyEq(atyp.element, init.ty)))
			error(e.pos, "\"" + e.typ + "\" array type mismatch");
		return new ExpTy(translate.arrayExp(size.exp, init.exp, lev), etyp);
	}

	ExpTy transExp(Level lev, tiger.Absyn.Exp e) {
		if (e instanceof tiger.Absyn.VarExp)
			return transExp(lev, (tiger.Absyn.VarExp) e);
		if (e instanceof tiger.Absyn.NilExp)
			return transExp(lev, (tiger.Absyn.NilExp) e);
		if (e instanceof tiger.Absyn.IntExp)
			return transExp(lev, (tiger.Absyn.IntExp) e);
		if (e instanceof tiger.Absyn.StringExp)
			return transExp(lev, (tiger.Absyn.StringExp) e);
		if (e instanceof tiger.Absyn.CallExp)
			return transExp(lev, (tiger.Absyn.CallExp) e);
		if (e instanceof tiger.Absyn.OpExp)
			return transExp(lev, (tiger.Absyn.OpExp) e);
		if (e instanceof tiger.Absyn.RecordExp)
			return transExp(lev, (tiger.Absyn.RecordExp) e);
		if (e instanceof tiger.Absyn.SeqExp)
			return transExp(lev, (tiger.Absyn.SeqExp) e);
		if (e instanceof tiger.Absyn.AssignExp)
			return transExp(lev, (tiger.Absyn.AssignExp) e);
		if (e instanceof tiger.Absyn.IfExp)
			return transExp(lev, (tiger.Absyn.IfExp) e);
		if (e instanceof tiger.Absyn.WhileExp)
			return transExp(lev, (tiger.Absyn.WhileExp) e);
		if (e instanceof tiger.Absyn.ForExp)
			return transExp(lev, (tiger.Absyn.ForExp) e);
		if (e instanceof tiger.Absyn.BreakExp)
			return transExp(lev, (tiger.Absyn.BreakExp) e);
		if (e instanceof tiger.Absyn.LetExp)
			return transExp(lev, (tiger.Absyn.LetExp) e);
		if (e instanceof tiger.Absyn.ArrayExp)
			return transExp(lev, (tiger.Absyn.ArrayExp) e);
		throw new Error("transExp");
	}

	/* End transExp */

	/*
	 * Begin transDec TransLate Exp transDec
	 */

	Exp transDec(Level lev, tiger.Absyn.TypeDec d) {
		tiger.Absyn.TypeDec it, itt;
		for (it = d; it != null; it = it.next) {
			boolean flag = false;
			itt = d;
			for (itt = d; itt != it; itt = itt.next)
				if (it.name.equals(itt.name)) {
					error(it.pos, "\"" + it.name + "\"type redefined");
					flag = true;
					break;
				}
			if (flag)
				continue;
			NAME nameit = (NAME) transTy(it.pos, it.name);
			nameit.bind(null);
			env.tenv.put(it.name, nameit);
		}
		for (it = d; it != null; it = it.next) {
			Type t = (Type) env.tenv.get(it.name);
			NAME tname = (NAME) t;
			// System.out.println(tname.name);//**********************************************
			Type type = transTy(it.ty);
			tname.bind(type);
		}
		for (it = d; it != null; it = it.next) {
			Type t = (Type) env.tenv.get(it.name);
			NAME tname = (NAME) t;
			Type type = tname.binding;
			if (type instanceof NAME) {
				NAME typename = (NAME) type;
				Type tt = (Type) env.tenv.get(typename.name);
				if (tt == null) {
					error(it.pos, "\"" + typename.name + "\" type undefined");
					tname.bind(newvoid);
				} else
					tname.bind(tt);
			} else if (type instanceof RECORD) {
				RECORD rtype = (RECORD) type, tmp;
				if (rtype == newrecord)
					continue;
				for (tmp = rtype; tmp != null; tmp = tmp.tail) {
					Type ftype = (Type) env.tenv
							.get(((NAME) tmp.fieldType).name);
					if (ftype == null) {
						error(it.pos, "\"" + ((NAME) tmp.fieldType).name
								+ "\" type undefined");
						ftype = newvoid;
					}
					ftype = ftype.actual();
					tmp.fieldType = ftype;
				}
				tname.bind(rtype);
			} else if (type instanceof ARRAY) {
				ARRAY atype = (ARRAY) type;
				Type etype = (Type) env.tenv.get(((NAME) atype.element).name);
				if (etype == null) {
					error(it.pos, "\"" + ((NAME) atype.element).name
							+ "\" type undefined");
					etype = newvoid;
				}
				etype = etype.actual();
				atype.element = etype;
				tname.bind(atype);
			} else
				tname.bind(type);
		}
		for (it = d; it != null; it = it.next) {
			NAME t = (NAME) env.tenv.get(it.name);
			if (t.isLoop())
				error(d.pos, "\"" + it.name + "\" cycle declaration");
		}
		return translate.emptyExp();
	}

	Exp transDec(Level lev, tiger.Absyn.VarDec d) {
		// System.out.println(d.name);//
		// ************************************************************
		ExpTy dexp = transExp(lev, d.init);
		Access acc = lev.allocLocal(d.escape);
		if (d.typ == null) {
			if (dexp.ty instanceof NIL)
				error(d.pos, "NIL initial no type");
			env.venv.put(d.name, new VarEntry(acc, dexp.exp, dexp.ty));
		} else {
			Type typ = transTy(d.typ);
			Type type = (Type) env.tenv.get(((NAME) typ).name);
			if (type == null) {
				error(d.pos, "\"" + ((NAME) typ).name + "\" type undefined");
				env.venv.put(d.name, newvoid);
				return translate.emptyExp();
			}
			type = type.actual();
			if (!checkTyEq(type, dexp.ty))
				error(d.pos, "var declaration type mismatch");
			env.venv.put(d.name, new VarEntry(acc, dexp.exp, type));
		}

		return translate.varDec(acc, dexp.exp, lev);
	}

	RECORD transTypeFields(tiger.Absyn.FieldList f) {
		return (RECORD) transTy(f);
	}

	Exp transDec(Level lev, tiger.Absyn.FunctionDec d) {
		tiger.Absyn.FunctionDec it, itt;
		for (it = d; it != null; it = it.next) {
			Type result;
			if (it.result != null) {
				NAME typ = (NAME) transTy(it.result);
				Type type = (Type) env.tenv.get(typ.name);
				if (type == null) {
					error(it.pos, "\"" + it.name
							+ "\" function return type undefine");
					type = newvoid;
				}
				result = type.actual();
			} else
				result = newvoid;
			RECORD formals = transTypeFields(it.params), p;
			if (formals != newrecord)
				for (p = formals; p != null; p = p.tail) {
					Type typ = (Type) env.tenv.get(((NAME) p.fieldType).name);
					if (typ == null) {
						error(it.pos, "argument type undefine");
						typ = newvoid;
					}
					typ = typ.actual();
					p.fieldType = typ;
				}
			boolean flag = false;
			for (itt = d; itt != it; itt = itt.next)
				if (itt.name.equals(it.name)) {
					flag = true;
					break;
				}
			if (flag) {
				error(it.pos, "\"" + it.name + "\" function redefined");
				break;
			}
			Level funlev = new Level(lev, it.name, new tiger.Util.BoolList(
					it.params));

			env.venv.put(it.name, new FunEntry(funlev, new Label(), formals,
					result));
		}
		for (it = d; it != null; it = it.next) {
			FunEntry fen = (FunEntry) env.venv.get(it.name);
			Type result = fen.result;
			RECORD formals = fen.formals;
			env.venv.beginScope();
			Level funlev = fen.level;
			loopflag.push();
			for (tiger.Absyn.FieldList p = it.params; p != null; p = p.tail) {
				Type pt = (Type) env.tenv.get(p.typ);
				if (pt == null) {
					error(p.pos, "\"" + it.name
							+ "\" function argument type undefinded");
					continue;
				}
				pt = pt.actual();
				Access acc = funlev.allocArguLocal(p.escape);
				env.venv.put(p.name,
						new VarEntry(acc, translate.emptyExp(), pt));
			}
			ExpTy fbody = transExp(funlev, it.body);
			translate.procEntryExit(fbody.exp, funlev, it.result != null);
			loopflag.pop();
			env.venv.endScope();
			if (!checkTyEq(fbody.ty, result))
				error(it.pos, "\"" + it.name
						+ "\" function return type mismatch");
		}
		return translate.emptyExp();
	}

	Exp transDec(Level lev, tiger.Absyn.Dec d) {
		if (d instanceof tiger.Absyn.VarDec)
			return transDec(lev, (tiger.Absyn.VarDec) d);
		if (d instanceof tiger.Absyn.FunctionDec)
			return transDec(lev, (tiger.Absyn.FunctionDec) d);
		if (d instanceof tiger.Absyn.TypeDec)
			return transDec(lev, (tiger.Absyn.TypeDec) d);
		throw new Error("transDec");
	}

	/* End transDec */

	/*
	 * Begin transTy transalte Type transTy
	 */

	Type transTy(tiger.Absyn.NameTy e) {
		return new NAME(e.name);
	}

	Type transTy(int pos, tiger.Symbol.Symbol e) {
		return transTy(new tiger.Absyn.NameTy(pos, e));
	}

	Type transTy(tiger.Absyn.ArrayTy e) {
		Type type = transTy(e.pos, e.typ);
		return new ARRAY(type);
	}

	Type transTy(tiger.Absyn.FieldList f) {
		tiger.Absyn.FieldList p = f, hs;
		RECORD record = newrecord, tail = null, tmp;
		boolean isApp;
		for (p = f; p != null; p = p.tail) {
			isApp = false;
			for (hs = f; hs != p; hs = hs.tail)
				if (hs.name.equals(p.name)) {
					error(f.pos, "\"" + p.name + "\" field element redefined");
					isApp = true;
					break;
				}
			if (!isApp) {
				Type type = transTy(p.pos, p.typ);
				tmp = new RECORD(p.name, type, null);
				if (tail == null) {
					record = tmp;
					tail = tmp;
				} else {
					tail.tail = tmp;
					tail = tmp;
				}
			}
		}
		return record;
	}

	Type transTy(tiger.Absyn.RecordTy e) {
		return transTy(e.fields);
	}

	Type transTy(tiger.Absyn.Ty e) {
		if (e instanceof tiger.Absyn.NameTy)
			return transTy((tiger.Absyn.NameTy) e);
		if (e instanceof tiger.Absyn.ArrayTy)
			return transTy((tiger.Absyn.ArrayTy) e);
		if (e instanceof tiger.Absyn.RecordTy)
			return transTy((tiger.Absyn.RecordTy) e);
		throw new Error("transTy");
	}

	/* End transTy */

	public Frag transProg(tiger.Frame.Frame globalFrame, tiger.Absyn.Exp exp) {
		globalLevZero = new Level(globalFrame);
		Level mainLevel = new Level(globalLevZero, tiger.Symbol.Symbol
				.symbol("t_main"), null);
		env.initialize();
		ExpTy mainFun = transExp(mainLevel, exp);
		translate.procEntryExit(mainFun.exp, mainLevel, false);
		return translate.getResult();
	}
}