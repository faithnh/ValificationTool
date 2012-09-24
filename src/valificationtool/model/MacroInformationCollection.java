package valificationtool.model;

import java.util.ArrayList;

public class MacroInformationCollection {
	ArrayList<MacroInformation> macroInfo;
	private String programName;
	private String projectName;
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public MacroInformationCollection(ArrayList<MacroInformation> macroInfo,
			String programName, String projectName){
		this.macroInfo = macroInfo;
		this.programName = programName;
		this.projectName = projectName;
	}

	public static MacroInformationCollection find(ArrayList<MacroInformationCollection> m, String fileName){
		MacroInformationCollection result = null;

		for(int i = 0; i < m.size(); i++){
			if(m.get(i).getProgramName().equals(fileName)){
				result = m.get(i);
				break;
			}
		}

		return result;

	}

	public ArrayList<MacroInformation> findMacroByValue(String value){
		ArrayList<MacroInformation> result = new ArrayList<MacroInformation>();
		for(int i = 0; i < macroInfo.size(); i++){
			if(macroInfo.get(i).getValue().equals(value)){
				result.add(macroInfo.get(i));
			}
		}
		return result;
	}
}
