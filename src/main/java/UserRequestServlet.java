import java.io.IOException;
import java.net.URI;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;

import mongoclient.MongoClientUtil;



@WebServlet("/request")
public class UserRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserRequestServlet.class.getClass().getName());

	public UserRequestServlet() {
		super();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FileHandler fileHandler = new FileHandler("C:\\logs\\UserRequest.log");
		fileHandler.setLevel(Level.INFO);
		logger.addHandler(fileHandler);
		logger.setLevel(Level.INFO);
		fileHandler.setFormatter(new java.util.logging.SimpleFormatter());

		HttpSession session = request.getSession(true);

		// Modify the session cookie domain
		String domain = "oauth2server"; // Change this to your desired domain
		Cookie cookie = new Cookie("JSESSIONID", session.getId());
		cookie.setDomain(domain);
		cookie.setPath("/");
		response.addCookie(cookie);

		String sessionid = request.getSession(true).getId();	
		logger.log(Level.INFO, "session_id" + sessionid);

		String user = request.getParameter("user");

		request.getRequestDispatcher("/UserAuthorizationHandler").include(request, response);
		AuthorizationRequest authRequest = (AuthorizationRequest)request.getAttribute("request_uri");
		response.sendRedirect(authRequest.toURI().toString());               //redirect the user browser to the AuthEndPoint
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}