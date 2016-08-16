

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteResult;

/**
 * Servlet implementation class rxSubmit
 */
@WebServlet("/rxSubmit")
public class rxSubmit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public rxSubmit() {
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
		//doGet(request, response);
		System.out.println("I am here in doPost!!!!!");
		
		
		String testName=request.getParameter("Name");
		String specialInstructions = request.getParameter("Special_Instructions");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dateTimeStamp = dateFormat.format(date); //2014/08/06 15:59:48
		BasicDBObject clinicalTestRow = new BasicDBObject();
		
		//HardCoded for now.
//		String patientName = "Shweta Parihar";
		String patientID = "1234567890.00";
//		String physicianName = "Dr. Anil Jain";
//		String physicianClinicName = "Dr Lal Multi Speciality Clinic";
			
//		System.out.println("1. patientID : "+patientID);
//		System.out.println("2. patientName : "+patientName);
//		System.out.println("3. physicianName : "+physicianName);
//		System.out.println("4. physicianClinicName : "+physicianClinicName);
//		System.out.println("5. testName : "+testName);
//		System.out.println("6. specialInstructions : "+specialInstructions);
//		System.out.println("7. dateTimeStamp : "+dateTimeStamp);		
		
		clinicalTestRow.put("patientID",patientID);
		clinicalTestRow.put("patientName",request.getParameter("patientName"));
		clinicalTestRow.put("username",request.getParameter("username"));
		clinicalTestRow.put("physicianName",request.getParameter("physicianName"));
		clinicalTestRow.put("physicianClinicName",request.getParameter("physicianClinicName"));
		
		List<BasicDBObject> tsts = new ArrayList<>();
		BasicDBObject tn = new BasicDBObject();
		tn.put("Name", testName);
		tn.put("Special_Instructions", specialInstructions);
		tsts.add(tn);
		
		clinicalTestRow.put("Tests", tsts);

		clinicalTestRow.put("dateTimeStamp",dateTimeStamp);		
		
		MongoClientURI uri = new MongoClientURI("< TODO add_url_here >");
		MongoClient client = new MongoClient(uri);

		DB db = client.getDB(uri.getDatabase());
		DBCollection users = db.getCollection("clinical_test_table");
		WriteResult result = users.insert(clinicalTestRow);
		

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Access-Control-Max-Age", "86400");

		response.getWriter().write(result.toString());
	}

}
