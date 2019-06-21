package br.ufrn.imd.dim0614.servidor_de_eventos.interfaces;

import java.util.List;

import br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event;
import br.ufrn.imd.dim0614.servidor_de_eventos.classes.User;

import java.rmi.Remote;

public interface Client extends Remote {

	/**
	 * @param event
	 * @return
	 */
	public boolean createEvent(Event event);

	/**
	 * @param event
	 * @return
	 */
	public boolean createEvent(String name, List<String> topics, String description);

	/**
	 * @param user
	 * @return
	 */
	public boolean addInterestTopic(User user, String topic);

	/**
	 * @return
	 */
	public User createUser(String userName, String name, List<String> interests);

	/**
	 * @param user
	 * @return
	 */
	public List<Event> checkNotifications(User user);

}
