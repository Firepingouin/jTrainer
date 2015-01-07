package jtrain;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
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
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("Domaine").setKeysOnly();
		PreparedQuery pq = datastore.prepare(q);
		this.deleteAllObjects("Domaine");
		
		// On insère les domaines
		String[] domaines = {"Run", "Fitness", "Swimming", "Tennis", "Box", "Soccer", "Rugby", "Ping Pong", "Volley Ball", "Baseball", "Drink", "Basketball"};
		for (String nom : domaines) {
			Entity d = new Entity("Domaine");
			d.setProperty("nom", nom);
			datastore.put(d);
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
