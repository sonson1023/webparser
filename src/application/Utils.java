package application;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utils {

	private static final Logger logger = LogManager.getLogger(Utils.class);

	public static void ShowAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("확인 필요");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public static void ShowAlert(String header, String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("확인 필요");
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}
	
	public static JSONObject readConfig() {
		JSONObject json = null ;
		try { 
			String fileName ="tag.json";
			ClassLoader classLoader = new Utils().getClass().getClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());
			String contents = new String(Files.readAllBytes(file.toPath()));
			json = new JSONObject(contents);
			logger.info(json.toString());
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(e.getLocalizedMessage());
			
		}
		return json;
		
	}
	
	public static List<String> readMaker() {
		List<String> list = new ArrayList();
		JSONObject obj = readConfig();
		JSONArray array = (JSONArray) obj.get("maker");
		for(int i=0; i< array.length(); i++) {
			list.add(array.getString(i));
		} 
		return list; 
	}
}
