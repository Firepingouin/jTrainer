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
		
		String idString = req.getParameter("id");
		
		if (!idString.equals("") && idString != null) {
			System.out.println("Get all details from training plan");
			resp.setContentType("application/json");
			PrintWriter out = resp.getWriter();
			JSONObject details = new JSONObject();
			
			Long id = Long.parseLong(idString);
			details = this.getDetails(id);
			
			out.print(details);
			out.flush();	
		}
	}

	private JSONObject getDetails(Long id) {
		JSONObject details = new JSONObject();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		// On filtre sur le domaine
		try {
				Filter idFilter = new FilterPredicate("id", FilterOperator.EQUAL, id);
				Query q = new Query("TrainingPlan").setFilter(idFilter);
				PreparedQuery pq = datastore.prepare(q);
				Entity trainingPlan = pq.asSingleEntity();
				
				if(trainingPlan != null) {
					JSONObject trainingPlanJson = new JSONObject();
					ArrayList<JSONObject> exercices = new ArrayList<JSONObject>();
					
					trainingPlanJson.put("titre", trainingPlan.getProperty("titre"));
					trainingPlanJson.put("description", trainingPlan.getProperty("description"));

					// Requete
					Filter trainingPlanFilter = new FilterPredicate("trainingPlanId", FilterOperator.EQUAL, id);
					Query q2 = new Query("Exercice").setFilter(trainingPlanFilter);
					PreparedQuery pq2 = datastore.prepare(q2);
					
					for (Entity exercice : pq2.asIterable()) {
						// Récupération des attributs de l'exercice
						String titre = (String) exercice.getProperty("titre");
						String description = (String) exercice.getProperty("description");
						int duree = Integer.parseInt(exercice.getProperty("duree")
								.toString());
						int repetitions = Integer.parseInt(exercice.getProperty(
								"repetitions").toString());
						Long trainingPlanId = Long.parseLong(exercice.getProperty(
								"trainingPlanId").toString());
						Long exerciceId = exercice.getKey().getId();

						// On construit l'objet JSON de l'exercice
						JSONObject exerciceJson = new JSONObject();

						// On set les attributs de l'objet
						exerciceJson.put("titre", titre);
						exerciceJson.put("description", description);
						exerciceJson.put("duree", duree);
						exerciceJson.put("repetitions", repetitions);
						exerciceJson.put("trainingPlanId", trainingPlanId);
						exerciceJson.put("exerciceId", exerciceId);

						// On ajoute l'exercice au tableau de résultats
						exercices.add(exerciceJson);	
					}
					JSONArray exercicesJson = new JSONArray(exercices);
					trainingPlanJson.put("exercices", exercicesJson);
					details.put("trainingPlan", trainingPlanJson);
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return details;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String action = req.getParameter("action");

		if (action.equals("add")) {
			this.addTrainingPlan(req);
		} else if (action.equals("search")) {
			Long id = Long.parseLong(req.getParameter("trainingPlanId"));
			if(id != null && id != 0) {
				this.getDetails(id);	
			}
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
