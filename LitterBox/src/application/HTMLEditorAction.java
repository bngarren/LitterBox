package application;

import com.model.Literature;


public abstract class HTMLEditorAction implements Runnable {

	private HTMLEditorActionListener listener;

	private Literature literature;
	private Object data;

	abstract void execute();

	@Override
	public void run() {
		execute();
		notifyListener();
	}

	private void notifyListener(){
		if (listener != null)
			listener.actionComplete(this);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Literature getLiterature() {
		return literature;
	}

	public void setLiterature(Literature literature) {
		this.literature = literature;
	}

	public HTMLEditorActionListener getListener() {
		return listener;
	}

	public void setListener(HTMLEditorActionListener listener) {
		this.listener = listener;
	}


}
