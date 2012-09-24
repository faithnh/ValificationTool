package valificationtool.model;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ErrorInformation {
	private String type;
	private String variable;
	private String expression;
	private int value;
	private int basic_location;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getBasic_location() {
		return basic_location;
	}
	public void setBasic_location(int basic_location) {
		this.basic_location = basic_location;
	}

	public static ErrorInformation generateErrorInformation(String xml) throws Exception{
		ErrorInformation result;

		//TODO:Create the Program chart data by xml data
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

		// ルート要素になっている子ノードを取得
		Element root = doc.getDocumentElement();
		System.out.println(doc.toString());
		//Get the prog tag.
		NodeList errors = root.getElementsByTagName("error");
		if(errors.getLength() > 0){
			result = new ErrorInformation();
			Element temp = (Element)(errors.item(0));
			result.type = temp.getAttribute("type");
			result.variable = temp.getAttribute("variable");
			Element expr = (Element)(temp.getElementsByTagName("expression").item(0));
			Element belo = null;
			if(temp.getElementsByTagName("basic_location").getLength() != 0){
				belo = (Element)(temp.getElementsByTagName("basic_location").item(0));
				result.basic_location = Integer.parseInt(belo.getAttribute("value"));
			}
			result.value = Integer.parseInt(expr.getAttribute("value"));
			if(!expr.getAttribute("text").equals("")){
				result.expression = expr.getAttribute("text");
			}else{
				result.expression = null;
			}
		}else{
			result = null;
		}
		return result;
	}
}