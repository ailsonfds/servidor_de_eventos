/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event;

/**
 * @author Ailson Forte dos Santos
 *
 */
public class Server extends UnicastRemoteObject implements br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server {

	private static final long serialVersionUID = 1L;

	protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#createEvent(br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event)
	 */
	public Event createEvent(Event event) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#createEvent(java.lang.String, java.util.List, java.lang.String)
	 */
	public Event createEvent(String name, List<String> topics, String description) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#publishEvent(br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event)
	 */
	public boolean publishEvent(Event event) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#publishEvent(java.lang.String)
	 */
	public boolean publishEvent(String eventName) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#groupEvent(java.lang.String)
	 */
	public List<Event> groupEvent(String topic) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#groupEvent(java.util.List)
	 */
	public List<Event> groupEvent(List<String> topics) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#addInterestTopic(java.lang.String, java.lang.String)
	 */
	public boolean addInterestTopic(String userName, String topic) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#notifyEvent()
	 */
	public boolean notifyEvent() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = generateArgsOpts();
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = parser.parse(options, args);
			String ipAddress = cmd.getOptionValue("ip");
			
			ipAddress = (ipAddress.isEmpty()?"localhost":ipAddress);

			System.out.println(ipAddress);

			System.setProperty("java.rmi.server.hostname", "localhost");
			LocateRegistry.createRegistry(1900);
			Naming.rebind("rmi://" + ipAddress + ":1900/EventServer", new Server());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			System.exit(1);
		}
	}

	private static final Options generateArgsOpts() {
		Options options = new Options();
		Option ip = new Option("i", "ip", true, "Serve ip address");
		ip.setType(String.class);
		ip.setRequired(false);
		ip.setArgs(1);
		options.addOption(ip);

		return options;
	}

}
