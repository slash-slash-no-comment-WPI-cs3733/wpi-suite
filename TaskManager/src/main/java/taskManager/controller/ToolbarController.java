/*******************************************************************************
 * Copyright (c) 2012-2014 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package taskManager.controller;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import taskManager.draganddrop.DDTransferHandler;
import taskManager.localization.Localizer;
import taskManager.model.StageModel;
import taskManager.model.WorkflowModel;
import taskManager.view.ReportsView;
import taskManager.view.RotationView;
import taskManager.view.StageView;
import taskManager.view.TaskView;
import taskManager.view.ToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;

/**
 * A controller for the toolbar view
 *
 * @author Beth Martino
 * @author Sam Khalandovsky
 */
public class ToolbarController extends DropTargetAdapter implements
		ActionListener, ItemListener, ComponentListener {

	private ToolbarView view;

	private static ToolbarController instance;

	/**
	 * Hide Singleton constructor
	 */
	private ToolbarController() {
		reset();
	}

	public void reset() {
		view = new ToolbarView(this);
	}

	/**
	 * Returns the singleton instance of ToolbarController. Creates one if
	 * needed.
	 * 
	 * @return the ToolbarController singleton
	 */
	public static ToolbarController getInstance() {
		if (instance == null) {
			instance = new ToolbarController();
		}
		return instance;
	}

	/**
	 * Returns the associated ToolbarView.
	 * 
	 * @return The associated ToolbarView
	 */
	public ToolbarView getView() {
		return view;
	}

	/**
	 * Sets the visible title in the toolbar, hyphenating if necessary
	 *
	 * @param name
	 *            The project name
	 */
	public void setProjectName(String name) {
		view.setProjectName(hyphenateProjectName(name));
	}

	/**
	 * Hyphenates the project name to fit in the toolbar if necessary.
	 * 
	 * @param name
	 *            The project name
	 * @return The project name, hyphenated if necessary
	 */
	private String hyphenateProjectName(String name) {
		int toolbarWidth = view.getWidth();
		// If the toolbar width is zero, the toolbar is not visible, so do not
		// attempt to hyphenate
		if (toolbarWidth == 0) {
			return name;
		}

		// Find the sum of the width of the children panels to see if it
		// overflows
		int otherChildrenWidth = 0;
		for (Component c : view.getComponents()) {
			otherChildrenWidth += c.getWidth();
		}
		otherChildrenWidth -= view.getProjectName().getWidth();
		int goalWidth = toolbarWidth - otherChildrenWidth;

		// Because of the word wrap, if there there is an overflow there will be
		// some section with no whitespace that causes the overflow, so find it
		// and hyphenate it
		String[] chunks = name.split(" ");
		List<String> newChunks = new ArrayList<String>();
		// For each string, hyphenate it if it exceeds the goal width
		for (String s : chunks) {
			int stringWidth = SwingUtilities.computeStringWidth(
					view.getProjectName().getFontMetrics(
							view.getProjectName().getFont()), s);
			if (stringWidth > goalWidth) {
				String hyphenatedStart = s + "-";
				String hyphenatedEnd = "";
				// Reduce the width of the string with hyphenation until it
				// fits
				while (SwingUtilities.computeStringWidth(view.getProjectName()
						.getFontMetrics(view.getProjectName().getFont()),
						hyphenatedStart) > goalWidth
						&& hyphenatedStart.length() >= 2) {
					hyphenatedEnd += hyphenatedStart.charAt(hyphenatedStart
							.length() - 2);
					hyphenatedStart = hyphenatedStart.substring(0,
							hyphenatedStart.length() - 2) + "-";
				}
				newChunks.add(hyphenatedStart);
				newChunks.add(hyphenatedEnd);
			} else {
				newChunks.add(s);
			}
		}

		// When done concatenate all the chunks
		String newName = "";
		for (String s : newChunks) {
			newName += s + " ";
		}
		// Remove the trailing space
		newName.substring(0, newName.length() - 1);
		return newName;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object button = e.getSource();
		if (button instanceof JButton) {
			final String name = ((JButton) button).getName();
			// close the task preview pane
			WorkflowController.getInstance().removeTaskInfos(true);
			WorkflowController.getInstance().removeChangeTitles();
			switch (name) {
			case ToolbarView.CREATE_TASK:
				TabPaneController.getInstance().addCreateTaskTab();
				break;
			case ToolbarView.CREATE_STAGE:
				// add a new stage from workflow controller
				WorkflowController.getInstance().addStageToView();
				break;
			case ToolbarView.REPORT:
				ReportsView rtv = new ReportsView();
				rtv.setController(new ReportsManager(rtv));
				TabPaneController.getInstance().addReportsTab(rtv);
				break;
			case ToolbarView.TASK_ANGLES:
				RotationController.resetAngles();
				WorkflowController.getInstance().reloadData();
			}
		} else if (button instanceof JComboBox) {
			Localizer.setLanguage(view.getSelectedLanguage());
		}
	}

	/**
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	@Override
	public void drop(DropTargetDropEvent e) {
		final Component target = e.getDropTargetContext().getComponent();
		if (target instanceof JLabel) {
			final String name = ((JLabel) target).getName();

			final Transferable trans = e.getTransferable();
			if (trans.isDataFlavorSupported(DDTransferHandler.getTaskFlavor())) {
				final TaskView taskV;
				Object transferData = null;
				try {
					transferData = trans.getTransferData(DDTransferHandler
							.getTaskFlavor());
				} catch (UnsupportedFlavorException e1) {
					e1.printStackTrace();
					return;
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}

				// ignore rotation views
				if (transferData instanceof RotationView) {
					transferData = ((RotationView) transferData).getPanel();
				}

				if (transferData instanceof TaskView) {
					taskV = (TaskView) transferData;
				} else {
					return;
				}

				switch (name) {
				case ToolbarView.DELETE:
					if (target.isEnabled()) {
						taskV.getController().deleteTask(); // remove from model
						taskV.getParent().remove(taskV); // remove from view
						// Reload and save workflow.
						WorkflowController.getInstance().reloadData();
						WorkflowController.getInstance().repaintView();
						WorkflowModel.getInstance().save();
						DDTransferHandler.dragSaved = true;
					}
					break;
				case ToolbarView.ARCHIVE:
					taskV.getController().setArchived(
							!taskV.getController().isArchived());
					// Reload and save workflow.
					WorkflowController.getInstance().reloadData();
					WorkflowController.getInstance().repaintView();
					WorkflowModel.getInstance().save();
					DDTransferHandler.dragSaved = true;
					break;
				} // end switch
			} else if (trans.isDataFlavorSupported(DDTransferHandler
					.getStageFlavor())) {
				final StageView stageV;
				try {
					stageV = (StageView) trans
							.getTransferData(DDTransferHandler.getStageFlavor());
				} catch (Exception ex) {
					System.out.println(ex.getStackTrace());
					return;
				}
				final StageController stageC = stageV.getController();
				final WorkflowModel model = WorkflowModel.getInstance();
				final List<StageModel> stages = model.getStages();

				if (ToolbarView.DELETE.equals(name)) {
					// Delete only when there are 2 or more stages.
					if (stages.size() >= 2) {
						// If the stage has tasks, show a confirmation dialog,
						// else
						// just delete the stage.
						if (!stageC.isEmpty()) {
							final Integer choice = JOptionPane
									.showConfirmDialog(
											TabPaneController.getInstance()
													.getView(),
											"The "
													+ stageV.getName()
													+ " stage contains tasks. Are you sure you want to delete this stage?",
											"Warning - Deleting a stage containing tasks",
											JOptionPane.YES_NO_OPTION);
							if (choice.equals(JOptionPane.NO_OPTION)) {
								return;
							}
						}
						stageC.deleteStage();
						DDTransferHandler.dragSaved = true;
						model.save();
						WorkflowController.getInstance().reloadData();
					} else {
						JOptionPane.showConfirmDialog(TabPaneController
								.getInstance().getView(),
								"You cannot delete the last stage.",
								"Warning - Invalid stage deletion",
								JOptionPane.CLOSED_OPTION);
					}
				}
			}
		} // End instanceof
	}

	/**
	 * Rejects or accepts the drag to make drag-over cursor correct.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.awt.dnd.DropTargetAdapter#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		String name = dtde.getDropTargetContext().getComponent().getName();

		if (view.isIconEnabled(name)) {
			dtde.acceptDrag(dtde.getDropAction());
		} else {
			dtde.rejectDrag();
		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		final Object checkBox = e.getSource();
		if (checkBox instanceof JCheckBox) {
			switch (((JCheckBox) checkBox).getName()) {
			case ToolbarView.FUN_MODE:
				if (e.getStateChange() == ItemEvent.SELECTED) {
					view.showFunButtons();
				} else {
					view.hideFunButtons();
				}
			}
		}
		// Reload the workflow view.
		WorkflowController.pauseInformation = false;
		WorkflowController.getInstance().removeTaskInfos(true);
		WorkflowController.getInstance().reloadData();
	}

	/**
	 * Set toolbar icons during drag action
	 *
	 * @param flavor
	 *            DataFlavor of drag
	 * @param comp
	 *            component being dragged
	 */
	public void setIconState(JComponent comp) {
		// ignore rotation views
		if (comp instanceof RotationView) {
			comp = ((RotationView) comp).getPanel();
		}

		if (comp instanceof TaskView) {
			boolean isArchived = ((TaskView) comp).getController().isArchived();
			if (isArchived) {
				view.setArchiveIcon(ToolbarView.UNARCHIVE);
			} else {
				view.setArchiveIcon(ToolbarView.ARCHIVE);
			}
			view.setDeleteEnabled(isArchived);
			view.setArchiveEnabled(true);
		} else if (comp instanceof StageView) {
			view.setDeleteEnabled(true);
			view.setArchiveEnabled(false);
		}
	}

	/**
	 * Reset the state of the icons
	 */
	public void resetIconState() {
		view.setArchiveEnabled(false);
		view.setDeleteEnabled(false);
		view.setArchiveIcon(ToolbarView.ARCHIVE);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// Reset the project name so that it will get hyphenated if necessary
		setProjectName(ConfigManager.getConfig().getProjectName());
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// Do nothing
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// Reset the project name so that it will get hyphenated if necessary
		// setProjectName(ConfigManager.getConfig().getProjectName());
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// Do nothing
	}
}
