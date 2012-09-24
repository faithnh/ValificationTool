package valificationtool.model;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.GC;

public class UseProgramChart {
	public static ArrayList<ProgramChart> p;
	private static String xml = "";
	private static String project_path;
	private static String project_name;
	private static ErrorInformation error_info;
	public static ArrayList<MacroInformationCollection> macro_information_collection;
	public static ArrayList<FixProgram> fixPrograms;
	public static void initString(){
		xml = "";
	}
	public static void addString(String line){
		xml = xml + line + "\n";
	}
	public static void initMacroInformaitonCollection(){
		macro_information_collection = new ArrayList<MacroInformationCollection>();
	}
	public static void gerenateProgramChart(/*GC gc*/) throws Exception{
		if(p != null){
			p.clear();
		}
		p = ProgramChart.gerenateProgramChart("<document>"+xml.replace("#dquot#","\"")+"</document>");
		error_info = ErrorInformation.generateErrorInformation("<document>"+xml.replace("#dquot#","\"")+"</document>");
	}
	public static void initFixPrograms(){
		fixPrograms = new ArrayList<FixProgram>();
	}

	public static void setViewer(TableViewer v){
		ProgramChart.setViewer(p, v);
	}
	public static String getProject_path() {
		return project_path;
	}
	public static void setProject_path(String project_path) {
		UseProgramChart.project_path = project_path;
	}
	public static String getProject_name() {
		return project_name;
	}
	public static void setProject_name(String project_name) {
		UseProgramChart.project_name = project_name;
	}
	public static ErrorInformation getError_info() {
		return error_info;
	}
	public static void setError_info(ErrorInformation error_info) {
		UseProgramChart.error_info = error_info;
	}
	public static String outputErrorStatement() {
		String result;
		if(error_info != null){
			result = error_info.getType() + " is occured on variable " + error_info.getVariable() + ". ";
			if(error_info.getType().equals("Upper Unbound") || error_info.getType().equals("Lower Unbound") ){

				if(error_info.getExpression() != null){
					result = result + "The value of statement " +  error_info.getExpression() + " is " + error_info.getValue() + ". ";
				}else{
					result = result + "The value accessing is " + error_info.getValue() + ". ";
				}
				result = result + "(This pointer is accessing on " + error_info.getBasic_location() + " internally. )";
			}
		}else{
			result = "No error occured!!";
		}
		return result;
	}
}
