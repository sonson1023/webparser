package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import application.item.ItemInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; 
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.*; 


public class SampleController implements Initializable {

	@FXML private Button goBtn; 
	@FXML private Button btnLoadCfg;
	@FXML private WebView webView;
	@FXML private TextField textUrl;
	@FXML private TableView<ItemInfo> tableView; 
	@FXML private TableColumn<ItemInfo, String> index;
    @FXML private TableColumn<ItemInfo, String> code;

    
    @FXML private TableColumn<ItemInfo, String> col_index;
    @FXML private TableColumn<ItemInfo, String> col_code;
	@FXML private TableColumn<ItemInfo, String> col_name;
    @FXML private TableColumn<ItemInfo, String> col_price;
    @FXML private TableColumn<ItemInfo, String> col_img1;
    @FXML private TableColumn<ItemInfo, String> col_img2;
    @FXML private TableColumn<ItemInfo, String> col_img3;
	
	@FXML private ComboBox<String> cbMaker;

	private JSONParser jsonParser = new JSONParser();
	private static final Logger logger = LogManager.getLogger(SampleController.class);
	private WebEngine webEngine;
	private ObservableList<ItemInfo> tableData = FXCollections.observableArrayList();
	
	static {
		
		
	}
	
	
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
		
		 //firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
	     //   lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		col_index.setCellValueFactory(x -> x.getValue().getIndex());
		col_code.setCellValueFactory(x -> x.getValue().getCode());
		col_name.setCellValueFactory(x -> x.getValue().getName());
		col_price.setCellValueFactory( x -> x.getValue().getPrice());
		col_img1.setCellValueFactory(x -> x.getValue().getImg1());
		col_img2.setCellValueFactory(x -> x.getValue().getImg2());
		col_img3.setCellValueFactory(x -> x.getValue().getImg3());
		
		
	}
	
	
	public void OnGoBtn(ActionEvent event) {
		if(textUrl.getText().length() > 0) {
			
			String url = textUrl.getText();
			webEngine.load(url);
		}else {
			Utils.ShowAlert("URL ÇÊ¿ä");
		}
	}

	public void OnLoadCfg(ActionEvent event) {
		//Utils.ShowAlert("filter");
		String html =  webEngine.executeScript("document.documentElement.outerHTML").toString();
		Document doc = Jsoup.parse(html);
		
		HashMap<String, ItemInfo> map = new HashMap();
		
		String json = "{\"gucci\":{\"item_code\":\" div.product-tiles-grid-item-detail > div.carousel.product-tiles-grid-item-carousel\",\"item_name\":\"div.product-tiles-grid-item-detail > div.product-tiles-grid-item-info > h2\",\"item_price\":\"div.product-tiles-grid-item-detail > div.product-tiles-grid-item-info > p.price > span.sale\"}}";
		JSONParser parser = new JSONParser();
		JSONObject jObj;
		
		try {
			jObj = (JSONObject)parser.parse(json);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		ArrayList<String> codeList = new ArrayList();
		ArrayList<String> nameList = new ArrayList();
		ArrayList<String> priceList = new ArrayList();
		
		Elements item_codes = doc.select("div.product-tiles-grid-item-detail > div.carousel.product-tiles-grid-item-carousel");
		logger.info("item_codes cnt = " + item_codes.size());
		for(Element ele : item_codes){
			
			//logger.info("ele : " + ele.text());
			codeList.add(ele.attributes().get("data-product-code"));
		}
		
		Elements item_names = doc.select("div.product-tiles-grid-item-detail > div.product-tiles-grid-item-info > h2");
		logger.info("item_name cnt = " + item_names.size());
		for(Element ele : item_names){
			//logger.info("ele : " + ele.text());
			nameList.add(ele.text());
		} 
		
		Elements item_prices = doc.select("div.product-tiles-grid-item-detail > div.product-tiles-grid-item-info > p.price > span.sale");
		logger.info("item_prices  cnt = " + item_prices .size());
		for(Element ele : item_prices ){
			//logger.info("ele : " + ele.text());
			priceList.add(ele.text());
		} 
		

		tableView.getItems().clear();
		
		ArrayList<ItemInfo> items = new ArrayList<ItemInfo>();
		for(int i=0; i< item_codes.size(); i++) {
			ItemInfo e = new ItemInfo(Integer.toString(i),codeList.get(i), nameList.get(i), priceList.get(i), "-", "-", "-");
			tableData.add(e);
		}
		
		tableView.setItems(tableData);
		tableView.refresh();
		
		 
		//tableView.getItems().addAll(tableData);
		

	}
	
	public void OnSelctMaker(ActionEvent evt) { 
		logger.info("click OnSelctMaker");
	}


	public void handleExit() {
		
		Platform.exit();
		System.exit(0);
	}
}
