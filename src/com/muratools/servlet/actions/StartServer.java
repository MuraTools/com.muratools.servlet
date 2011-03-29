package com.muratools.servlet.actions;

import org.eclipse.jface.action.IAction;
import com.muratools.eclipse.MuraToolsAction;
import com.muratools.servlet.RailoServerControl;

public class StartServer extends MuraToolsAction {
	
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {		
		RailoServerControl rsc = new RailoServerControl();
		try {
			rsc.initServer(getTargetDirectory());
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}
