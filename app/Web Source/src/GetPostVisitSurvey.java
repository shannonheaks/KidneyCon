

import static com.mongodb.client.model.Filters.eq;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class GetPostVisitSurvey
 */
@WebServlet("/GetPostVisitSurvey")
public class GetPostVisitSurvey extends HttpServlet {
	private static final long serialVersionUID = 1L;
    JSONObject info;
    JSONArray jArr;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPostVisitSurvey() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		info = new JSONObject();
		jArr = new JSONArray();
		MongoClientURI uri = new MongoClientURI("< TODO add_url_here >");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase("kidney_db");
		
		FindIterable<Document> iterable = db.getCollection("follow_up_survey").find(eq("username", request.getParameter("username")));		
		
		iterable.forEach(new Block<Document>() {
			
		    @Override
		    public void apply(final Document document) {
		        System.out.println(document);
		        buildJSON(document);

		    } 
		});
		mongoClient.close();
		
		try {
			info.put("surveyReturn", "success");
			info.put("surveys", jArr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public void buildJSON(Document document){
		try {
			JSONObject jo = new JSONObject();
			jo.put("q1", document.get("q1"));
			jo.put("q2", document.get("q2"));
			jo.put("q3", document.get("q3"));
			jo.put("q4", document.get("q4"));
			jo.put("q5", document.get("q5"));
			jo.put("q6", document.get("q6"));
			jo.put("q7", document.get("q7"));
			jo.put("q8", document.get("q8"));
			jo.put("q9", document.get("q9"));
			jo.put("q10", document.get("q10"));
			jo.put("timestamp", document.get("timestamp"));
			jArr.put(jo);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
