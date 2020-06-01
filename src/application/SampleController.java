package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import application.item.ItemInfo; 
import application.utils.GucciParser;
import application.utils.ItemSrc;
import application.utils.Parser;
import application.utils.Utils; 
import application.utils.PradaParser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.*;
import javafx.stage.Stage;
public class SampleController implements Initializable { 
	private static final Logger logger = LogManager.getLogger(SampleController.class);
	
	@FXML private BorderPane boarder;
	@FXML private Button goBtn; 
	@FXML private Button btnLoadCfg;
	@FXML private Button btnExtract;
	@FXML private Button btnClear;
	@FXML private WebView webView;
	@FXML private WebView adview;
	@FXML private TextField textUrl;
	@FXML private TextField tb_currency;
	@FXML private TableView<ItemInfo> tableView; 
	@FXML private TableColumn<ItemInfo, String> index;
    @FXML private TableColumn<ItemInfo, String> code; 
    @FXML private TableColumn<ItemInfo, String> col_index;
    @FXML private TableColumn<ItemInfo, String> col_code;
	@FXML private TableColumn<ItemInfo, String> col_name;
    @FXML private TableColumn<ItemInfo, String> col_price;
    @FXML private TableColumn<ItemInfo, String> col_won;
    @FXML private TableColumn<ItemInfo, String> col_img1;
    @FXML private TableColumn<ItemInfo, String> col_img2;
    @FXML private TableColumn<ItemInfo, String> col_img3;
    @FXML private TableColumn<ItemInfo, String> col_link;
    
	
	@FXML private ComboBox<String> cbMaker;
	@FXML private ComboBox<String> cbType;
	@FXML private ComboBox<String> cbSex;
 
	private WebEngine webEngine;
	private WebEngine adWebEngine;
	private ObservableList<ItemInfo> tableData = FXCollections.observableArrayList();
	
	GucciParser gucciParser;
	PradaParser pradaParser;
	Parser parser;
	Scene scene ;
	
	private static org.json.JSONObject config;
	private static List<String> makerList;
	private static List<String> categoriesList;
	private static List<String> sexList;
	
	
	private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A";
	private static String htmlCode = "<html><body>- <script async src=\"https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js\"></script>\r\n" + 
			"<!-- deutsch -->\r\n" + 
			"<ins class=\"adsbygoogle\"\r\n" + 
			"     style=\"display:block\"\r\n" + 
			"     data-ad-client=\"ca-pub-6388414680496509\"\r\n" + 
			"     data-ad-slot=\"1901376121\"\r\n" + 
			"     data-ad-format=\"auto\"\r\n" + 
			"     data-full-width-responsive=\"true\"></ins>\r\n" + 
			
			"<script>\r\n" + 
			"     (adsbygoogle = window.adsbygoogle || []).push({});\r\n" + 
			"</script>-</body></html>";
	static {
		config = Utils.readConfig();
		makerList = Utils.readMaker();
		categoriesList = Utils.readCategories();
		sexList = Utils.readSex();
	}
	
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub 
  
		//조회
		goBtn.setOnAction(event -> OnGoBtn(event));
		cbMaker.setOnAction(evt -> OnSelctMaker(evt));
		cbType.setOnAction(evt -> onSelectCategory(evt));
		cbSex.setOnAction(evt ->OnSelectSex(evt));
		btnLoadCfg.setOnAction(evt -> OnLoadCfg(evt));
		btnClear.setOnAction(evt -> OnClearDataView(evt));
		btnExtract.setOnAction(evt -> OnExtract(evt));
		
		webEngine = webView.getEngine();
		//webEngine.setUserAgent(USER_AGENT);
		
		adWebEngine = adview.getEngine();
		adWebEngine.loadContent(htmlCode, "text/html");
		adWebEngine.setUserAgent(USER_AGENT);
		
		//메이커 불러로고 combobox 데이터 추가
		ObservableList<String> options = FXCollections.observableArrayList(makerList);
		cbMaker.setItems(options);
		
		//카테고리 불러로고 combobox 데이터 추가
		ObservableList<String> optionType = FXCollections.observableArrayList(categoriesList);
		cbType.setItems(optionType);

