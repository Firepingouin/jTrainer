package jtrain;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
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
						domaine.put("key", result.getKey().toString());
						domaine.put("nom", (String) result.getProperty("nom"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					domaines.add(domaine);
				}
			}
			resp.setContentType("application/json");      
			PrintWriter out = resp.getWriter();
			JSONArray jsonObject = new JSONArray(domaines);
			out.print(jsonObject.toString());
			out.flush();
		}
	}
}
