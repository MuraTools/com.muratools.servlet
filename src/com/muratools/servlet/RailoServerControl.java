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
		server = new Server(port);
		
		context = new WebAppContext();
		context.setDescriptor("/WEB-INF/web.xml");
		context.setResourceBase(docBase);
		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		
		server.setHandler(context);
		
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
	
}
