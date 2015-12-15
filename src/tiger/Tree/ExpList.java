package tiger.Tree;

public class ExpList {
	public AbsExp head;

	public ExpList tail;

	public ExpList(AbsExp h, ExpList t) {
		head = h;
		tail = t;
	}
}
