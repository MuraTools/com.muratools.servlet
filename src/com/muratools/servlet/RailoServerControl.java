/**
 * 
 */
package com.muratools.servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

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
