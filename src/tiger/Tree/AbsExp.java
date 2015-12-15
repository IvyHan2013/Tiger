package tiger.Tree;

abstract public class AbsExp {
	abstract public ExpList kids();

	abstract public AbsExp build(ExpList kids);
}
