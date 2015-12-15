package tiger.Semant;

public class ForState {// stack: record is a iterator in any loop
	class Stack {
		int num;

		tiger.Temp.Label loopOut[];

		Stack par, next;

		Stack(int n) {
			num = n;
			par = null;
			next = null;
			loopOut = new tiger.Temp.Label[1000];
		}
	};

	Stack now;

	ForState() {
		now = new Stack(0);
	}

	void pop() {
		if (now.par == null)
			throw new Error("stack overflow");
		now = now.par;
	}

	void push() {
		if (now.next != null) {
			now = now.next;
			now.num = 0;
		} else {
			now.next = new Stack(0);
			now.next.par = now;
			now = now.next;
		}
	}

	boolean isFor() {
		return now.num != 0;
	}

	void inFor() {
		now.num++;
	}
	
	void inFor(tiger.Temp.Label done){
		now.loopOut[now.num++]=done;
	}

	void outFor() {
		if (now.num == 0)
			throw new Error("no any loop");
		now.num--;
	}
	
	tiger.Temp.Label getLabel(){
		return now.loopOut[now.num-1];
	}
}
