package ca.gc.cra.fxit.xmlt.exception;

import ca.gc.cra.fxit.xmlt.task.ITask;

public class InvalidMessageRefIdException extends TaskException {
	private static final long serialVersionUID = 7616368751768324813L;

	public InvalidMessageRefIdException(int s, String msg, ITask t){
		super(s,msg, t);
	}
}
