package valificationtool.actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;

import valificationtool.util.OriginalRuntime;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class SampleAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	/**
	 * The constructor.
	 */
	public SampleAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action){
		Shell parent = Display.getCurrent().getActiveShell();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(parent);
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException{
					String[] cmd = {"java", "-version"};
					// 進捗を監視する処理
					monitor.beginTask("1秒寝る処理を10回繰り返します。", 1);

					OriginalRuntime or = new OriginalRuntime();
					try {
						or.execCmd(cmd);
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
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

	/**
	 * Selection in the workbench has been changed. We
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}