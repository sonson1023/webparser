package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import application.ItemParser.ItemSrc;
import application.item.ItemInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent; 
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.*; 

import javax.imageio.ImageIO;
public class SampleController implements Initializable {

	@FXML private Button goBtn; 
	@FXML private Button btnLoadCfg;
	@FXML private Button btnExtract;
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
    @FXML private TableColumn<ItemInfo, String> col_link;
	
	@FXML private ComboBox<String> cbMaker;

	private JSONParser jsonParser = new JSONParser();
	private static final Logger logger = LogManager.getLogger(SampleController.class);
	private WebEngine webEngine;
	private ObservableList<ItemInfo> tableData = FXCollections.observableArrayList();
	
	private static org.json.JSONObject config;
	private static List<String> makerList;
	static {
		config = Utils.readConfig();
		makerList = Utils.readMaker();
	}
	
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub 
		
		goBtn.setOnAction(event -> OnGoBtn(event));
		cbMaker.setOnAction(evt -> OnSelctMaker(evt));
		btnExtract.setOnAction(evt -> OnExtract(evt));
		webEngine = webView.getEngine();
		ObservableList<String> options = FXCollections.observableArrayList(makerList);
		cbMaker.setItems(options);

		col_index.setCellValueFactory(x -> x.getValue().getIndex());
		col_code.setCellValueFactory(x -> x.getValue().getCode());
		col_name.setCellValueFactory(x -> x.getValue().getName());
		col_price.setCellValueFactory( x -> x.getValue().getPrice());
		col_img1.setCellValueFactory(x -> x.getValue().getImg1());
		col_img2.setCellValueFactory(x -> x.getValue().getImg2());
		col_img3.setCellValueFactory(x -> x.getValue().getImg3()); 
		
		
		
	}
	
	
	private void OnExtract(ActionEvent evt) {
		// TODO Auto-generated method stub 
		String imgUrl = tableView.getItems().stream().findFirst().get().getImg1().get();
		System.out.println(imgUrl);
	}


	public void OnGoBtn(ActionEvent event) {
		if(textUrl.getText().length() > 0) {
			
			String url = textUrl.getText();
			webEngine.load(url);
		}else {
			Utils.ShowAlert("URL ÇÊ¿ä");
		}
	}

	@SuppressWarnings("unused")
	public void OnLoadCfg(ActionEvent event) {
		//Utils.ShowAlert("filter");
		try {
			String html =  webEngine.executeScript("document.documentElement.outerHTML").toString();
			ItemParser itemParser = new ItemParser(html);
			
			String maker = cbMaker.getValue();
			JSONObject obj = (JSONObject) config.get(maker);
			String item_code = obj.getString("item_code");
			String item_name = obj.getString("item_name");
			String item_price = obj.getString("item_price"); 
			String item_img1 = obj.getString("item_img1");
			String item_img2 = obj.getString("item_img2");
			String item_img3 = obj.getString("item_img3");

			itemParser.set_item_code(item_code);
			itemParser.set_item_name(item_name);
			itemParser.set_item_price(item_price); 
			itemParser.set_imgSrc(item_img1, item_img2, item_img3);
			
			itemParser.parser();
			 
			ArrayList<String> codeList = itemParser.getListCode();
			ArrayList<String> nameList = itemParser.getListName();
			ArrayList<String> priceList = itemParser.getListPrice();
			HashMap<Integer, ItemSrc> imgSrcList = itemParser.getImgSrcSetList();
			
			
			tableView.getItems().clear();
			 
			for(int i=0; i< codeList.size(); i++) {
				ItemInfo e = new ItemInfo(Integer.toString(i),codeList.get(i), nameList.get(i), priceList.get(i), 
						imgSrcList.get(i).getImg1(), imgSrcList.get(i).getImg2(), imgSrcList.get(i).getImg3(), "http");
				tableData.add(e);
			}
			
			tableView.setItems(tableData);
			tableView.refresh();
		}catch(Exception e) {
			Utils.ShowAlert("parseing error : " + e.getLocalizedMessage());
		}
		
	}
	
	public void OnSelctMaker(ActionEvent evt) { 
		logger.info("click : " + cbMaker.getValue());
		String maker = cbMaker.getValue();
		JSONObject obj = (JSONObject) config.get(maker);
		String url = obj.getString("link");
		textUrl.setText(url);
	}


	public void handleExit() {
		
		Platform.exit();
		System.exit(0);
	}
}
