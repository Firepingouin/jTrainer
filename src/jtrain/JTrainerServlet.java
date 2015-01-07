package jtrain;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

@SuppressWarnings("serial")
public class JTrainerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// On met le message dans le datastore
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity messageEntity = new Entity("Message");
		messageEntity.setProperty("title", "Jtrainer is your fitness partner !");
		ds.put(messageEntity);
		
		// On essaie de récupérer le message du cache
		String key = "mod";
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
		String message = (String)cache.get(key);
		
		// Si le message n'est pas en cache
		if(message == null) {
			// On cherche le message dans le datastore et on le met dans le cache
			Key mKey = messageEntity.getKey();
			try {
				message = (String)ds.get(mKey).getProperty("message");
				cache.put(key, message);
			} catch (EntityNotFoundException e) {
				e.printStackTrace();
				message ="";
			}
		}
		
		// On renvoit l'objet json
		resp.setContentType("application/json");      
		PrintWriter out = resp.getWriter();
		String jsonObject = "{ mod: \""+message+"\"}";
		out.print(jsonObject);
		out.flush();
	}
}
