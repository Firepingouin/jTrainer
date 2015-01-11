package jtrain;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * @author mlazzje
 *
 *         Servlet to manage search of : - training plan - training exercises -
 *         load training plan and exercises from a domain
 */
@SuppressWarnings("serial")
public class JSearchServlet extends HttpServlet {
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
		String requestType = req.getParameter("type");

		if (requestType != null) {
			List<JSONObject> domaines = new ArrayList<JSONObject>();
			if (requestType.equals("domaine")) {
				DatastoreService ds = DatastoreServiceFactory
						.getDatastoreService();
				Query q = new Query("Domaine").addSort("nom");

				PreparedQuery pq = ds.prepare(q);
				for (Entity result : pq.asIterable()) {
					JSONObject domaine = new JSONObject();
					try {
						domaine.put("id", result.getKey().getId());
						domaine.put("nom", (String) result.getProperty("nom"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					domaines.add(domaine);
				}
				resp.setContentType("application/json");
				PrintWriter out = resp.getWriter();
				JSONArray jsonObject = new JSONArray(domaines);
				out.print(jsonObject.toString());
				out.flush();
			} else if (requestType.equals("search")) {
				String domaineId = req.getParameter("domainId");
				String searchKeyword = req.getParameter("searchKeyword");

				try {
					JSONObject results = new JSONObject();

					if (domaineId != null) {
						results.putOpt("searchResults",
								this.getAllForDomaine(domaineId));
					} else if (searchKeyword != null) {
						results.putOpt("searchResults",
								this.search(searchKeyword));
					}

					JSONArray news = new JSONArray();
					try {
						news = this.fetchNews();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					results.put("news", news);

					// On retourne un objet JSON contenant les résultats de la
					// recherche
					resp.setContentType("application/json");
					PrintWriter out = resp.getWriter();
					out.print(results.toString());
					out.flush();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private JSONArray fetchNews() throws Exception {
		// Récupération des news à partir de l'url donnée
		JSONArray news = new JSONArray();
		URL url = new URL(
				"http://www.runnersworld.com/taxonomy/term/740/1/feed");
		// Parsing du dom
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputStream in = new BufferedInputStream(url.openStream());
		Document doc = dBuilder.parse(in);
		NodeList nl = doc.getElementsByTagName("item");
		Node attr;
		// Ajout du titre et de la description de la news
		for (int i = 0; i < nl.getLength(); i++) {
			NodeList nl2 = nl.item(i).getChildNodes();
			JSONObject currentNews = new JSONObject();
			for (int i2 = 0; i2 < nl2.getLength(); i2++) {
				attr = nl2.item(i2);
				if (attr.getNodeName().equals("title")) {
					currentNews.put("titre", attr.getTextContent());
				} else if (attr.getNodeName().equals("description")) {
					currentNews.put("description", attr.getTextContent());
				}
			}
			news.put(currentNews);
		}
		return news;
	}

	private JSONObject search(String searchKeyword) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		List<JSONObject> trainingPlans = new ArrayList<JSONObject>();
		List<JSONObject> exercices = new ArrayList<JSONObject>();
		JSONObject results = new JSONObject();

		// On filtre sur le domaine
		Filter titreFilter = new FilterPredicate("titre", FilterOperator.EQUAL,
				searchKeyword.toString());

		// Requete sur les training plans, puis les exercices
		Query q = new Query("TrainingPlan").setFilter(titreFilter);
		PreparedQuery pq = datastore.prepare(q);

		// Pour chaque training plan correspondant
		for (Entity trainingPlan : pq.asIterable()) {
			// Récupération des attributs du training plan
			String titre = (String) trainingPlan.getProperty("titre");
			String description = (String) trainingPlan
					.getProperty("description");
			Long trainingPlanId = trainingPlan.getKey().getId();
			Long domaineId = Long.parseLong(trainingPlan.getProperty(
					"domaineId").toString());

			try {
				// On construit l'objet JSON du training plan
				JSONObject trainingPlanJson = new JSONObject();

				// On set les attributs de l'objet
				trainingPlanJson.put("titre", titre);
				trainingPlanJson.put("description", description);
				trainingPlanJson.put("domaineId", domaineId);
				int dureeTotaleTrainingPlan = 0;

				// On requête les exercices pour obtenir la durée totale du
				// training plan
				Filter exerciceFilter = new FilterPredicate("trainingPlanId",
						FilterOperator.EQUAL, trainingPlanId);
				Query totalTimeQuery = new Query("Exercice")
						.setFilter(exerciceFilter);
				PreparedQuery ptotalTimeQuery = datastore
						.prepare(totalTimeQuery);

				// Pour chaque exercice trouvé on somme la durée à la durée
				// totale
				for (Entity exercice : ptotalTimeQuery.asIterable()) {
					// On somme la durée
					long duree = (long) exercice.getProperty("duree");
					dureeTotaleTrainingPlan += duree;
				}

				// On ajoute la durée totale au training plan
				trainingPlanJson.put("duree", dureeTotaleTrainingPlan);

				// On ajoute le training plan au tableau de résultats
				trainingPlans.add(trainingPlanJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// On requête sur les exercices
		q = new Query("Exercice").setFilter(titreFilter);
		pq = datastore.prepare(q);

		// Pour chaque exercice correspondant
		for (Entity exercice : pq.asIterable()) {
			// Récupération des attributs du training plan
			String titre = (String) exercice.getProperty("titre");
			String description = (String) exercice.getProperty("description");
			int duree = Integer.parseInt(exercice.getProperty("duree")
					.toString());
			int repetitions = Integer.parseInt(exercice.getProperty(
					"repetitions").toString());
			Long trainingPlanId = Long.parseLong(exercice.getProperty(
					"trainingPlanId").toString());
			Long exerciceId = exercice.getKey().getId();

			try {
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
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// On construit des Array JSON a partir des Array construits
		JSONArray trainingPlansArray = new JSONArray(trainingPlans);
		JSONArray exercicesArray = new JSONArray(exercices);

		// On insère les JSONArray dans l'objet résultat JSON
		try {
			results.put("trainingPlans", trainingPlansArray);
			results.put("exercices", exercicesArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// On retourne les résultats trouvés en Objet Json
		return results;
	}

	private JSONObject getAllForDomaine(String domaineId) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		List<JSONObject> trainingPlans = new ArrayList<JSONObject>();
		List<JSONObject> exercices = new ArrayList<JSONObject>();
		JSONObject results = new JSONObject();

		// On filtre sur le domaine
		Filter trainingPlanFilter = new FilterPredicate("domaineId",
				FilterOperator.EQUAL, Long.parseLong(domaineId.toString()));

		// Requete sur les training plans, puis les exercices
		Query q = new Query("TrainingPlan").setFilter(trainingPlanFilter);
		PreparedQuery pq = datastore.prepare(q);

		// Pour chaque training plan correspondant
		for (Entity trainingPlan : pq.asIterable()) {
			// Récupération des attributs du training plan
			String titre = (String) trainingPlan.getProperty("titre");
			String description = (String) trainingPlan
					.getProperty("description");
			Long trainingPlanId = trainingPlan.getKey().getId();

			try {
				// On construit l'objet JSON du training plan
				JSONObject trainingPlanJson = new JSONObject();

				// On set les attributs de l'objet
				trainingPlanJson.put("titre", titre);
				trainingPlanJson.put("description", description);
				trainingPlanJson.put("domaineId", domaineId);
				int dureeTotaleTrainingPlan = 0;

				// On requête les exercices pour obtenir la durée totale du
				// training plan
				Filter exerciceFilter = new FilterPredicate("trainingPlanId",
						FilterOperator.EQUAL, trainingPlanId);
				Query totalTimeQuery = new Query("Exercice")
						.setFilter(exerciceFilter);
				PreparedQuery ptotalTimeQuery = datastore
						.prepare(totalTimeQuery);

				// Pour chaque exercice trouvé on somme la durée à la durée
				// totale, et on construit un objet JSON
				for (Entity exercice : ptotalTimeQuery.asIterable()) {

					// On somme la durée
					long duree = (long) exercice.getProperty("duree");
					dureeTotaleTrainingPlan += duree;

					// On récupère les attributs de l'exercice
					String titreExercice = (String) exercice
							.getProperty("titre");
					String descriptionExercice = (String) trainingPlan
							.getProperty("description");
					long repetitions = (long) exercice
							.getProperty("repetitions");

					// On construit l'objet JSON de l'exercice
					JSONObject exerciceJson = new JSONObject();
					exerciceJson.put("titre", titreExercice);
					exerciceJson.put("description", descriptionExercice);
					exerciceJson.put("duree", duree);
					exerciceJson.put("repetitions", repetitions);
					exerciceJson.put("trainingPlanId", trainingPlanId);

					// On ajoute l'exercice au tableau de résultats
					exercices.add(exerciceJson);
				}

				// On ajoute la durée totale au training plan
				trainingPlanJson.put("duree", dureeTotaleTrainingPlan);

				// On ajoute le training plan au tableau de résultats
				trainingPlans.add(trainingPlanJson);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// On construit des Array JSON a partir des Array construits
		JSONArray trainingPlansArray = new JSONArray(trainingPlans);
		JSONArray exercicesArray = new JSONArray(exercices);

		// On insère les JSONArray dans l'objet résultat JSON
		try {
			results.put("trainingPlans", trainingPlansArray);
			results.put("exercices", exercicesArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// On retourne les résultats trouvés en Objet Json
		return results;
	}
}
