package application.dialog;

import java.io.File;

public interface UploadDialogListener {

	void accepted(File file);
	void denied();

}
