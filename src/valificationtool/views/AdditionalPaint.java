package valificationtool.views;

import org.eclipse.swt.graphics.GC;

public class AdditionalPaint {
	public static void fillOvalWithOutLine(GC gc,int x, int y, int width, int height){
		gc.drawOval(x,y,width,height);
		gc.fillOval(x+1,y+1,width-1,height-1);
	}
}
