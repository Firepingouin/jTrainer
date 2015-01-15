package jtrain;

import java.io.IOException;
import java.util.Enumeration;

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
		
		// TEST
		System.out.println("--");
		System.out.println("/addTrainingPlan");
		Enumeration params = req.getParameterNames(); 
		while(params.hasMoreElements()){
		 String paramName = (String)params.nextElement();
		 System.out.println("Attribute Name - "+paramName+", Value - "+req.getParameter(paramName));
		}
		// FIN TEST
		JSONObject trainingPlan = null;
		try {
			trainingPlan = new JSONObject(req.getParameter("trainingPlan"));
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if(trainingPlan==null) {
			return;
		}

		// Récupération des paramètres du Training Plan
		String titre;
		String description;
		String domaineId;
		try {
			domaineId = trainingPlan.getString("domaineId");
			titre = trainingPlan.getString("titre");
			description = trainingPlan.getString("description");
			
			// Ajout du training plan
			Entity t = new Entity("TrainingPlan");
			t.setProperty("titre", titre);
			t.setProperty("description", description);
			t.setProperty("domaineId", domaineId);
			datastore.put(t);

			long trainingPlanId = (t.getKey().getId());

			// Récupération des exercices
			JSONArray exercices = null;
			try {
				exercices = new JSONArray(trainingPlan.getString("exercices"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (exercices != null) {
				for (int i = 0; i < exercices.length(); i++) {
					// Récupération des paramètres de l'exercice
					JSONObject exercice;
					try {
						exercice = exercices.getJSONObject(i);
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
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}	
}
