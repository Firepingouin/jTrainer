package jtrain;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class JTrainingTaskServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		try {
			JSONObject json = new JSONObject(req.getParameter("trainingPlan")).getJSONObject("trainingPlan");

			// Récupération des paramètres du Training Plan
			String titre = json.getString("titre");
			String description = json.getString("description");
			long domaineId = json.getLong("domaineId");

			if (titre == null || description == null)
				return;

			// Ajout du training plan
			Entity t = new Entity("TrainingPlan");
			t.setProperty("titre", titre);
			t.setProperty("description", description);
			t.setProperty("domaineId", domaineId);
			datastore.put(t);

			long trainingPlanId = (t.getKey().getId());

			// Récupération des exercices
			JSONArray exercices = json.getJSONArray("exercices");
			if (exercices != null) {
				for (int i = 0; i < exercices.length(); i++) {
					// Récupération des paramètres de l'exercice
					JSONObject exercice = exercices.getJSONObject(i);
					String titreExercice = exercice.getString("titre");
					String descriptionExercice = exercice
							.getString("description");
					int duree = exercice.getInt("duree");
					int repetitions = exercice.getInt("repetitions");

					if (titreExercice == null || descriptionExercice == null)
						return;

					// Ajout de l'exercice
					Entity e = new Entity("Exercice");
					e.setProperty("titre", titreExercice);
					e.setProperty("description", descriptionExercice);
					e.setProperty("duree", duree);
					e.setProperty("repetitions", repetitions);
					e.setProperty("trainingPlanId", trainingPlanId);

					datastore.put(e);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
