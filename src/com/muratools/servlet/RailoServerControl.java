/**
 * 
 */
package com.muratools.servlet;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.osgi.framework.Bundle;

/**
 * @author CHASXG
 *
 */
public class RailoServerControl {
	
	public static int DEFAULT_PORT = 8675;
	public static String CONSOLE_NAME = "Mura Tools";
	
	private int port;
	private Server server;
	private WebAppContext context;
	private MessageConsole console;
	private IWorkbenchPage page;
	
	public RailoServerControl(){
		setPort(DEFAULT_PORT);
	}
	
	public RailoServerControl(int port) {
		setPort((port <= 0) ? DEFAULT_PORT : port);
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	private void initConsole(){
		console = findConsole(CONSOLE_NAME);
		page = findPage();
		
		String id = IConsoleConstants.ID_CONSOLE_VIEW;
		try {
			IConsoleView view = (IConsoleView) page.showView(id);
			view.display(console);
		}
		catch (PartInitException e) {
			e.printStackTrace();
		}
		
	}
	
	public void initServer(String docBase) throws Exception{
		initConsole();
		
		deployRailo(docBase);
		
		println("Creating Server...");
		server = new Server(getPort());
		
		println("Creating context...");
		//context = new WebAppContext();
		println("Setting context descriptor to '/WEB-INF/web.xml'");
		//context.setDescriptor("/WEB-INF/web.xml");
		println("Setting Resource Base (docBase) to: '" + docBase + "'");
		//context.setResourceBase(docBase);
		println("Setting context path to '/'");
		//context.setContextPath("/");
		//context.setParentLoaderPriority(true);
		
		println("Setting the server's handler to the context");
		//server.setHandler(context);
		
		println("Starting the server...");
		//server.start();
		//server.join();
	}
	
	private void deployRailo(String target){
		String webinf = "RAILO-WEB-INF";
		try {
			// Check to see if this directory contains a WEB-INF
			// Do nothing if it does, otherwise deploy the war file
			File file = new File(target + "/WEB-INF");
			if (!file.exists()){
				String pathToWar = getInstallLocation() + "static";
				println("Deploying Railo WEB-INF...");
				File war = new File(pathToWar + "/" + webinf);
				FileUtils.copyDirectory(war, new File(target + "/WEB-INF"));
			} else {
				println("Skipping Railo WEB-INF deployment...\n\tWEB-INF already exists in target...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getInstallLocation() throws Exception {
		Bundle bundle = Platform.getBundle("com.muratools.servlet");
		URL locationUrl = FileLocator.find(bundle, new Path("/"), null);
		URL fileUrl = FileLocator.toFileURL(locationUrl);
		return fileUrl.getFile();
	}
	
	public void stopServer() throws Exception {
		stopServer(false);
	}
	
	public void stopServer(boolean restart) throws Exception {
		if (server != null && server.isRunning()){
			server.stop();
			if (restart){
				server.start();
				server.join();
			}
		}
	}
	
	private void println(String message){
		String ts = new Date().toString() + " :: ";
		console.newMessageStream().println(ts + message);
	}
	
	private IWorkbenchPage findPage(){
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		return win.getActivePage();
	}
	
	private MessageConsole findConsole(String name) {
	      ConsolePlugin plugin = ConsolePlugin.getDefault();
	      IConsoleManager conMan = plugin.getConsoleManager();
	      IConsole[] existing = conMan.getConsoles();
	      for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	      //no console found, so create a new one
	      MessageConsole myConsole = new MessageConsole(name, null);
	      conMan.addConsoles(new IConsole[]{myConsole});
	      return myConsole;
	   }
}
