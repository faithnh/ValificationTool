package valificationtool.views;


import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;


public class SampleView extends ViewPart {
	//. Canvasオブジェクトと、GCオブジェクトを定義①
	private Canvas canvas = null;
	private GC gc = null;

	public SampleView() {
	}

	public void createPartControl(Composite parent) {
		//. 画面いっぱいに拡大②
		parent.setLayout( new FillLayout() );

		//. Canvas の取得③
		canvas = new Canvas( parent, SWT.NONE );
		canvas.setLayout( new FillLayout() );
		canvas.setBackground( new Color( canvas.getDisplay()
				, new RGB( 255, 255, 255 ) ) );

		//. GC の取得④
		gc = new GC( canvas );
	}

	public void drawChart(){

	}
}


