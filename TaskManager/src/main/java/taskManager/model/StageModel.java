package taskManager.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

/**
 * Description
 *
 * @author joe
 * @version Nov 6, 2014
 */
public class StageModel extends AbstractModel {

	// List of tasks in this stage
	private List<TaskModel> taskList;

	// The public name of this stage
	private String name;

	// The private name of this stage. May contain octothorps to ensure
	// uniqueness within this workflow.
	private String id;

	// Whether users can remove this stage
	private boolean removable;

	/**
	 * Constructor for Stage Models. Defaults to removable.
	 *
	 * @param workflow
	 *            The workflow that the stage is a part of.
	 * @param name
	 *            The name of the stage.
	 */
	public StageModel(WorkflowModel workflow, String name) {
		this(workflow, name, true);
	}

	/**
	 * Constructor for Stage Models
	 *
	 * @param workflow
	 *            The workflow that the stage is a part of.
	 * @param name
	 *            The name of the stage.
	 * @param removable
	 *            Whether or not the stage can be removed.
	 */
	public StageModel(WorkflowModel workflow, String name, boolean removable) {
		this.name = name;
		this.id = name;
		this.removable = removable;
		taskList = new ArrayList<TaskModel>();
		workflow.addStage(this);
	}

	/**
	 * Get the name of the Stage.
	 *
	 * @return the name of the stage
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the internal id of the Stage
	 *
	 * @return the id of the Stage
	 */
	public String getID() {
		return id;
	}

	/**
	 * Set the internal id of the Stage.
	 *
	 * @param id
	 *            set the id of the stage.
	 */
	public void setID(String id) {
		this.id = id;
	}

	public List<TaskModel> getTasks() {
		return taskList;
	}

	/**
	 * Add a task to the end of the task list
	 *
	 * @param t
	 *            the task to add.
	 */
	public void addTask(TaskModel t) {
		addTask(taskList.size(), t);
	}

	/**
	 * Insert a task in a specific spot in the task list. Tasks with duplicate
	 * public names will changed internal names by adding a '#'
	 *
	 * @param index
	 *            the index to insert the task at
	 * @param newTask
	 *            the task to add
	 */
	public void addTask(int index, TaskModel newTask) {
		for (TaskModel task : taskList) {
			if (task.getID().equals(newTask.getID())) {
				newTask.setID(newTask.getID() + '#');
				addTask(index, newTask);
				return;
			}
		}
		taskList.add(index, newTask);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toJson() {
		return (new Gson()).toJson(this);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		if (o instanceof StageModel) {
			return ((StageModel) o).getID() == this.getID();
		}
		return false;
	}

}
