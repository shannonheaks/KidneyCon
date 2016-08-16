

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.*;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class LevelMonitor
 */
@WebServlet("/VitalsLog")
public class VitalsLog extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VitalsLog() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String BP = request.getParameter("BP");
		String BS = request.getParameter("BS");
		String TEMP = request.getParameter("TEMP");
		String FEELING = request.getParameter("FEELING");
		JSONObject info = new JSONObject();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dateTimeStamp = dateFormat.format(date); 
				
		if(isBloodPressureSafe(BP) == false || isBloodSugarSafe(BS) == false){
			try {
				info.put("status", "danger");
			} catch (JSONException e) {e.printStackTrace();}
		}
		else{
			try {
				info.put("status", "safe");
			} catch (JSONException e) {e.printStackTrace();}
		}		
		try {
			info.put("BP", BP);
			info.put("BS", BS);
		} catch (JSONException e) {e.printStackTrace();}
		
		MongoClientURI uri = new MongoClientURI("< TODO add_url_here >");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase("kidney_db");
		
		if(isBloodPressureSafe(BP) == false || isBloodSugarSafe(BS) == false){
			db.getCollection("vitals_log").insertOne(new Document()
					.append("username", request.getParameter("username"))
					.append("BP", request.getParameter("BP"))
					.append("BS", request.getParameter("BS"))
					.append("TEMP", request.getParameter("TEMP"))
					.append("FEELING", request.getParameter("FEELING"))
					.append("timestamp", dateTimeStamp)
					.append("status", "danger"));
		}else{
			db.getCollection("vitals_log").insertOne(new Document()
					.append("username", request.getParameter("username"))
					.append("BP", request.getParameter("BP"))
					.append("BS", request.getParameter("BS"))
					.append("TEMP", request.getParameter("TEMP"))
					.append("FEELING", request.getParameter("FEELING"))
					.append("timestamp", dateTimeStamp)
					.append("status", "safe"));
			
		}
		mongoClient.close();
				
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		response.setContentType("application/json");     
		PrintWriter out1 = response.getWriter();  
		out1.print(info);
		out1.flush();

		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub	
		doGet(request, response);
	}
	
	boolean isBloodPressureSafe(String BP){		
		String[] splitBP = BP.split("/");		
		int systolic = Integer.parseInt(splitBP[0]);
		int diastolic = Integer.parseInt(splitBP[1]);		
		return !(systolic < 90 || diastolic < 60 || systolic > 140 || diastolic > 90 );
	}
	
	boolean isBloodSugarSafe(String BS){
		int bloodSugar = Integer.parseInt(BS);
		return !(bloodSugar < 70 || bloodSugar > 300);
	}

}
