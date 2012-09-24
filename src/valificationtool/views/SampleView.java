package valificationtool.views;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.eval.VariablesInfo;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.SWT;

import valificationtool.marker.BugMarker;
import valificationtool.model.ProgramChart;
import valificationtool.model.UseProgramChart;
import valificationtool.model.VariableInfo;
import valificationtool.util.Console;
import valificationtool.util.FileOpen;


public class SampleView extends ViewPart {
	//リストビュー
	Table list;
    // ListViewer
    TableViewer viewer;
    TableViewer variable_viewer;

    Label error_info;

    Composite error_view;
    Composite detail;

	public SampleView() {

	}

	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1,true));
		GridData error_gd = new GridData(GridData.FILL_HORIZONTAL);
		GridData detail_gd = new GridData(GridData.FILL_BOTH);
		//エラー情報
		error_view = new Composite(parent, SWT.NONE);
		error_view.setLayoutData(error_gd);
		error_view.setLayout(new FillLayout());
		error_info = new Label(error_view, SWT.H_SCROLL);

		//エラー詳細情報
		detail = new Composite(parent, SWT.NONE);
		detail.setLayoutData(detail_gd);
		detail.setLayout(new FillLayout());
		//リストビュー
		list = new Table(detail, SWT.V_SCROLL | SWT.FULL_SELECTION);
	     // 3. Tableの罫線表示を可する
		list.setLinesVisible(true);
 		// 4. Tableのヘッダー表示を可にする
        list.setHeaderVisible(true);
 		// 5. ヘッダーのカラムに値をセット
 		TableColumn column = new TableColumn(list, SWT.LEFT, 0);
 		column.setText("ファイル名");
 		column.setWidth(200);
 		column = new TableColumn(list, SWT.LEFT, 1);
 		column.setText("行");
 		column.setWidth(40);
 		column = new TableColumn(list, SWT.LEFT, 2);
 		column.setText("プログラム");
 		column.setWidth(100);
        // ListViewerに接続
        viewer = new TableViewer(list);
        viewer.setContentProvider(new ProgramChartContentProvider());
        viewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO 自動生成されたメソッド・スタブ
				return false;
			}

			@Override
			public void dispose() {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				// TODO 自動生成されたメソッド・スタブ
				if(element instanceof ProgramChart){
					ProgramChart p = (ProgramChart)element;
					switch(columnIndex){
					case 0:
							return p.getFile();
					case 1:
						if(p.getLine() != 0){
							return p.getLine() + "";
						}else{
							return "";
						}
					case 2:
						return p.getProgram();
					}
				}
				return "";
			}

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				// TODO 自動生成されたメソッド・スタブ
				return null;
			}
		});
        viewer.addDoubleClickListener(new IDoubleClickListener(){
        	public void doubleClick(DoubleClickEvent event) {

        		IStructuredSelection sel = (IStructuredSelection)event.getSelection();
        		ProgramChart target_program = (ProgramChart)sel.getFirstElement();
        		if(target_program.getFile() != null && target_program.getProject_name() != null){
	        		TableItem item;
	        		variable_viewer.getTable().removeAll();
	        		for(int i = 0; i < target_program.before_variable_info.size(); i++){
	        			VariableInfo temp = target_program.before_variable_info.get(i);
	        			int a_idx = target_program.after_variable_info.indexOf(temp);
	        			String variable = temp.getName();
	        			String type = temp.getType();
	        			String value = temp.getValue();
	        			if(a_idx != -1 && !value.equals("")){
	        				VariableInfo a_temp = target_program.after_variable_info.get(a_idx);
	        				value = value + " -> " + a_temp.getValue();
	        			}
	        			VariableInfo t = new VariableInfo(type, variable, value);

	        			item = new TableItem(variable_viewer.getTable(), SWT.NONE);

	        			item.setText(new String[] { variable, type, value });
	        		}
        		}
        		try {
            		//ファイルを開け、指定行にジャンプする
        		    FileOpen.jumpLineOnFile(target_program.getFile(), UseProgramChart.getProject_path(),new Integer(target_program.getLine()));
				}catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
        	}
    	});
        variable_viewer = new TableViewer(detail, SWT.FULL_SELECTION | SWT.V_SCROLL);
		// 2. TableViewerからTableを取得
		Table table = variable_viewer.getTable();
		// 3. Tableの罫線表示を可する
		table.setLinesVisible(true);
		// 4. Tableのヘッダー表示を可にする
		table.setHeaderVisible(true);
		// 5. ヘッダーのカラムに値をセット
		column = new TableColumn(table, SWT.LEFT, 0);
		column.setText("変数名");
		column.setWidth(100);
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("型名");
		column.setWidth(100);
		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("値");
		column.setWidth(100);
		//入力の読み込み
        try {
			UseProgramChart.gerenateProgramChart();
			if(UseProgramChart.p.size() != 0){
				viewer.setInput(UseProgramChart.p);
			}else{
				ArrayList<ProgramChart> p = new ArrayList<ProgramChart>();
				p.add(new ProgramChart("",0,"","No Result!",""));
				viewer.setInput(
					p
				);
			}
			error_info.setText(UseProgramChart.outputErrorStatement());
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public void setFocus() {

	}

}

class ProgramChartContentProvider implements IStructuredContentProvider {
	public Object[] getElements(Object inputElement) {
		return ((java.util.List<ProgramChart>)inputElement).toArray();
	}
	public void dispose() {
	}
	public void inputChanged(Viewer viewer,
			Object oldInput, Object newInput) {
	}
}

