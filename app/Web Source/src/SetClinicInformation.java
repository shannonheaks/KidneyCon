

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class SetClinicInformation
 */
@WebServlet("/SetClinicInformation")
public class SetClinicInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetClinicInformation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MongoClientURI uri = new MongoClientURI("< TODO add_url_here >");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase("kidney_db");
		MongoCollection<Document> collection = db.getCollection("clinic_info");
		collection.updateOne(eq("mainClinic", "true"), set("clinicName", request.getParameter("clinicName")));
		collection.updateOne(eq("mainClinic", "true"), set("clinicAddress", request.getParameter("clinicAddress")));
		collection.updateOne(eq("mainClinic", "true"), set("clinicPhone", request.getParameter("clinicPhone")));
		collection.updateOne(eq("mainClinic", "true"), set("clinicEmail", request.getParameter("clinicEmail")));
		collection.updateOne(eq("mainClinic", "true"), set("clinicHours", request.getParameter("clinicHours")));
		mongoClient.close();
		
		JSONObject info = new JSONObject();
		try {
			info.put("setClinic", "success");
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

}
