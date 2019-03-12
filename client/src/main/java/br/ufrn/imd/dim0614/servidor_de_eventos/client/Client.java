/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event;
import br.ufrn.imd.dim0614.servidor_de_eventos.classes.User;
import br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server;

/**
 * @author Ailson Forte dos Santos
 *
 */
public class Client implements br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Client {
	
	private static Server server;

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Client#createEvent(br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event)
	 */
	public boolean createEvent(Event event) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Client#createEvent(java.lang.String, java.util.List, java.lang.String)
	 */
	public boolean createEvent(String name, List<String> topics, String description) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Client#addInterestTopic(br.ufrn.imd.dim0614.servidor_de_eventos.classes.User, java.lang.String)
	 */
	public boolean addInterestTopic(User user, String topic) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Client#createUser(java.lang.String, java.lang.String, java.util.List)
	 */
	public User createUser(String userName, String name, List<String> interests) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Client#checkNotifications(br.ufrn.imd.dim0614.servidor_de_eventos.classes.User)
	 */
	public List<Event> checkNotifications(User user) {
		// TODO Auto-generated method stub
		return null;
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
			
			server = (Server) Naming.lookup("rmi://" + ipAddress + ":1900/EventServer");
			
			Scanner scanner = new Scanner(System.in);
			System.out.println("Insert your user name: ");
			String userName = scanner.nextLine();
			User user = server.lookup(userName);
			if(user == null) {
				System.out.println("Hello! It's your first time here...");
				System.out.println("What's your name?");
				String name = scanner.nextLine();
				System.out.println("What's your interest topics? (write them separated by a single space)");
				List<String> interests = Arrays.asList(scanner.nextLine().split(" "));
				user = new User(name, userName, interests);
				server.newUser(user);
			} else {
				System.out.println("Welcome back, " + userName);
			}
			
			server.loginUser(userName);
			System.out.println("== EVENT SERVER ==");
			Integer option = 0;
			
			while (option != 4) {
				System.out.println("1 - Create event");
				System.out.println("4 - Exit");
				option = scanner.nextInt();
				scanner.nextLine();
				if(option == 1) {
					System.out.println("Name: ");
					String name = scanner.nextLine();
					System.out.println("Topics: ");
					List<String> topics = Arrays.asList(scanner.nextLine().split(" "));
					System.out.println("Description: ");
					String description = scanner.nextLine();
					
					Event event = new Event(name, topics, description);
					System.out.println(event.toString());
					server.publishEvent(event);
					System.out.println(server.listEvents());
				}
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			e.printStackTrace();
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
