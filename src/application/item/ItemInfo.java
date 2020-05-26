package application.item;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.SampleController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemInfo {

	private static final Logger logger = LogManager.getLogger(ItemInfo.class);
	/**
	 * 
	 */
	public StringProperty getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code.set(code);
	}

	public StringProperty getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price.set(price);
	}

	public StringProperty getName() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index.set(index);
	}

	public StringProperty getImg1() {
		return img1;
	}

	public void setImg1(String img1) {
		this.img1.set(img1);
	}

	public StringProperty getImg2() {
		return img2;
	}

	public void setImg2(String img2) {
		this.img2.set(img2);
	}

	public StringProperty getImg3() {
		return img3;
	}

	public void setImg3(String img3) {
		this.img3.set(img3);
	}
	
	public void setItemLink(String itemLink) {
		this.itemlink.set(itemLink);
	}
	public StringProperty getItemLink() {
		return itemlink;
	}
	
	public void setWon(String won) {
		this.won.set(won);;
	}
	public StringProperty getWon() {
		return won;
	} 
	
	private StringProperty  price;
	private StringProperty  name;
	private StringProperty  index;
	private StringProperty  img1, img2, img3;
	private StringProperty	itemlink;
	private StringProperty won; 
	private SimpleStringProperty code;
	
	public ItemInfo(String index, String code, String name, String price, String img1, String img2, String img3, String itemLink, int cur) {
		this.index = new SimpleStringProperty(index);
		this.code = new SimpleStringProperty(code);
		this.name = new SimpleStringProperty(name);
		this.price = new SimpleStringProperty(price);
		this.img1 = new SimpleStringProperty(img1);
		this.img2 = new SimpleStringProperty(img2);
		this.img3 = new SimpleStringProperty(img3);
		this.itemlink = new SimpleStringProperty(itemLink);
		this.won = new SimpleStringProperty(exchange(price, cur)); 
	}
	
	private String exchange(String euro, int cur) {
		int tmp =1;
		String str;
		try {
			String stmp = euro.replace(",", "").replace(" ","").replace("$", "").replace("¢æ", "").replace(".", "");
			tmp = Integer.parseInt(stmp);
		}catch(Exception e) {
			logger.error("euro : " + euro);
		}
		str = Integer.toString(tmp * cur);
		return str;
	}
}
