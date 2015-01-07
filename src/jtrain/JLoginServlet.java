package jtrain;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.*;

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
		String openid = req.getParameter("openid");
		String email = req.getParameter("email");
				
				/*
				 * UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser(); // or req.getUserPrincipal()
        Set<String> attributes = new HashSet();

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        if (user != null) {
            out.println("Hello <i>" + user.getNickname() + "</i>!");
            out.println("[<a href=\""
                    + userService.createLogoutURL(req.getRequestURI())
                    + "\">sign out</a>]");
        } else {
            out.println("Hello world! Sign in at: ");
            for (String providerName : openIdProviders.keySet()) {
                String providerUrl = openIdProviders.get(providerName);
                String loginUrl = userService.createLoginURL(req
                        .getRequestURI(), null, providerUrl, attributes);
                out.println("[<a href=\"" + loginUrl + "\">" + providerName + "</a>] ");
            }
        }
*/
		
	}
}
