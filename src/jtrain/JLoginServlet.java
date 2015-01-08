package jtrain;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * @author mlazzje
 *
 * Servlet to manage login with openid
 *
 * http	/login
 *
 */
@SuppressWarnings("serial")
public class JLoginServlet extends HttpServlet {
	
	private static final Map<String, String> openIdProviders;
    static {
        openIdProviders = new HashMap<String, String>();
        openIdProviders.put("google", "https://www.google.com/accounts/o8/id");
        openIdProviders.put("yahoo", "yahoo.com");
        openIdProviders.put("open", "myopenid.com");
    }
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
				
		// TODO store in memcache
		
		String callbackUrl = req.getParameter("callback");
		
		if(callbackUrl==null) {
			callbackUrl = req.getRequestURI();
		}
		
		Boolean logged = new Boolean(false);
		HashMap<String, String> domains = new HashMap<String, String>();
		JSONObject domainsJSON = new JSONObject();
		String logoutUrl = "";
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser(); // or req.getUserPrincipal()
        Set<String> attributes = new HashSet();

        /*resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();*/

        if (user != null) {
            //out.println("Hello <i>" + user.getNickname() + "</i>!");
            //out.println("[<a href=\""
                    //+ userService.createLogoutURL(req.getRequestURI())
                    //+ "\">sign out</a>]");
        	logoutUrl = userService.createLogoutURL(req.getRequestURI());
            logged = true;
        } else {
            //out.println("Hello world! Sign in at: ");
            for (String providerName : openIdProviders.keySet()) {
                String providerUrl = openIdProviders.get(providerName);
                String loginUrl = userService.createLoginURL(callbackUrl, null, providerUrl, attributes);
                //out.println("[<a href=\"" + loginUrl + "\">" + providerName + "</a>] ");
                domains.put(providerName, loginUrl);
            }
            logged=false;
            domainsJSON = new JSONObject(domains);
        }
        
        // On renvoit l'objet json
 		resp.setContentType("application/json");      
 		PrintWriter res = resp.getWriter();
 		String jsonObject = "{ \"logged\": \""+logged+"\"";
 		if(logged) {
 			jsonObject = jsonObject + ", \"user\" : { \"mail\" : \""+user.getEmail()+"\", \"nickname\" : \""+user.getNickname()+"\"}, \"logout\" : \""+logoutUrl+"\"";
 		} else {
 			jsonObject = jsonObject + ", \"domains\" : "+domainsJSON;
 		}
 		jsonObject = jsonObject + "}";
 		res.print(jsonObject);
 		res.flush();
		
	}
}
