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

	// We have a public constructor, but we shouldn't construct a Stage unless
	// we add it to the parent.
	// Option a: Include WorkflowModel in constructor.
	// Option b: Disallow public construction (?)
	public StageModel(WorkflowModel workflow, String name) {
		this(workflow, name, true);
	}

	public StageModel(WorkflowModel workflow, String name, boolean removable) {
		this.name = name;
		this.id = name;
		this.removable = removable;
		taskList = new ArrayList<TaskModel>();
		workflow.addStage(this);
	}

	public String getName() {
		return name;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public List<TaskModel> getTasks() {
		return taskList;
	}

	public void addTask(TaskModel t) {
		addTask(taskList.size(), t);
	}

	// Indicies here are confusing, aren't tasks unordered?
	/**
	 * Description goes here.
	 *
	 * @param index
	 *            the index to insert
	 * @param newTask
	 */
	public void addTask(int index, TaskModel newTask) {
		for (TaskModel task : taskList) {
			if (task.getID().equals(newTask.getID())) {
				newTask.setID(newTask.getID() + '#');
				addTask(index, newTask);
				break;
			}
		}
		taskList.add(index, newTask);
	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#save()
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#delete()
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/*
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJson()
	 */
	@Override
	public String toJson() {
		return (new Gson()).toJson(this);
	}

	@Override
	public Boolean identify(Object o) {
		if (o instanceof StageModel) {
			return ((StageModel) o).getID() == this.getID();
		}
		return false;
	}

}
