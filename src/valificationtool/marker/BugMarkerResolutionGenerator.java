package valificationtool.marker;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import valificationtool.model.FixProgram;
import valificationtool.model.UseProgramChart;
import valificationtool.util.string.FindStringInformation;
import valificationtool.util.string.StringExtend;

public class BugMarkerResolutionGenerator implements IMarkerResolutionGenerator {
			@Override
			public IMarkerResolution[] getResolutions(IMarker marker) {
				ArrayList<IMarkerResolution> result = new ArrayList<IMarkerResolution>();
				final ArrayList<FixProgram> fixPrograms = UseProgramChart.fixPrograms;
				for(int i = 0; i < fixPrograms.size(); i++){
					final FixProgram f = fixPrograms.get(i);
					IMarkerResolution2 resolution = new IMarkerResolution2() {

						public String getLabel() {
							String label = "";
							if(f.getType().equals(FixProgram.UPPER_UNBOUND_ERROR)){
								label = "Add the condition on line " + f.getLine() + ".";
							}
							return label;
						}
						public void run(IMarker marker) {
							// 解決処理
							if(f.getType().equals(FixProgram.UPPER_UNBOUND_ERROR)){
								fixUpperUnboundError(f);
							}
						}
						@Override
						public String getDescription() {
							String description = "";
							if(f.getType().equals(FixProgram.UPPER_UNBOUND_ERROR)){
								description = f.getFixProblemStatement();
							}
							return description;
						}
						@Override
						public Image getImage() {
							return null;
						}
					};
					result.add(resolution);
				}
				return result.toArray(new IMarkerResolution[result.size()]);
			}
			public void fixUpperUnboundError(FixProgram f){
				//リソースからファイルを開ける
        		IWorkbench workbench = PlatformUI.getWorkbench();
        		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        		IWorkspace workspace = ResourcesPlugin.getWorkspace();
        		IWorkspaceRoot root = workspace.getRoot();
        		IContainer container = root.getProject(UseProgramChart.getProject_name());
        		IFile file = container.getFile(new Path(f.getProgramPath().replaceAll(f.getProjectName(),"")));
    		    IWorkbenchPage page=  window.getActivePage();
    		    IEditorPart editorPart = null;
    		    try {
					editorPart = IDE.openEditor(page, file);
				} catch (PartInitException e) {
					e.printStackTrace();
					return;
				}

    		    IEditorInput editorInput = editorPart.getEditorInput();
    		    AbstractTextEditor aEditor = (AbstractTextEditor) editorPart;
    		    IDocument document =
    		    		  aEditor.getDocumentProvider().getDocument(editorInput);

    		    try {
    				//開いたファイルから修正対象の文字列を探索する
					String tmp = document.get(document.getLineOffset(f.getLine()-1),document.getLineLength(f.getLine()-1));
					tmp = StringExtend.replaceFuzzy(tmp, f.getFixBefore(), f.getFixAfter());

					//見つけた文字列を修正後の文字列に置き換える
					document.replace(document.getLineOffset(f.getLine()-1),document.getLineLength(f.getLine()-1), tmp);
    		    } catch (BadLocationException e) {
					e.printStackTrace();
					return;
				}

    		    //該当マーカーの削除

    		    IResource resource = (IResource)editorInput.getAdapter(IResource.class);
    		    try {
					IMarker[] m = resource.findMarkers(BugMarker.MARKER_ID, false, IResource.DEPTH_INFINITE);
					for(int i = 0; i < m.length; i++){
						if((Integer)m[i].getAttribute(IMarker.LINE_NUMBER) == f.getError_line()){
							m[i].delete();
							break;
						}
					}
				} catch (CoreException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

			}
}