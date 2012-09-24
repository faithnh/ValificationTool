package valificationtool.util;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
/**
 * Consoleにログを出力
 *
 *
 * @author openforce
 *
 */
public class Console {

	private MessageConsole myConsole;

	public Console(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				myConsole = (MessageConsole) existing[i];
		//no console found, so create a new one
		myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[]{myConsole});
	}


	public void out(String message)
	{
		MessageConsoleStream out = myConsole.newMessageStream();
		out.println(message);
	}
}