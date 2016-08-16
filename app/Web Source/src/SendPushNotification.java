

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class SendMessage
 */
@WebServlet("/SendPushNotification")
public class SendPushNotification extends HttpServlet {
	private static final long serialVersionUID = 1L;
    Boolean isVerified = false;
    HttpServletRequest request2;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendPushNotification() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request2 = request;
		isVerified = false;
		JSONObject info = new JSONObject();
		
		verifyLogin();
		try{
			if(isVerified){
				//response.getWriter().append("Served Success at: ").append(request.getContextPath());
				info.put("login", "success");
			}
			else{
				//response.getWriter().append("Served fail at: ").append(request.getContextPath());
				info.put("login", "failure");
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
		
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		response.setContentType("application/json");     
		PrintWriter out = response.getWriter();  
		out.print(info);
		out.flush();
		//doGet(request, response);
	}
	
	
	void verifyLogin(){
		
		MongoClientURI uri = new MongoClientURI("< TODO add_url_here >");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase("kidney_db");
		
		FindIterable<Document> iterable = db.getCollection("test_patients").find();		
		
		iterable.forEach(new Block<Document>() {
			
		    @Override
		    public void apply(final Document document) {
		        System.out.println(document);
		        try {
					isVerified(document);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    } 
		});
		mongoClient.close();
	}
	
	void isVerified(Document loginInfo) throws IOException{

		
		if(loginInfo.get("username").equals(request2.getParameter("username"))){
			System.out.println(loginInfo);
			System.out.println(request2.getParameter("username"));
			isVerified = true;
			String apiKey = "<INSERT GCM API KEY HERE>";
			int numOfRetries = 3;
			Sender sender = new Sender(apiKey);
			Message message = new Message.Builder()
			    .addData("message", "Hi " + request2.getParameter("patName")+ " this is a reminder for your upcoming appointment" +
			    " at " + request2.getParameter("time") +
			    " on " + request2.getParameter("message")+ ". We look forward to your visit! " +
			    request2.getParameter("extraMsg")) 
			    .build();
			Result result = sender.send(message, loginInfo.get("token").toString(), numOfRetries);
		}

		
	}
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Send Response
	    super.doOptions(request,  response);
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With");
	}

}
