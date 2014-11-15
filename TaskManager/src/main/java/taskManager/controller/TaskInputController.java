package taskManager.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import taskManager.JanewayModule;
import taskManager.view.EditTaskView;

public class TaskInputController implements KeyListener {

	EditTaskView etv = JanewayModule.etv;

	public TaskInputController() {
		this.checkFields();
	}

	/**
	 * checks to see if the edit task fields aren't empty and meet the
	 * requirements. If a field doesn't meet the requirements, display an error
	 */
	private boolean checkFields() {
		boolean titleValid = true;
		boolean descriptionValid = true;
		boolean estEffortValid = true;

		// checks each required field and determines if it meets the
		// requirements for that field
		if (etv.getTitle().getText().isEmpty()) {
			titleValid = false;
		}
		if (etv.getDescription().getText().isEmpty()) {
			descriptionValid = false;
		}
		// TODO add est effort min and max
		if (etv.getEstEffort().getText().isEmpty()) {
			estEffortValid = false;
		}

		// display the errors
		etv.setTitleErrorVisible(!titleValid);
		etv.setDescriptionErrorVisible(!descriptionValid);
		etv.setEstEffortErrorVisible(!estEffortValid);

		return titleValid && descriptionValid && estEffortValid;

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (this.checkFields()) {
			etv.enableSave();
		} else {
			etv.disableSave();
		}
	}

}
