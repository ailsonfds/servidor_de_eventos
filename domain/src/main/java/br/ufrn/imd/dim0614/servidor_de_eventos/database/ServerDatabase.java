package br.ufrn.imd.dim0614.servidor_de_eventos.database;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event;
import br.ufrn.imd.dim0614.servidor_de_eventos.classes.User;

/**
 * @author Franklin Lima
 *
 */
public class ServerDatabase {
	
	private List<Event> events;
	private List<User> users;
	
	public ServerDatabase() {
		this.events = new ArrayList<Event>();
		this.users = new ArrayList<User>();
	}

	/**
	 * @param event event to add.
	 * @return if was added.
	 */
	public boolean add(Event event) {
		synchronized (events) {
			return this.events.add(event);
		}
	}
	
	/**
	 * @param event to remove.
	 * @return if was removed.
	 */
	public boolean remove(Event event) {
		synchronized (events) {
			return this.events.remove(event);
		}
	}
	
	/**
	 * @param user to add.
	 * @return if was added.
	 */
	public boolean add(User user) {
		synchronized (users) {
			return this.users.add(user);
		}
	}
	
	/**
	 * @param user to remove.
	 * @return if was removed.
	 */
	public boolean remove(User user) {
		synchronized (users) {
			return this.users.remove(user);
		}
	}
	
	/**
	 * @return the events.
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set.
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}

	/**
	 * @return the users.
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users users to set.
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
