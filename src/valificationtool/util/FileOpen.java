package valificationtool.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import valificationtool.model.UseProgramChart;

public class FileOpen {
	/**
	 * 指定したプロジェクトのファイルをワークベンチから開ける
	 * @param programPath 対象のプログラムのパス
	 * @param projectPath 対象のプロジェクトのパス
	 * @return
	 */
	public static IEditorPart getIEditorPart(String programPath, String projectPath){

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IContainer container = root.getProject(UseProgramChart.getProject_name());
		IFile file = container.getFile(new Path(programPath.replaceAll(projectPath,"")));
	    IWorkbenchPage page=  window.getActivePage();
	    IEditorPart editorPart = null;
	    try {
			editorPart = IDE.openEditor(page, file);
		} catch (PartInitException e) {
			e.printStackTrace();
			return null;
		}

	    return editorPart;
	}

	public static void jumpLineOnFile(String programPath, String projectPath, int line) throws Exception{
		//ファイルを開ける
	    IEditorPart editorPart = FileOpen.getIEditorPart(programPath, projectPath);


		IEditorInput editorInput = editorPart.getEditorInput();
		IResource resource = (IResource)editorInput.getAdapter(IResource.class);
		//マーカを作成し、指定行ジャンプを行う
		Map attributes = new HashMap();
		attributes.put(IMarker.LINE_NUMBER, line);
		IMarker marker = resource.createMarker(IMarker.TEXT);
		marker.setAttributes(attributes);

		IDE.gotoMarker(editorPart, marker);

		marker.delete();
	}
}
