package tiger.Translate;

public class ExpList {

	public Exp head;

	public ExpList tail;

	public ExpList(Exp e, ExpList t) {
		head = e;
		tail = t;
	}

}
