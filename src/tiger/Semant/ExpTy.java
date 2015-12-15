package tiger.Semant;
import tiger.Translate.Exp;
import tiger.Types.*;

public class ExpTy {
	Exp exp;
	Type ty;
	ExpTy(Exp e, Type t){
		exp=e; ty=t;		
	}
}
