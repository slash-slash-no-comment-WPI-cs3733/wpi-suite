package taskManager.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.core.models.UserSerializer;

public class WorkflowModel extends AbstractModel {
	List<StageModel> stagesList;
	String name;

	public WorkflowModel() {
		stagesList = new ArrayList<StageModel>();
		// TODO Add default stages
	}

	/**
	 * Moves a stage currently in the WorkFlowModel to the given position on its
	 * list.
	 *
	 * @param s
	 *            Stage to be moved.
	 * @param index
	 *            Target location in the list
	 *
	 */
	public void moveStage(int index, StageModel s) {
		if (!stagesList.contains(s)) {
			throw new IllegalArgumentException(
					"Stage being moved must already be in the workflow.");
		}
		if (stagesList.size() <= index) {
			index = stagesList.size();
		}
		if (index < 0) {
			index = 0;
		}
		stagesList.remove(s);
		stagesList.add(index, s);
		return;
	}

	/**
	 * Adds a stage to index items from the beginning of the WorkFlowModel Stage
	 * is added to the end of the current list.
	 *
	 * @param StageModel
	 *            newStage Stage to be added.
	 */
	public synchronized void addStage(StageModel newStage) {
		addStage(stagesList.size(), newStage);
	}

	/**
	 * Adds a stage to index items from the beginning of the WorkFlowModel
	 *
	 * @param int index Index in the list of stages where we are adding the new
	 *        stage.
	 * @param StageModel
	 *            newStage Stage to be added.
	 */
	public synchronized void addStage(int index, StageModel newStage) {
		for (StageModel stage : stagesList) {
			if (stage.getID().equals(newStage.getID())) {
				newStage.setID(newStage.getID() + '#');
				addStage(index, newStage);
				return;
			}
		}
		stagesList.add(index, newStage);
	}

	/**
	 * Gets a list of the stages in this workflow.
	 *
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
		Gson gson = new GsonBuilder().registerTypeAdapter(WorkflowModel.class,
				new UserSerializer()).create();
		return gson.toJson(this, User.class);
	}

	@Override
	public Boolean identify(Object o) {
		if (o instanceof WorkflowModel) {
			return ((WorkflowModel) o).name == name;
		}
		return false;
	}
}
