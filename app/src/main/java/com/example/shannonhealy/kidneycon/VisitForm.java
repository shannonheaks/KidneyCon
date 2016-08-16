package com.example.shannonhealy.kidneycon;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * Servlet implementation class VisitForm
 */
@WebServlet("/VisitForm")
public class VisitForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VisitForm() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		List<String> complaints = Arrays.asList("firstCom", "secondCom", "thirdCom");
		List<String> allergies = Arrays.asList("allergy1", "allergy2", "allergy3", "foodAllergy");
		List<String> meds = Arrays.asList("firstMed", "secondMed", "thirdMed");
		List<String> surgeries =Arrays.asList("firstSur", "secondSur", "thirdSur");
		List<String> drugs = Arrays.asList("smoke", "drink");
		List<String> relatives = Arrays.asList("cancer", "tuberculosis", "diabetes");
		
		String full = request.getParameter("objects");
		String sub = full.substring(1,  full.length()-1);
		List<String> items = Arrays.asList(sub.split("\\s*,\\s*"));
		System.out.println(items);	
		
		BasicDBObject form = new BasicDBObject();
		List<String> comp = new ArrayList<>();		
		List<String> all =  new ArrayList<>();		
		List<String> med =  new ArrayList<>();		
		List<String> surg = new ArrayList<>();		
		//List<BasicDBObject> drug = new ArrayList<>();		
		//List<BasicDBObject> rel =  new ArrayList<>();
		BasicDBObject temp1 = new BasicDBObject();
		BasicDBObject temp2 = new BasicDBObject();

		for(int i = 0; i < items.size(); i++){
			System.out.println(items.get(i));
			if(complaints.contains(items.get(i))){
				comp.add(request.getParameter(items.get(i)));				
			}
			else if (allergies.contains(items.get(i))){
				all.add(request.getParameter(items.get(i)));
			}
			else if(meds.contains(items.get(i))){
				med.add(request.getParameter(items.get(i)));
			}
			else if(surgeries.contains(items.get(i))){
				surg.add(request.getParameter(items.get(i)));
			}
			else if(drugs.contains(items.get(i))){
					temp1.put(items.get(i), request.getParameter(items.get(i)));
					//drug.add(temp);

			}
			else if(relatives.contains(items.get(i))){
					temp2.put(items.get(i), request.getParameter(items.get(i)));
					//rel.add(temp);

			}
			else{
				System.out.println("error in post params");
			}			
		}
		
		
			form.put("complaints", comp);
			form.put("allergies", all);
			form.put("meds", med);
			form.put("surgeries", surg);
			form.put("drugs", temp1);
			form.put("relatives", temp2);
			form.put("username", request.getParameter("username"));
			form.put("status", "success");
			
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dateTimeStamp = dateFormat.format(date); //2014/08/06 15:59:48
		
		
		MongoClientURI uri = new MongoClientURI("< TODO add_url_here >");
		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase db = mongoClient.getDatabase("kidney_db");
		
		db.getCollection("patient_forms").insertOne(new Document()
				.append("username", request.getParameter("username"))
				.append("timestamp", dateTimeStamp)
				.append("complaints", comp)
				.append("allergies", all)
				.append("meds", med)
				.append("surgeries", surg)
				.append("drugs", temp1)
				.append("relatives", temp2));
		
		mongoClient.close();
		
		
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	    response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		
		response.setContentType("application/json");     
		PrintWriter out = response.getWriter();  
		out.print(form);
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
