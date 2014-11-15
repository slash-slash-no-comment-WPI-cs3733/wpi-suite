package taskManager.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import taskManager.JanewayModule;
import taskManager.view.EditTaskView;

public class TaskInputController implements KeyListener {

	EditTaskView etv = JanewayModule.etv;

	public TaskInputController() {

	}

	/**
	 * checks to see if the edit task fields aren't empty and meet the
	 * requirements
	 */
	private boolean checkFields() {
		boolean titleValid = true;
		boolean descriptionValid = true;
		boolean estEffortValid = true;
		boolean actEffortValid = true;
		if (etv.getTitle().getText().isEmpty()) {
			titleValid = false;
		}
		if (etv.getDescription().getText().isEmpty()) {
			descriptionValid = false;
		}
		// TODO add est effort min and max
		if (etv.getEstEffort().getText().isEmpty()) {
			descriptionValid = false;
		}
		if (etv.getActEffort().getText().isEmpty()
				&& etv.getActEffort().isVisible()) {
			descriptionValid = false;
		}
		return titleValid && descriptionValid && estEffortValid
				&& actEffortValid;

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (this.checkFields()) {
			etv.enableSave();
		} else {
			etv.disableSave();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
