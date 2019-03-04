/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event;

/**
 * @author Ailson Forte dos Santos
 *
 */
public interface Server extends Remote {

	public Event createEvent(Event event) throws RemoteException;
	public Event createEvent(String name, List<String> topics, String description) throws RemoteException;
	public boolean publishEvent(Event event) throws RemoteException;
	public boolean publishEvent(String eventName) throws RemoteException;
	public List<Event> groupEvent(String topic) throws RemoteException;
	public List<Event> groupEvent(List<String> topic) throws RemoteException;
	public boolean addInterestTopic(String userName, String topic) throws RemoteException;
	public boolean notifyEvent() throws RemoteException;

}
