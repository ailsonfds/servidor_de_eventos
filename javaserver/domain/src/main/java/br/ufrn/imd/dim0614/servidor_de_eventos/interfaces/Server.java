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

	public Integer userHasNotifications(String userName) throws RemoteException;
	
	public List<Event> unreadNotifications(String userName) throws RemoteException;
	
	public List<Event> readedNotifications(String userName) throws RemoteException;
	
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
	 * @return true if successful
	 * @throws RemoteException
	 */
	public boolean publishEvent(Event event) throws RemoteException;
	
	/**
	 * @param userName
	 * @param topic
	 * @return true if successful
	 * @throws RemoteException
	 */
	public boolean addInterestTopic(String userName, String topic) throws RemoteException;
	
}
