package jtrain;

import java.awt.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
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
		PrintWriter out = resp.getWriter();
		JSONObject details = new JSONObject();
		
		String key = req.getParameter("key");
		if (!key.equals("")) {
			details = this.getDetails(key);
		}
		out.print(details);
		out.flush();
	}

	private JSONObject getDetails(String key) {
		JSONObject details = new JSONObject();
//		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		// On filtre sur le domaine
//		Entity tp;
//		try {
//			tp = datastore.get(KeyFactory.stringToKey(key.toString()));
//			if(tp != null) {
//				JSONObject trainingPlanJson = new JSONObject();
//				ArrayList<JSONObject> exercices = new ArrayList<JSONObject>();
//				trainingPlanJson.put("titre", tp.getProperty("titre"));
//				trainingPlanJson.put("description", tp.getProperty("description"));
//				
//				// Requete
//				Filter trainingPlanFilter = new FilterPredicate("trainingPlanId", FilterOperator.EQUAL, tp.getKey().getId());
//				Query q = new Query("Exercice").setFilter(trainingPlanFilter);
//				PreparedQuery pq = datastore.prepare(q);
//				
//				for (Entity exercice : pq.asIterable()) {
//					// Récupération des attributs de l'exercice
//					String titre = (String) exercice.getProperty("titre");
//					String description = (String) exercice.getProperty("description");
//					int duree = Integer.parseInt(exercice.getProperty("duree")
//							.toString());
//					int repetitions = Integer.parseInt(exercice.getProperty(
//							"repetitions").toString());
//					Long trainingPlanId = Long.parseLong(exercice.getProperty(
//							"trainingPlanId").toString());
//					Long exerciceId = exercice.getKey().getId();
//
//					// On construit l'objet JSON de l'exercice
//					JSONObject exerciceJson = new JSONObject();
//
//					// On set les attributs de l'objet
//					exerciceJson.put("titre", titre);
//					exerciceJson.put("description", description);
//					exerciceJson.put("duree", duree);
//					exerciceJson.put("repetitions", repetitions);
//					exerciceJson.put("trainingPlanId", trainingPlanId);
//					exerciceJson.put("exerciceId", exerciceId);
//
//					// On ajoute l'exercice au tableau de résultats
//					exercices.add(exerciceJson);
//				}
//				JSONArray exercicesJson = new JSONArray(exercices);
//				trainingPlanJson.put("exercices", exercicesJson);
//				details.put("trainingPlan", trainingPlanJson);
//			}
//		} catch (EntityNotFoundException | JSONException e) {
//			e.printStackTrace();
//		}
		return details;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("action");

		if (action.equals("add")) {
			this.addTrainingPlan(req);
		} else if (action.equals("search")) {

		}
	}

	private void addTrainingPlan(HttpServletRequest req) {

		// Récupération de l'objet JSON
		System.out.println("--");
		String jsonString = req.getParameter("trainingPlan");
		System.out.println(jsonString);
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
