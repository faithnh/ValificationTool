package valificationtool.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.RegexFileInfoMatcher;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import valificationtool.marker.BugMarker;
import valificationtool.model.ErrorInformation;
import valificationtool.model.FixProgram;
import valificationtool.model.MacroInformation;
import valificationtool.model.MacroInformationCollection;
import valificationtool.model.ProgramChart;
import valificationtool.model.UseProgramChart;
import valificationtool.util.GetMacroRuntime;
import valificationtool.util.OriginalRuntime;
import valificationtool.util.ProjectInformation;
import valificationtool.views.SampleView;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SampleHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SampleHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parent = Display.getCurrent().getActiveShell();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(parent);
		IProject project = null;
		//プロジェクトのファイル(C言語)を取得
		final ArrayList<String> projects;
		try {
			project = ProjectInformation.getProject(event);
			projects = ProjectInformation.getProjectFiles(event);
			UseProgramChart.setProject_path(ProjectInformation.getProjectPath(event));
			UseProgramChart.setProject_name(ProjectInformation.getProjectName(event));
			UseProgramChart.initMacroInformaitonCollection();
			if(projects.size() > 0){
				try {
					dialog.run(true, true, new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException{
							// 進捗を監視する処理
							OriginalRuntime or = new OriginalRuntime();
							monitor.beginTask("検証式の追加中。", projects.size() + 1);
							ArrayList<String> cbmc_cmd = new ArrayList<String>();
							cbmc_cmd.add("cbmc/cbmc.exe");
							for(int i = 0; i < projects.size(); i++){
								//検証式の付加
								String[] ansic_cmd = {"ansic/ansic.exe", projects.get(i), "--array-unbound-check","--undefined-control-check","--zero-division-check"};

								cbmc_cmd.add(projects.get(i).toString() + "_out.c_pre.c_output.c");
								try {
									or.execCmd(ansic_cmd);
								} catch (IOException e) {
									// 自動生成された catch ブロック
									e.printStackTrace();
								}

								//マクロの取得
								String[] macro_cmd = {"gcc","-E","-dM",projects.get(i)};
								GetMacroRuntime gmr = new GetMacroRuntime();
								ArrayList<String> macro;
								try {
									macro = gmr.execCmd(macro_cmd);
								} catch (IOException e) {
									// 自動生成された catch ブロック
									e.printStackTrace();
									macro = new ArrayList<String>();
								}
								ArrayList<MacroInformation> macroInfo = new ArrayList<MacroInformation>();
								String programName = projects.get(i);
								String projectName = projects.get(i);
								for(int m_i = 0; m_i < macro.size(); m_i++){
								    Pattern pattern = Pattern.compile("(#define) ([^ ]+)(.*)");
								    Matcher matcher = pattern.matcher(macro.get(m_i));
								    MacroInformation m;
								    if(matcher.find()){
									    if(matcher.groupCount() == 3){
									    	m = new MacroInformation(matcher.group(2), matcher.group(3).trim());
									    }else{
									    	m = new MacroInformation(matcher.group(2), "");
									    }
								    }else{
								    	m = new MacroInformation("", "");
								    }
								    macroInfo.add(m);
								}
								UseProgramChart.macro_information_collection.add(new MacroInformationCollection(macroInfo, programName, projectName));
								monitor.worked(1);
							}
							cbmc_cmd.add("--slice-formula");
							//cbmcの実行
							UseProgramChart.initString();
							try {
								or.execCmd(cbmc_cmd.toArray(new String[cbmc_cmd.size()]));
							} catch (IOException e) {
								// 自動生成された catch ブロック
								e.printStackTrace();
							}

							//一時ファイルの削除
							for(int i = 0; i < projects.size(); i++){
								File f = new File(projects.get(i).toString() + "_out.c");
								f.delete();
								f = new File(projects.get(i).toString() + "_out.c_pre.c");
								f.delete();
								f = new File(projects.get(i).toString() + "_out.c_pre.c_output.c");
								f.delete();
								f = new File(projects.get(i).toString() + "_out.c_pre.c_output.c,tmp");
								f.delete();
							}

							monitor.worked(1);
							monitor.done();
						}
					} );
				} catch(InvocationTargetException e) {
					// 処理中に何らかの例外が発生したときの処理
				} catch(InterruptedException e) {
					// キャンセルされたときの処理
				}
			}
		} catch (Exception e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		IViewPart v = page.findView("valificationtool.views.SampleView");
		if(v != null){
			page.hideView(v);
		}
		try {
			IViewPart view = page.showView("valificationtool.views.SampleView");

		} catch (PartInitException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		try{
			//プロジェクトからファイル情報の取得
			ArrayList<IFile> files = new ArrayList<IFile>();

			ProjectInformation.getProjectFiles(project, files);

			//プロジェクト単位でマーカー削除
			project.deleteMarkers(BugMarker.MARKER_ID, false, IResource.DEPTH_INFINITE);

			if(UseProgramChart.p.size() > 0){
				ProgramChart programInfo = UseProgramChart.p.get(UseProgramChart.p.size() - 1);
				//検証エラーを発生したところにマーカーを入れる
				for(int i = 0; i < files.size(); i++){
					if(files.get(i).getLocation().toString().equals(programInfo.getFile())){
						BugMarker.createMarker((IResource)files.get(i), programInfo.getLine(), UseProgramChart.outputErrorStatement());
					}
				}

				//修正方法の生成
				UseProgramChart.initFixPrograms();
				UseProgramChart.fixPrograms = FixProgram.getFixProgramUpperUnbound(UseProgramChart.p, UseProgramChart.getError_info(),
						UseProgramChart.macro_information_collection);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
