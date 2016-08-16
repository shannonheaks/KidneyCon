

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
 * Servlet implementation class GetLabDate
 */
@WebServlet("/GetVitalsLog")
public class GetVitalsLog extends HttpServlet {
	private static final long serialVersionUID = 1L;
    JSONObject info;
    JSONArray jArr;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetVitalsLog() {
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
		
		FindIterable<Document> iterable = db.getCollection("vitals_log").find(eq("username", request.getParameter("username")));		
		
		iterable.forEach(new Block<Document>() {
			
		    @Override
		    public void apply(final Document document) {
		        System.out.println(document);
		        buildJSON(document);

		    } 
		});
		mongoClient.close();
		
		try {
			info.put("labReturn", "success");
			info.put("labs", jArr);
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
		JSONObject jo = new JSONObject();
		try {
			jo.put("BP", document.get("BP"));
			jo.put("BS", document.get("BS"));
			jo.put("TEMP", document.get("TEMP"));
			jo.put("FEELING", document.get("FEELING"));
			jo.put("status", document.get("status"));
			jo.put("timestamp", document.get("timestamp"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jArr.put(jo);
	}

}
