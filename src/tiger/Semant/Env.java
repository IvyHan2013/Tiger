package tiger.Semant;

import tiger.Symbol.*;
import tiger.Types.*;
import tiger.errormsg.*;
import tiger.Temp.*;
import tiger.Util.*;
import tiger.Translate.*;

public class Env {
	INT newint = new INT();

	STRING newstring = new STRING();

	NIL newnil = new NIL();

	VOID newvoid = new VOID();

	Table venv;

	Table tenv;

	ErrorMsg errorMsg;

	Env(ErrorMsg err) {
		errorMsg = err;
	}

	public void initialize() {
		venv = new Table();
		tenv = new Table();

		NAME nameint = new NAME(Symbol.symbol("int"));
		nameint.bind(new INT());
		tenv.put(nameint.name, nameint);

		NAME namestring = new NAME(Symbol.symbol("string"));
		namestring.bind(new STRING());
		tenv.put(namestring.name, namestring);

		NAME namenil = new NAME(Symbol.symbol("nil"));
		namenil.bind(new NIL());
		tenv.put(namenil.name, namenil);

		Symbol print = Symbol.symbol("print");
		RECORD printargu = new RECORD(Symbol.symbol("s"), newstring, null);
		venv.put(print, new FunEntry(new Level(Semant.globalLevZero, print,
				new BoolList(true, null)), new Label(), printargu, newvoid,
				true));

		Symbol printi = Symbol.symbol("printi");
		RECORD printiargu = new RECORD(Symbol.symbol("i"), newint, null);
		venv.put(printi, new FunEntry(new Level(Semant.globalLevZero, printi,
				new BoolList(true, null)), new Label(), printiargu, newvoid,
				true));

		Symbol flush = Symbol.symbol("flush");
		RECORD flushargu = null;
		venv.put(flush, new FunEntry(new Level(Semant.globalLevZero, flush,
				null), new Label(), flushargu, newvoid, true));

		Symbol getchar = Symbol.symbol("getchar");
		RECORD getcharargu = null;
		venv.put(getchar, new FunEntry(new Level(Semant.globalLevZero, getchar,
				null), new Label(), getcharargu, newstring, true));

		Symbol ord = Symbol.symbol("ord");
		RECORD ordargu = new RECORD(Symbol.symbol("s"), newstring, null);
		venv.put(ord, new FunEntry(new Level(Semant.globalLevZero, ord,
				new BoolList(true, null)), new Label(), ordargu, newint, true));

		Symbol chr = Symbol.symbol("chr");
		RECORD chrargu = new RECORD(Symbol.symbol("i"), newint, null);
		venv.put(chr, new FunEntry(new Level(Semant.globalLevZero, chr,
				new BoolList(true, null)), new Label(), chrargu, newstring,
				true));

		Symbol size = Symbol.symbol("size");
		RECORD sizeargu = new RECORD(Symbol.symbol("s"), newstring, null);
		venv
				.put(size, new FunEntry(new Level(Semant.globalLevZero, size,
						new BoolList(true, null)), new Label(), sizeargu,
						newint, true));

		Symbol substring = Symbol.symbol("substring");
		RECORD substringargu3 = new RECORD(Symbol.symbol("n"), newint, null);
		RECORD substringargu2 = new RECORD(Symbol.symbol("first"), newint,
				substringargu3);
		RECORD substringargu1 = new RECORD(Symbol.symbol("s"), newstring,
				substringargu2);
		RECORD substringargu = substringargu1;
		venv.put(substring, new FunEntry(new Level(Semant.globalLevZero,
				substring, new BoolList(true, new BoolList(true, new BoolList(
						true, null)))), new Label(), substringargu, newstring,
				true));

		Symbol concat = Symbol.symbol("concat");
		RECORD concatargu2 = new RECORD(Symbol.symbol("s2"), newstring, null);
		RECORD concatargu1 = new RECORD(Symbol.symbol("s1"), newstring,
				concatargu2);
		RECORD concatargu = concatargu1;
		venv.put(concat, new FunEntry(new Level(Semant.globalLevZero, concat,
				new BoolList(true, new BoolList(true, null))), new Label(),
				concatargu, newstring, true));

		Symbol not = Symbol.symbol("not");
		RECORD notargu = new RECORD(Symbol.symbol("i"), newint, null);
		venv.put(not, new FunEntry(new Level(Semant.globalLevZero, not,
				new BoolList(true, null)), new Label(), notargu, newint, true));

		Symbol exit = Symbol.symbol("exit");
		RECORD exitargu = new RECORD(Symbol.symbol("i"), newint, null);
		venv.put(exit,
				new FunEntry(new Level(Semant.globalLevZero, exit,
						new BoolList(true, null)), new Label(), exitargu,
						newvoid, true));
	}
}
