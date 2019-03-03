/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Ailson Forte dos Santos
 *
 */
public interface Server extends Remote {

	public boolean createEvent(String name, String topics, String description) throws RemoteException;
	public boolean publishEvent() throws RemoteException;
	public boolean groupEvent() throws RemoteException;
	public boolean addInterestTopic() throws RemoteException;
	public boolean notifyEvent() throws RemoteException;

}
