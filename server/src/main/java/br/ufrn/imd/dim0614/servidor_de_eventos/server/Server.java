/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event;
import br.ufrn.imd.dim0614.servidor_de_eventos.classes.User;
import br.ufrn.imd.dim0614.servidor_de_eventos.database.ServerDatabase;

/**
 * @author Ailson Forte dos Santos
 *
 */
public class Server extends UnicastRemoteObject implements br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server {

	private static final long serialVersionUID = 1L;

	private ServerDatabase database;

	protected Server() throws RemoteException {
		super();
		this.database = new ServerDatabase();
	}

	public Integer userHasNotifications(String userName) throws RemoteException {
		return this.database.getUsers().get(userName).getNotifications().get(false).size();
	}
	
	public List<Event> unreadNotifications(String userName) throws RemoteException {
		List<Event> unread = new ArrayList<>(this.database.getUsers().get(userName).getNotifications().get(false));
		this.database.getUsers().get(userName).getNotifications().get(true).addAll(unread);
		this.database.getUsers().get(userName).getNotifications().get(false).clear();
		return unread;
	}
	
	public List<Event> readedNotifications(String userName) throws RemoteException {
		List<Event> readed = this.database.getUsers().get(userName).getNotifications().get(true);
		return readed;
	}
	
	public boolean newUser(User user) throws RemoteException {
		return this.database.add(user.getUserName(), user);
	}
	
	public boolean loginUser(String userName) throws RemoteException {
		if(this.database.getUsers().get(userName).isLogged())
			return false;
		this.database.getUsers().get(userName).login();
		return true;
	}
	
	public boolean logoutUser(String userName) throws RemoteException {
		if(this.database.getUsers().get(userName).isLogged()) {
			this.database.getUsers().get(userName).logout();
			return true;
		}
		return false;
	}
	
	public User lookup(String userName) throws RemoteException {
		if(this.database.getUsers().containsKey(userName))
			return this.database.getUsers().get(userName);
		return null;
	}

	public String listEvents() throws RemoteException {
		return database.getEvents().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#publishEvent(br.
	 * ufrn.imd.dim0614.servidor_de_eventos.classes.Event)
	 */
	public boolean publishEvent(Event event) throws RemoteException {
		this.database.getUsers().entrySet().forEach(item -> {
			for(String topic : event.getTopics())
				if(item.getValue().getInterestTopics().contains(topic))
					item.getValue().addNotification(event);
		});
		
		return this.database.add(event);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#addInterestTopic(
	 * java.lang.String, java.lang.String)
	 */
	public boolean addInterestTopic(String userName, String topic) throws RemoteException {
		if (database.getUsers().containsKey(userName)) {
			database.getUsers().get(userName).addInterestTopic(topic);
			return true;
		}
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

			ipAddress = (ipAddress.isEmpty() ? "localhost" : ipAddress);

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

	/**
	 * @return org.apache.commons.cli.Options object with all required CLI options
	 */
	private static final Options generateArgsOpts() {
		Options options = new Options();
		Option ip = new Option("i", "ip", true, "Serve ip address");
		ip.setType(String.class);
		ip.setRequired(false);
		options.addOption(ip);

		return options;
	}

}
