package taskManager.model;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserSerializer;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class WorkflowModel extends AbstractModel {
	List<StageModel> stagesList;
	private static int idCount = 0;
	private final int id;
	String name;
	
	public WorkflowModel() {
		stagesList = new ArrayList<StageModel>();
		id = idCount++;
		// TODO Add default stages
	}
	
	/**
	 * Moves a stage currently in the WorkFlowModel to the given position on it's list.
	 * @param s Stage to be moved.
	 * @param index Target location in the list
	 * 
	 */
	public void moveStage(int index, StageModel s) {
		if(!stagesList.contains(s)) {
			throw new IllegalArgumentException("Stage being moved must already be in the workflow.");
		}
		if(stagesList.size() <= index) {
			index = stagesList.size();
		}
		stagesList.remove(s);
		stagesList.add(index, s);
		return;
	}
	
	/**
	 * Adds a stage to the end of the WorkFlowModel 
	 * @param s Stage to be added.
	 */
	public void addStage(StageModel s) {
		addStage(stagesList.size(), s);
	}
	
	/**
	 * Adds a stage to index items from the beginning of the WorkFlowModel 
	 * @param s Stage to be added.
	 */
	public void addStage(int index, StageModel s) {
		stagesList.add(index, s);
	}
	/**
	 * Gets a list of the stages in this workflow.
	 * @return the list of stages
	 */
	public List<StageModel> getStages() {
		return stagesList;
	}
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Serializes this Workflow model into a JSON string.
     * 
     * @return the JSON representation of this User
     */
	@Override
    public String toJson() {
        String json;
        Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserSerializer()).create();
        json = gson.toJson(this, User.class);

        return json;
    }


	@Override
	public Boolean identify(Object o) {
		if(o instanceof WorkflowModel) {
			return true;
		} else {
			return false;
		}
	}
}
