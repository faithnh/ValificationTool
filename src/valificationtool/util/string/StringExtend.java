package valificationtool.util.string;

public class StringExtend{
	public static FindStringInformation findFuzzy(String target, String src){
		FindStringInformation f = null;
		String tmp = src.replaceAll(" ", "").
				replaceAll("　", "").
				replaceAll("\t", "").
				replaceAll("\r\n", "").
				replaceAll("\n", "");
		int i, j, offset;
		for(i = 0; i < target.length(); i++){
			offset = 0;
			boolean find_mode = false;
			for(j = 0; i + j + offset < target.length() && j < tmp.length(); j++){
				if(target.charAt(i+j+offset) != tmp.charAt(j)){
					if(find_mode &&
						(target.charAt(i+j+offset) == ' ' ||
						target.charAt(i+j+offset) == '\t' ||
						target.charAt(i+j+offset) == '\n' ||
						target.charAt(i+j+offset) == '\r')){
						offset++;
					}else{
						find_mode = false;
						break;
					}
				}else{
					find_mode = true;
				}
			}
			if(j == tmp.length()){
				f = new FindStringInformation(i, j + offset);
			}
		}

		return f;
	}

	public static String replaceFuzzy(String target, String oldStr, String newStr){
		String str;
		FindStringInformation f = findFuzzy(target, oldStr);

		str = target.replace(target.substring(f.getOffset(), f.getOffset() + f.getLength()), newStr);

		return str;
	}
}

