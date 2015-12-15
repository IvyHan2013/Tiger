package tiger.Translate;

public abstract class Exp {
	public abstract tiger.Tree.AbsExp unEx();

	public abstract tiger.Tree.Stm unNx();

	public abstract tiger.Tree.Stm unCx(tiger.Temp.Label t, tiger.Temp.Label f);

}
