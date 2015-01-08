package jtrain;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

/**
 * @author nicolas
 *
 * Servlet to manage login with openid
 *
 * http	/fixtures
 *
 */
@SuppressWarnings("serial")
public class JFixturesServlet extends HttpServlet {
	
	private DatastoreService datastore;
	
	public JFixturesServlet() {
		this.datastore = DatastoreServiceFactory.getDatastoreService();
	}
		
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		this.populateData();
	}
	
	public void populateData() {
		String domaineKind = "Domaine";
		String exerciceKind = "Exercice";
		String trainingPlanKind = "TrainingPlan";
		
		// Suppression des donnees precedentes
		this.deleteAllObjects(domaineKind);
		this.deleteAllObjects(exerciceKind);
		this.deleteAllObjects(trainingPlanKind);
		
		String[] domaines = {"Run", "Fitness", "Swimming", "Tennis", "Box", "Soccer", "Rugby", "Ping Pong", "Volley Ball", "Baseball", "Drink", "Basketball"};
		String[][] exercices = {
				{"Exercice 1", "Premier exercice", "1", "2"},
				{"Exercice 2", "Second exercice", "2", "3"},
				{"Exercice 3", "Troisieme exercice", "3", "4"},
				{"Exercice 4", "Quatrieme exercice", "4", "5"}
		};
		String[][] trainingPlans = {
				{"Training plan 1", "Premier training plan"},
				{"Training plan 2", "Second training plan"},
				{"Training plan 3", "Troisieme training plan"}				
		};
		
		for (String nom : domaines) {
			// Insertion du domaine
			Entity d = new Entity("Domaine");
			d.setProperty("nom", nom);
			datastore.put(d);
			
			// On récupère l'id du domaine inséré
			long domaineId = (d.getKey().getId());
			
			// Insertion training plans
			for (String[] trainingPlan : trainingPlans) {
				Entity e = new Entity(trainingPlanKind);
				e.setProperty("titre", trainingPlan[0].toString());
				e.setProperty("description", trainingPlan[1].toString());
				e.setProperty("domaineId", domaineId);
				datastore.put(e);
				
				long trainingPlanId = (e.getKey().getId());
				
				// Insertion exercices
				for (String[] exercice : exercices) {
					Entity f = new Entity(exerciceKind);
					f.setProperty("titre", exercice[0].toString());
					f.setProperty("description", exercice[1].toString());
					f.setProperty("duree", Integer.parseInt(exercice[2].toString()));
					f.setProperty("repetitions", Integer.parseInt(exercice[3].toString()));
					f.setProperty("trainingPlanId", trainingPlanId);

					datastore.put(f);
				}	
			}
		}
	}
	
	public void deleteAllObjects(String kind){
		  final DatastoreService dss=DatastoreServiceFactory.getDatastoreService();
		  final Query query=new Query(kind);
		  query.setKeysOnly();
		  final ArrayList<Key> keys=new ArrayList<Key>();
		  for (  final Entity entity : dss.prepare(query).asIterable(FetchOptions.Builder.withLimit(100000))) {
		    keys.add(entity.getKey());
		  }
		  dss.delete(keys);
		}
}
