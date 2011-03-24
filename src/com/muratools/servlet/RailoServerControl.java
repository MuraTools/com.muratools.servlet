/**
 * 
 */
package com.muratools.servlet;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.osgi.framework.Bundle;

/**
 * @author CHASXG
 *
 */
public class RailoServerControl {
	
	public static int DEFAULT_PORT = 8080;
	
	private int port;
	private Server server;
	private WebAppContext context;
	
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

	public void initServer(String docBase) throws Exception{
		deployRailo(docBase);
		
		println("Creating Server...");
		server = new Server(port);
		
		println("Creating context...");
		context = new WebAppContext();
		println("Setting context descriptor to '/WEB-INF/web.xml'");
		context.setDescriptor("/WEB-INF/web.xml");
		println("Setting Resource Base (docBase) to: '" + docBase + "'");
		context.setResourceBase(docBase);
		println("Setting context path to '/'");
		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		
		println("Setting the server's handler to the context");
		server.setHandler(context);
		
		println("Starting the server...");
		server.start();
		server.join();
	}
	
	private void deployRailo(String target){
		String webinf = "RAILO-WEB-INF";
		try {
			// Check to see if this directory contains a WEB-INF
			// Do nothing if it does, otherwise deploy the war file
			File file = new File(target + "/WEB-INF");
			if (!file.exists()){
				String pathToWar = getInstallLocation() + "static";
				println("Deploying " + pathToWar + "/" + webinf + " to " + target + "/WEB-INF");
				File war = new File(pathToWar + "/" + webinf);
				FileUtils.copyDirectory(war, new File(target + "/WEB-INF"));
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
		System.out.println(message);
	}
	
}
