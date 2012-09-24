package valificationtool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class ProjectInformation{
	/**
	 * プロジェクトの情報を取得する
	 * @return プロジェクトのファイル一覧を返す
	 */
	public static ArrayList<String> getProjectFiles(ExecutionEvent event) throws Exception{
		ArrayList<String> result = new ArrayList<String>();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		StringBuilder builder = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		if(currentSelection instanceof IStructuredSelection){
			IStructuredSelection sSelection = (IStructuredSelection) currentSelection;
			for(Iterator<?> iterator = sSelection.iterator(); iterator.hasNext();){
				Object type=(Object)iterator.next();
				if(type instanceof IProject){
					IProject e = (IProject) type;
					getProjectFilesNames(e, result);
				}
				System.out.println("type="+type);
			}
		}
		return result;
	}
	public static String getProjectPath(ExecutionEvent event) throws Exception{
		String result = "";
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		StringBuilder builder = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		if(currentSelection instanceof IStructuredSelection){
			IStructuredSelection sSelection = (IStructuredSelection) currentSelection;
			for(Iterator<?> iterator = sSelection.iterator(); iterator.hasNext();){
				Object type=(Object)iterator.next();
				if(type instanceof IProject){
					IProject e = (IProject) type;
					result = e.getLocation().toString();
				}
				System.out.println("type="+type);
			}
		}
		return result;
	}

	public static String getProjectName(ExecutionEvent event) throws Exception{
		String result = "";
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		StringBuilder builder = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		if(currentSelection instanceof IStructuredSelection){
			IStructuredSelection sSelection = (IStructuredSelection) currentSelection;
			for(Iterator<?> iterator = sSelection.iterator(); iterator.hasNext();){
				Object type=(Object)iterator.next();
				if(type instanceof IProject){
					IProject e = (IProject) type;
					result = e.getName();
				}
				System.out.println("type="+type);
			}
		}
		return result;
	}
	/**
	 * プロジェクトから名前を取得する
	 * @param p 対象のプロジェクト名
	 * @param result 取得したプロジェクト名一覧
	 * @throws Exception
	 */
	public static void getProjectFilesNames(IProject p, ArrayList<String> result) throws Exception{
		IResource[] sources = p.members();
		for(IResource item : sources){
			if(item.getType() == IResource.FOLDER){
				IFolder folder = (IFolder)item;
				getProjectFilesNames(folder, result);
			}else if(item.getType() == IResource.FILE){
				IFile file = (IFile)item;
				String tmp = file.getLocation().toString();
				if(tmp.lastIndexOf(".c")+2 == tmp.length()){
					result.add(tmp);
				}
			}
		}
	}

	/**
	 * フォルダから名前を取得する
	 * @param p 対象のフォルダ名
	 * @param result 取得したプロジェクト名一覧
	 * @throws Exception
	 */
	public static void getProjectFilesNames(IFolder p, ArrayList<String> result) throws Exception{
		IResource[] sources = p.members();
		for(IResource item : sources){
			if(item.getType() == IResource.FOLDER){
				IFolder folder = (IFolder)item;
				getProjectFilesNames(folder, result);
			}else if(item.getType() == IResource.FILE){
				IFile file = (IFile)item;
				String tmp = file.getLocation().toString();
				if(tmp.lastIndexOf(".c")+2 == tmp.length()){
					result.add(tmp);
				}
			}
		}
	}
	public static IProject getProject(ExecutionEvent event) throws Exception{
		IProject result = null;
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		StringBuilder builder = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator");
		if(currentSelection instanceof IStructuredSelection){
			IStructuredSelection sSelection = (IStructuredSelection) currentSelection;
			for(Iterator<?> iterator = sSelection.iterator(); iterator.hasNext();){
				Object type=(Object)iterator.next();
				if(type instanceof IProject){
					result = (IProject) type;
					break;
				}
			}
		}
		return result;
	}
	/**
	 * プロジェクトからファイル情報の取得
	 * @param p 対象のプロジェクト
	 * @param result 取得したファイル情報
	 * @throws Exception
	 */
	public static void getProjectFiles(IProject p, ArrayList<IFile> result) throws Exception{
		IResource[] sources = p.members();
		for(IResource item : sources){
			if(item.getType() == IResource.FOLDER){
				IFolder folder = (IFolder)item;
				getProjectFiles(folder, result);
			}else if(item.getType() == IResource.FILE){
				IFile file = (IFile)item;
				String tmp = file.getLocation().toString();
				if(tmp.lastIndexOf(".c")+2 == tmp.length()){
					result.add(file);
				}
			}
		}
	}

	/**
	 * フォルダからファイル情報の取得
	 * @param p 対象のフォルダ
	 * @param result 取得したファイル情報
	 * @throws Exception
	 */
	public static void getProjectFiles(IFolder p, ArrayList<IFile> result) throws Exception{
		IResource[] sources = p.members();
		for(IResource item : sources){
			if(item.getType() == IResource.FOLDER){
				IFolder folder = (IFolder)item;
				getProjectFiles(folder, result);
			}else if(item.getType() == IResource.FILE){
				IFile file = (IFile)item;
				String tmp = file.getLocation().toString();
				if(tmp.lastIndexOf(".c")+2 == tmp.length()){
					result.add(file);
				}
			}
		}
	}
}
