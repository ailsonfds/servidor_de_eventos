/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event;
import br.ufrn.imd.dim0614.servidor_de_eventos.classes.User;

/**
 * @author Ailson Forte dos Santos
 *
 */
public interface Server extends Remote {

	/**
	 * @param user
	 * @return
	 * @throws RemoteException
	 */
	public boolean newUser(User user) throws RemoteException;
	
	/**
	 * @param userName
	 * @return
	 * @throws RemoteException
	 */
	public boolean loginUser(String userName) throws RemoteException;
	
	/**
	 * @param userName
	 * @return
	 * @throws RemoteException
	 */
	public boolean logoutUser(String userName) throws RemoteException;
	
	/**
	 * @return the events
	 * @throws RemoteException
	 */
	public String listEvents() throws RemoteException;
	
	/**
	 * @param userName
	 * @return the user
	 * @throws RemoteException
	 */
	public User lookup(String userName) throws RemoteException;
	
	/**
	 * @param event
	 * @return a reference to the event created
	 * @throws RemoteException
	 */
	public Event createEvent(Event event) throws RemoteException;
	
	/**
	 * @param name
	 * @param topics
	 * @param description
	 * @return a reference to the event created
	 * @throws RemoteException
	 */
	public Event createEvent(String name, List<String> topics, String description) throws RemoteException;
	
	/**
	 * @param event
	 * @return true if successful
	 * @throws RemoteException
	 */
	public boolean publishEvent(Event event) throws RemoteException;
	
	/**
	 * @param eventName
	 * @return true if successful
	 * @throws RemoteException
	 */
	public boolean publishEvent(String eventName) throws RemoteException;
	
	/**
	 * @param topic
	 * @return list with events related to the topic parameter
	 * @throws RemoteException
	 */
	public List<Event> groupEvent(String topic) throws RemoteException;
	
	/**
	 * @param topic
	 * @return list with events related to the topics parameter
	 * @throws RemoteException
	 */
	public List<Event> groupEvent(List<String> topics) throws RemoteException;
	
	/**
	 * @param userName
	 * @param topic
	 * @return true if successful
	 * @throws RemoteException
	 */
	public boolean addInterestTopic(String userName, String topic) throws RemoteException;
	
	/**
	 * @return true if successful
	 * @throws RemoteException
	 */
	public boolean notifyEvent() throws RemoteException;

}
