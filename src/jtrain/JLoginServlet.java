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
				
		Boolean logged = new Boolean(false);
		/*HashMap  = new HashMap<String, String>();*/
		
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser(); // or req.getUserPrincipal()
        Set<String> attributes = new HashSet();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        if (user != null) {
            out.println("Hello <i>" + user.getNickname() + "</i>!");
            out.println("[<a href=\""
                    + userService.createLogoutURL(req.getRequestURI())
                    + "\">sign out</a>]");
            logged = true;
        } else {
            out.println("Hello world! Sign in at: ");
            for (String providerName : openIdProviders.keySet()) {
                String providerUrl = openIdProviders.get(providerName);
                String loginUrl = userService.createLoginURL(req
                        .getRequestURI(), null, providerUrl, attributes);
                out.println("[<a href=\"" + loginUrl + "\">" + providerName + "</a>] ");
            }
            logged=false;
        }
        
        /*// On renvoit l'objet json
 		resp.setContentType("application/json");      
 		PrintWriter res = resp.getWriter();
 		String jsonObject = "{ \"logged\": \""+logged+"\"}";
 		res.print(jsonObject);
 		res.flush();*/
		
	}
}
