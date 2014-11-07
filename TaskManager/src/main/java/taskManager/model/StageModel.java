package taskManager.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserSerializer;


public class StageModel extends AbstractModel {

	// List of tasks in this stage
	private List<TaskModel> taskList;

	// The next ID to be handed out
	private static int idCount = 0;

	// The id of this stage
	private final int id;

	// The name of this stage
	private String name;

	// Whether users can remove this stage
	private boolean removable;

	public StageModel(String name) {
		this(name, true);
	}

	public StageModel(String name, boolean removable) {
		id = idCount++;
		this.name = name;
		this.removable = removable;
		taskList = new ArrayList<TaskModel>();
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	public List<TaskModel> getTasks() {
		return taskList;
	}

	public void addTask(TaskModel t) {
		addTask(taskList.size(),t);
	}
	
	public void addTask(int index, TaskModel t) {
		taskList.add(index, t);
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
        String json;
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserSerializer()).create();
        json = gson.toJson(this, User.class);

        return json;
	}

	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		if(o instanceof StageModel) {
			return ((StageModel) o).getID() == this.getID();
		}
		return false;
	}

}
