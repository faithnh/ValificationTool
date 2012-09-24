package valificationtool.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Color;

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import valificationtool.views.AdditionalPaint;

public class ProgramChart {
	/*private int x;
	private int y;*/
	public ArrayList<ProgramChart> next_branch;
	public ArrayList<VariableInfo> before_variable_info;
	public ArrayList<VariableInfo> after_variable_info;
	private String program;

	private int line;
	private String type;

	private String file;
	private String project_name;

	public ProgramChart(){
		this("", 0,"","","");
	}
	public ProgramChart(String program, int line, String type, String file, String project_name){

		next_branch = new ArrayList<ProgramChart>();
		before_variable_info = new ArrayList<VariableInfo>();
		after_variable_info = new ArrayList<VariableInfo>();
		this.program = program;
		this.line = line;
		this.setType(type);
		this.file = file;
		this.project_name = project_name;

	}

	public void addNextBranch(ProgramChart p){
		next_branch.add(p);
	}

	public String getProgram(){
		return this.program;
	}

	public int getLine(){
		return this.line;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static void setViewer(ArrayList<ProgramChart> array_program_chart, TableViewer v){
		for(int i = 0; i < array_program_chart.size(); i++){
			v.add(array_program_chart.get(i));
		}
	}

	public static ArrayList<ProgramChart> gerenateProgramChart(String str) throws Exception{
		int gap = 10;

		ArrayList<ProgramChart> result = new ArrayList<ProgramChart>();

		//TODO:Create the Program chart data by xml data
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(str)));

		// ルート要素になっている子ノードを取得
		Element root = doc.getDocumentElement();
		System.out.println(doc.toString());
		//Get the prog tag.
		NodeList prog_list = root.getElementsByTagName("source");
		if(prog_list.getLength() > 0){
			//Get the source data from prog tags and add it to result.
			Element below_element = (Element) prog_list.item(0);
			Element below_source_tag = (Element) below_element.getElementsByTagName("prog").item(0);
			String below_path = below_element.getAttribute("file");
			ProgramChart below = new ProgramChart(below_source_tag.getAttribute("text"),
					Integer.parseInt(below_element.getAttribute("loc")),
					below_element.getAttribute("type"),
					below_path, UseProgramChart.getProject_path());
			NodeList before_variable_info_node = below_element.getElementsByTagName("before_variable_info");
			NodeList after_variable_info_node = below_element.getElementsByTagName("after_variable_info");
			Element before_variable_info_element;
			Element after_variable_info_element;
			if(before_variable_info_node.getLength() > 0){
				before_variable_info_element = (Element) before_variable_info_node.item(0);
				NodeList info_list = before_variable_info_element.getElementsByTagName("variable");
				for(int i = 0; i < info_list.getLength(); i++){
					Element info = (Element)(info_list.item(i));
					if(info.getAttribute("max_size").equals("")){
						below.before_variable_info.add(new VariableInfo(info.getAttribute("type"), info.getAttribute("name"), info.getAttribute("value")));
					}else{
						below.before_variable_info.add(new VariableInfo(info.getAttribute("type"), info.getAttribute("name"), info.getAttribute("value"),
								Integer.parseInt(info.getAttribute("max_size")), Integer.parseInt(info.getAttribute("basis_location")),  Integer.parseInt(info.getAttribute("defined")) != 0 ? true : false,
										Integer.parseInt(info.getAttribute("alloced")) != 0 ? true : false));
					}
				}
			}
			if(after_variable_info_node.getLength() > 0){
				after_variable_info_element = (Element) after_variable_info_node.item(0);
				NodeList info_list = after_variable_info_element.getElementsByTagName("variable");
				for(int i = 0; i < info_list.getLength(); i++){
					Element info = (Element)(info_list.item(i));
					if(info.getAttribute("max_size").equals("")){
						below.after_variable_info.add(new VariableInfo(info.getAttribute("type"), info.getAttribute("name"), info.getAttribute("value")));
					}else{
						below.after_variable_info.add(new VariableInfo(info.getAttribute("type"), info.getAttribute("name"), info.getAttribute("value"),
								Integer.parseInt(info.getAttribute("max_size")), Integer.parseInt(info.getAttribute("basis_location")),  Integer.parseInt(info.getAttribute("defined")) != 0 ? true : false,
										Integer.parseInt(info.getAttribute("alloced")) != 0 ? true : false));
					}
				}
			}
			result.add(below);

			for(int i = 1; i < prog_list.getLength(); i++){

				Element element = (Element) prog_list.item(i);
				Element source_tag = (Element) element.getElementsByTagName("prog").item(0);
				String path = element.getAttribute("file");
				ProgramChart next = new ProgramChart(source_tag.getAttribute("text"),
						Integer.parseInt(element.getAttribute("loc")),
						element.getAttribute("type"),
						path,UseProgramChart.getProject_path());
				before_variable_info_node = element.getElementsByTagName("before_variable_info");
				after_variable_info_node = element.getElementsByTagName("after_variable_info");
				if(before_variable_info_node.getLength() > 0){
					before_variable_info_element = (Element) before_variable_info_node.item(0);
					NodeList info_list = before_variable_info_element.getElementsByTagName("variable");
					for(int j = 0; j < info_list.getLength(); j++){
						Element info = (Element)(info_list.item(j));
						if(info.getAttribute("max_size").equals("")){
							next.before_variable_info.add(new VariableInfo(info.getAttribute("type"), info.getAttribute("name"), info.getAttribute("value")));
						}else{
							next.before_variable_info.add(new VariableInfo(info.getAttribute("type"), info.getAttribute("name"), info.getAttribute("value"),
									Integer.parseInt(info.getAttribute("max_size")), Integer.parseInt(info.getAttribute("basis_location")),  Integer.parseInt(info.getAttribute("defined")) != 0 ? true : false,
											Integer.parseInt(info.getAttribute("alloced")) != 0 ? true : false));
						}
					}
				}
				if(after_variable_info_node.getLength() > 0){
					after_variable_info_element = (Element) after_variable_info_node.item(0);
					NodeList info_list = after_variable_info_element.getElementsByTagName("variable");
					for(int j = 0; j < info_list.getLength(); j++){
						Element info = (Element)(info_list.item(j));
						if(info.getAttribute("max_size").equals("")){
							next.after_variable_info.add(new VariableInfo(info.getAttribute("type"), info.getAttribute("name"), info.getAttribute("value")));
						}else{
							next.after_variable_info.add(new VariableInfo(info.getAttribute("type"), info.getAttribute("name"), info.getAttribute("value"),
									Integer.parseInt(info.getAttribute("max_size")), Integer.parseInt(info.getAttribute("basis_location")),  Integer.parseInt(info.getAttribute("defined")) != 0 ? true : false,
											Integer.parseInt(info.getAttribute("alloced")) != 0 ? true : false));
						}
					}
				}
				result.add(next);
				below.addNextBranch(next);
				below = next;
			}
		}

		return result;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getFile() {
		return file;
	}
	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}
	public String getProject_name() {
		return project_name;
	}
}
