package valificationtool.marker;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.texteditor.MarkerUtilities;

public class BugMarker {
	public static final String MARKER_ID = "ValificationTool.BugMarker";
	public static void  createMarker(IResource resource, int line, String message){
	    Map attributes = new HashMap();
	    attributes.put(IMarker.PRIORITY, Integer.valueOf(IMarker.PRIORITY_NORMAL));
	    attributes.put(IMarker.SEVERITY, Integer.valueOf(IMarker.SEVERITY_WARNING));
	    attributes.put(IMarker.LINE_NUMBER, Integer.valueOf(line));
	    attributes.put("varification_error_code", "Upper");
	    attributes.put(IMarker.MESSAGE, message);
	    try {
			MarkerUtilities.createMarker(resource, attributes, MARKER_ID);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
