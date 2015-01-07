package jtrain;

import java.io.IOException;
import java.io.PrintWriter;

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
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		// Get the printwriter object from resp to write the required json object to the output stream      
		PrintWriter out = resp.getWriter();
		String jsonObject = "{ firstname: 'Nico', lastname: 'Pari' }";
		out.print(jsonObject);
		out.flush();
	}
}
