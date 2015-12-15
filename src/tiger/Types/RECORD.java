package  tiger.Types;
import tiger.Symbol.*;

public class RECORD extends Type {
   public  Symbol fieldName;
   public Type fieldType;
   public RECORD tail;
   public RECORD(Symbol n, Type t, RECORD x) {
       fieldName=n; fieldType=t; tail=x;
   }
   public boolean coerceTo(Type t) {
	return this==t.actual();
   }
}
   

