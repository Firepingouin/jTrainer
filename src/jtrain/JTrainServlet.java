package jtrain;

import java.awt.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.sun.java.swing.plaf.windows.resources.windows;

/**
 * @author mlazzje
 *
 *         Servlet to manage loading of : - training plan - training exercises
 *         And manage add of : - training plan - training exercises
 * 
 */
@SuppressWarnings("serial")
public class JTrainServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		// Get the printwriter object from resp to write the required json
		// object to the output stream
		PrintWriter out = resp.getWriter();
		String jsonObject = "{ firstname: 'Nico', lastname: 'Pari' }";
		out.print(jsonObject);
		out.flush();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("action");

		if (action.equals("add")) {
			this.addTrainingPlan(req);
		}
	}

	private void addTrainingPlan(HttpServletRequest req) {

		// Récupération de l'objet JSON
		String jsonString = req.getParameter("trainingPlan");
		if (jsonString != null) {
			Queue queue = QueueFactory.getDefaultQueue();

			// Ajout d’une tache simple
			TaskOptions task = TaskOptions.Builder.withUrl("/addTrainingPlan")
					.param("trainingPlan", jsonString);
			queue.add(task);

			// Ajout d’une tache simple avec des paramètres de configuration
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("X-AppEngine-TaskName", "task2");
			headers.put("X-AppEngine-TaskRetryCount", "4");
			queue.deleteTask("task");
			queue.purge();
		}
	}
}
