package taskManager.model;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class ReportManagerTest {

	private ReportsManagerModel rmm;
	private WorkflowModel wm;
	private StageModel sm1;
	private StageModel sm2;
	private StageModel sm3;
	private TaskModel tm1;
	private TaskModel tm2;
	private TaskModel tm3;
	private TaskModel tm4;
	private TaskModel tm5;
	private TaskModel tm6;
	private TaskModel tm7;
	private TaskModel tm8;
	private User u1;
	private User u2;
	private User u3;
	private Set<User> users;

	@Before
	public void intialize() {
		wm = new WorkflowModel("Workflow");
		rmm = new ReportsManagerModel(wm);
		sm1 = new StageModel("Stage 1");
		sm2 = new StageModel("Stage 2");
		sm3 = new StageModel("Stage 3");
		wm.addStage(sm1);
		wm.addStage(sm2);
		wm.addStage(sm3);
		tm1 = new TaskModel("Task 1", sm1);
		tm1.setDueDate(new Date(1000));
		tm2 = new TaskModel("Task 2", sm2);
		tm2.setDueDate(new Date(2000));
		tm3 = new TaskModel("Task 3", sm3);
		tm3.setDueDate(new Date(3000));
		tm4 = new TaskModel("Task 4", sm1);
		tm4.setDueDate(new Date(4000));
		tm5 = new TaskModel("Task 5", sm2);
		tm5.setDueDate(new Date(5000));
		tm6 = new TaskModel("Task 6", sm3);
		tm6.setDueDate(new Date(6000));
		tm7 = new TaskModel("Task 7", sm1);
		tm7.setDueDate(new Date(7000));
		tm8 = new TaskModel("Task 8", sm2);
		tm8.setDueDate(new Date(8000));
		u1 = new User("User 1", "User 1", null, 1);
		u2 = new User("User 2", "User 2", null, 2);
		u3 = new User("User 3", "User 3", null, 3);
		users = new HashSet<User>();
		users.add(u1);
		users.add(u2);
		users.add(u3);
	}

	@Test
	public void testVelocity() {
		tm1.addAssigned(u1);
		tm1.setEstimatedEffort(5);

		tm2.addAssigned(u2);
		tm2.setEstimatedEffort(20);

		tm3.addAssigned(u3);
		tm3.setEstimatedEffort(14);

		tm4.addAssigned(u1);
		tm4.addAssigned(u2);
		tm4.setEstimatedEffort(6);

		tm5.addAssigned(u1);
		tm5.addAssigned(u3);
		tm5.setEstimatedEffort(8);

		tm6.addAssigned(u2);
		tm6.addAssigned(u3);
		tm6.setEstimatedEffort(2);

		tm7.addAssigned(u1);
		tm7.addAssigned(u2);
		tm7.addAssigned(u3);
		tm7.setEstimatedEffort(12);

		tm8.setEstimatedEffort(10);

		// U1 5 +3+4+4 = 16
		// U2 20+3+1+4 = 28
		// U3 14+4+1+4 = 23

		// U1 5 +6+8+12 = 31
		// U2 20+6+2+12 = 40
		// U3 14+8+2+12 = 36

		Date start = new Date(0);
		Date end = new Date();
		Map<User, Map<Date, Double>> dataWithAverage = rmm.getVelocity(users,
				start, end, true);
		Double totalU1Effort = 0.0;
		Double totalU2Effort = 0.0;
		Double totalU3Effort = 0.0;
		System.out.println(dataWithAverage.get(u1).values().size());
		for (Double avgEffort : dataWithAverage.get(u1).values()) {
			totalU1Effort += avgEffort;
		}
		for (Double avgEffort : dataWithAverage.get(u2).values()) {
			totalU2Effort += avgEffort;
		}
		for (Double avgEffort : dataWithAverage.get(u3).values()) {
			totalU3Effort += avgEffort;
		}
		assertEquals(16.0, totalU1Effort, 0.001);
		assertEquals(28.0, totalU2Effort, 0.001);
		assertEquals(23.0, totalU3Effort, 0.001);

		Map<User, Map<Date, Double>> dataWithTotals = rmm.getVelocity(users,
				start, end, false);
		totalU1Effort = 0.0;
		totalU2Effort = 0.0;
		totalU3Effort = 0.0;
		for (Double avgEffort : dataWithTotals.get(u1).values()) {
			totalU1Effort += avgEffort;
		}
		for (Double avgEffort : dataWithTotals.get(u2).values()) {
			totalU2Effort += avgEffort;
		}
		for (Double avgEffort : dataWithTotals.get(u3).values()) {
			totalU3Effort += avgEffort;
		}
		assertEquals(totalU1Effort, 31.0, 0.001);
		assertEquals(totalU2Effort, 40.0, 0.001);
		assertEquals(totalU3Effort, 36.0, 0.001);
	}
}
