/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server;

/**
 * @author Ailson Forte dos Santos
 *
 */
public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Server server = (Server) Naming.lookup("rmi://localhost:1900/EventServer");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

}