		col_index.setCellValueFactory(x -> x.getValue().getIndex());
		col_code.setCellValueFactory(x -> x.getValue().getCode());
		col_name.setCellValueFactory(x -> x.getValue().getName());
		col_price.setCellValueFactory( x -> x.getValue().getPrice());
		col_won.setCellValueFactory(x -> x.getValue().getWon());
		col_img1.setCellValueFactory(x -> x.getValue().getImg1());
		col_img2.setCellValueFactory(x -> x.getValue().getImg2());
		col_img3.setCellValueFactory(x -> x.getValue().getImg3()); 
		col_link.setCellValueFactory(x -> x.getValue().getItemLink());
		

		tableView.getSelectionModel().setCellSelectionEnabled(true);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		//setCopyTable();
		
		final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
	    tableView.setOnKeyPressed(event -> {
	        if (keyCodeCopy.match(event)) {
	            copySelectionToClipboard(tableView);
	        }
	    });

	    setContextMenu();
	    
	} 
	
	private void OnClearDataView(ActionEvent evt) {
		// TODO Auto-generated method stub
		tableView.getItems().clear();
		tableData.clear();		
	}

	public void getScene() {
		this.scene = boarder.getScene();
	}
	
	private void setContextMenu() {
		ContextMenu menu = new ContextMenu();
		try {
			MenuItem item = new MenuItem("Copy");
			item.setOnAction(evt -> copySelectionToClipboard(tableView));			
			menu.getItems().add(item);
			tableView.setContextMenu(menu);
		}catch(Exception e) {
			logger.error( e.getStackTrace().toString());
		}
	} 
	
	@SuppressWarnings("rawtypes")
	public void copySelectionToClipboard(final TableView<?> table) {
	    final Set<Integer> rows = new TreeSet();
	    for (final TablePosition tablePosition : table.getSelectionModel().getSelectedCells()) {
	        rows.add(tablePosition.getRow());
	    }
	    final StringBuilder strb = new StringBuilder();
	    boolean firstRow = true;
	    for (final Integer row : rows) {
	        if (!firstRow) {
	            strb.append('\n');
	        }
	        firstRow = false;
	        boolean firstCol = true;
	        for (final TableColumn<?, ?> column : table.getColumns()) {
	            if (!firstCol) {
	                strb.append('\t');
	            }
	            firstCol = false;
	            final Object cellData = column.getCellData(row);
	            strb.append(cellData == null ? "" : cellData.toString());
	        }
	    }
	    final ClipboardContent clipboardContent = new ClipboardContent();
	    clipboardContent.putString(strb.toString());
	    Clipboard.getSystemClipboard().setContent(clipboardContent);
	}
	
	private void OnExtract(ActionEvent evt) {
		// TODO Auto-generated method stub 
		
		Consumer<ItemInfo> consumer = new Consumer<ItemInfo>() {
			@Override
			public void accept(ItemInfo t) {
				// TODO Auto-generated method stub
				String code = t.getCode().get();
				
				String img1 = t.getImg1().get();
				String img2 = t.getImg2().get();
				String img3 = t.getImg3().get();
				
				logger.info("img1 : {}, img2 : {}, img3 : {}", img1, img2, img3);
				String cdir = "C:/img/";
				
				if(img1 != null)
					parser.saveImg(img1, cdir, code+"_1", "jpg");
				if(img2 != null)
					parser.saveImg(img2,cdir, code+"_2", "jpg");
				if(img3 != null)
					parser.saveImg(img3, cdir, code+"_3", "jpg");
			}
		};
		try {
			this.boarder.getScene().setCursor(Cursor.WAIT);
			tableView.getItems().stream().forEach(consumer);
			Utils.ShowAlert("이미지 저장 완료");
		}
		catch(Exception e) {
			Utils.ShowAlert("이미지 저장 실패, " + e.getLocalizedMessage());
		}
		finally {
			this.boarder.getScene().setCursor(Cursor.DEFAULT);
		}
	}
	

	//페이지 이동
	public void OnGoBtn(ActionEvent event) {
		if(textUrl.getText().length() > 0) {
			String url = textUrl.getText();
			webEngine.load(url);
			
		}else {
			Utils.ShowAlert("URL 필요");
		}
	}

	
	@SuppressWarnings("unused")
	public void OnLoadCfg(ActionEvent event) {
		//Utils.ShowAlert("filter"); 
		this.boarder.getScene().setCursor(Cursor.WAIT);
		
		String html =  webEngine.executeScript("document.documentElement.outerHTML").toString();
		ArrayList<String> priceList = null, linkList = null, nameList= null, codeList = null;
		
		HashMap<Integer, ItemSrc> imgSrcList = null;
		
		try {
			String maker = cbMaker.getValue();
			if(maker.equals("gucci")) {
				parser = new GucciParser(html); 
				JSONObject obj = (JSONObject) config.get(maker);
				
				String item_code = obj.getString("item_code");
				String item_name = obj.getString("item_name");
				String item_price = obj.getString("item_price"); 
				String item_img1 = obj.getString("item_img1");
				String item_img2 = obj.getString("item_img2");
				String item_img3 = obj.getString("item_img3");
				String item_link = obj.getString("item_link");
				 
				
				parser.parser(item_code, item_name, item_price, item_img1, item_img2, item_img3, item_link);
				 
				codeList= parser.getListCode();
				nameList = parser.getListName();
				priceList = parser.getListPrice();
				imgSrcList = parser.getImgSrcSetList();
				linkList = parser.getLinkList();
			}
			else if(maker.equals("prada"))
			{
				parser  = new PradaParser(html);
				JSONObject obj = (JSONObject)config.get(maker);
				
				String item_code = obj.getString("item_code");
				String item_name = obj.getString("item_name");
				String item_price = obj.getString("item_price"); 
				String item_img1 = obj.getString("item_img1");
				String item_img2 = obj.getString("item_img2");
				String item_img3 = obj.getString("item_img3");
				String item_link = obj.getString("item_link");
				 
				
				parser.parser(item_code, item_name, item_price, item_img1, item_img2, item_img3, item_link);
				
				codeList= parser.getListCode();
				nameList = parser.getListName();
				priceList = parser.getListPrice();
				imgSrcList = parser.getImgSrcSetList();
				linkList = parser.getLinkList();
				
			}
			
			tableView.getItems().clear();
			tableData.clear();
			int currency = Integer.parseInt(tb_currency.getText().replace(",", "").replace("$", "").replace(" ", "").replace("€", "").replace("£", ""));
			String index, code, name, price ="";
			String  img1, img2, img3;
			String itemLink;
			
			for(int i=0; i< codeList.size(); i++) {
				index = Integer.toString(i);
				code = codeList.get(i);
				name = nameList.get(i);
				price = priceList.get(i);
				img1 = imgSrcList.get(i).getImg1();
				img2 = imgSrcList.get(i).getImg2();
				img3 = imgSrcList.get(i).getImg3();
				itemLink = linkList.get(i);
				
				ItemInfo e = new ItemInfo(index, code, name, price, img1,img2,img3, itemLink, currency);
				tableData.add(e);
			}
			
			tableView.setItems(tableData);
			tableView.refresh();
		}catch(Exception e) {
			Utils.ShowAlert(this.getClass().getName() + "  parseing error : "+ e.getLocalizedMessage());
		}finally {
			this.boarder.getScene().setCursor(Cursor.DEFAULT);
		}
		
	}


	private void OnSelectSex(ActionEvent evt) {
		// TODO Auto-generated method stub
		
	}

	
	private void onSelectCategory(ActionEvent evt) {
		logger.info("click categories : {}", cbType.getValue() );
		String maker = cbMaker.getValue();
		String category = cbType.getValue();
		
		if(maker.isEmpty())
		{
			Utils.ShowAlert("메이커를 먼저 선택하세요");
		}else {
			JSONObject obj = (JSONObject) config.get(maker);
			String categoryTag = category + "_link";
			String url = obj.getString(categoryTag);
			textUrl.setText(url);	
		} 
	} 
	 
	public void OnSelctMaker(ActionEvent evt) { 
		logger.info("maker click : " + cbMaker.getValue()); 
		textUrl.setText("");		
	} 
}
