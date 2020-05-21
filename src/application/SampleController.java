package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.*;
import net.bytebuddy.asm.Advice.Exit;


public class SampleController implements Initializable {

	@FXML private Button goBtn; 
	@FXML private Button btnLoadCfg;
	@FXML private WebView webView;
	@FXML private TextField textUrl;
	@FXML private TableView dataView; 
	@FXML private ComboBox<String> cbMaker;
	

	private WebEngine webEngine;
	  
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		goBtn.setOnAction(event -> OnGoBtn(event));
		cbMaker.setOnAction(evt -> OnSelctMaker(evt));
		
		webEngine = webView.getEngine();
		ObservableList<String> options = 
			    FXCollections.observableArrayList(
			        "±¸Âî"
			    );
		cbMaker.setItems(options);
	}
	
	
	public void OnGoBtn(ActionEvent event) {
		if(textUrl.getText().length() > 0) {
			Utils.ShowAlert("OK");
			
			String url = textUrl.getText();
			webEngine.load(url);
		}else {
			Utils.ShowAlert("URL ÇÊ¿ä");
		}
	}

    WebDriver driver = new FirefoxDriver();
	
	public void OnLoadCfg(ActionEvent event) {
		//Utils.ShowAlert("filter");
		String html =  webEngine.executeScript("document.documentElement.outerHTML").toString();
		org.w3c.dom.Document xmlDoc = webEngine.getDocument(); 
	}
	
	public void OnSelctMaker(ActionEvent evt) {
		Utils.ShowAlert("filter");
	}


	public void handleExit() {
		
		Platform.exit();
		System.exit(0);
	}
}
