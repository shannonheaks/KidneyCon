

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
 * Servlet implementation class GetVisitForm
 */
@WebServlet("/GetVisitForm")
public class GetVisitForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
    JSONObject info;
    JSONArray jArr, info2;
    int count;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetVisitForm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		count = 0;
		info = new JSONObject();
		info2 = new JSONArray();
		jArr = new JSONArray();
		MongoClientURI uri = new MongoClientURI("< TODO add_url_here >");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase("kidney_db");
		
		FindIterable<Document> iterable = db.getCollection("patient_forms").find(eq("username", request.getParameter("username")));		
		
		iterable.forEach(new Block<Document>() {
			
		    @Override
		    public void apply(final Document document) {
		        System.out.println(document);
		        buildJSON(document);

		    } 
		});
		mongoClient.close();
		
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		
		response.setContentType("application/json");     
		PrintWriter out = response.getWriter();  
		out.print(info2);
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
		count += 1;
		System.out.println(count);
		try {
			info.put("labNum", "Lab " + String.valueOf(count));
			info.put("timestamp", document.get("timestamp"));
			info.put("username", document.get("username"));
			info.put("complaints", document.get("complaints"));
			info.put("allergies", document.get("allergies"));
			info.put("meds", document.get("meds"));
			info.put("surgeries", document.get("surgeries"));
			info.put("drugs", document.get("drugs"));
			info.put("relatives", document.get("relatives"));
			info2.put(info);
			info = new JSONObject();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
