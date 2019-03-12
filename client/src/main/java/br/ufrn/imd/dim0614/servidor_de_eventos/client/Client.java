/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
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

			try {
				ipAddress = (ipAddress.isEmpty()?"localhost":ipAddress);
			} catch(NullPointerException e) {
				ipAddress = "localhost";
			}

			server = (Server) Naming.lookup("rmi://" + ipAddress + ":1900/EventServer");

			Scanner scanner = new Scanner(System.in);
			System.out.println("Insert your user name: ");
			final String userName = scanner.nextLine();

			final User user;
			if(server.lookup(userName) == null) {
				System.out.println("Hello! It's your first time here...");
				System.out.println("What's your name?");
				String name = scanner.nextLine();
				List<String> interests = readInterestTopics();
				user = new User(name, userName, interests);
				server.newUser(user);
			} else {
				user = server.lookup(userName);
				System.out.println("Welcome back, " + userName);
			}

			Thread thread;

			if(server.loginUser(userName)) {

				thread = new Thread(new Runnable() {
					
					public void run() {

						int oldValueNotifications = 0;

						while(true) {
							try {
								int temp = server.userHasNotifications(userName);
								if(temp != oldValueNotifications) {
									if(temp > 0)
										System.out.println("You have " + temp + " new notifications");
									oldValueNotifications = temp;		
								}

								Thread.sleep(1000);
							} catch (RemoteException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				thread.start();

				Integer option = -1;

				while (option != 0) {
					System.out.println("\n\n== EVENT SERVER ==");
					System.out.println("1 - Create event");
					System.out.println("2 - Add new interest topics");
					System.out.println("3 - See unread notifications");
					System.out.println("4 - See readed notifications");
					System.out.println("5 - See your interest topics");
					System.out.println("6 - See all events");
					System.out.println("0 - Exit");
					option = scanner.nextInt();
					scanner.nextLine();
					if(option == 1) {
						System.out.println("Name: ");
						String name = scanner.nextLine();

						List<String> topics = readInterestTopics();
						System.out.println("Description: ");
						String description = scanner.nextLine();

						Event event = new Event(name, topics, description);
						if(server.publishEvent(event)) System.out.println("Event added!");
					} else if(option == 2) {
						List<String> topics = readInterestTopics();
						for(String topic : topics) server.addInterestTopic(userName, topic);
					} else if(option == 3) {
						List<Event> unread = server.unreadNotifications(userName);
						for(Event event : unread) System.out.println(event);
					} else if(option == 4) {
						List<Event> readed = server.readedNotifications(userName);
						for(Event event : readed) System.out.println(event);
					} else if(option == 5) {
						System.out.println(server.lookup(userName).getInterestTopics());
					} else if(option == 6) {
						System.out.println(server.listEvents());
					}
				}
			} else {
				System.out.println("user already connected... exiting...");
			}
			
			server.logoutUser(userName);


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

	public static List<String> readInterestTopics() {
		Scanner scanner = new Scanner(System.in);
		String topic = "";
		List<String> topics = new ArrayList<String>();
		while(!topic.equals("!done")) {
			System.out.println("Inform your new interest and press <enter> (input '!done' when you're done): ");
			topic = scanner.nextLine();
			if(!topic.trim().isEmpty() && !topic.equals("!done"))
				topics.add(topic);
		}
		return topics;
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
