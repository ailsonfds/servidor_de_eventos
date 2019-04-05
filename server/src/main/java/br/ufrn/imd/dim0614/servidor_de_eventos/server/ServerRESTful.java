/**
 * 
 */
package br.ufrn.imd.dim0614.servidor_de_eventos.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;

import br.ufrn.imd.dim0614.servidor_de_eventos.classes.Event;
import br.ufrn.imd.dim0614.servidor_de_eventos.classes.User;
import br.ufrn.imd.dim0614.servidor_de_eventos.database.ServerDatabase;

/**
 * @author Ailson F. dos Santos
 *
 */
@Path("/live_server")
public class ServerRESTful extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	private ServerDatabase database;

	protected ServerRESTful() {
		this.database = new ServerDatabase();
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
	
	@GET
	@Path("/{user_id}/notifications")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject userHasNotifications(@PathParam("user_id") String userName) {
		JsonObject result = new JsonObject();
		result.addProperty("notificationsAmount", this.database.getUsers().get(userName).getNotifications().get(false).size());
		return result;
	}

	@POST
	@Path("/{user_id}/notifications/new")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonArray unreadNotifications(@PathParam("user_id") String userName) {
		JsonArray unreadReturn = new JsonArray();
		List<Event> unread = new ArrayList<>(this.database.getUsers().get(userName).getNotifications().get(false));
		for(Event event: unread) {
			JsonObject el = new JsonObject();
			el.addProperty("name", event.getName());
			el.addProperty("description", event.getDescription());
			JsonArray topics = new JsonArray();
			for(String topic : event.getTopics()) topics.add(topic);
			el.add("topics", topics);
			unreadReturn.add(el);
		}
		this.database.getUsers().get(userName).getNotifications().get(true).addAll(unread);
		this.database.getUsers().get(userName).getNotifications().get(false).clear();
		return unreadReturn;
	}

	@GET
	@Path("/{user_id}/notifications/old")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonArray readedNotifications(@PathParam("user_id") String userName) {
		JsonArray readed = new JsonArray();
		for(Event event: this.database.getUsers().get(userName).getNotifications().get(true)) {
			JsonObject el = new JsonObject();
			el.addProperty("name", event.getName());
			el.addProperty("description", event.getDescription());
			JsonArray topics = new JsonArray();
			for(String topic : event.getTopics()) topics.add(topic);
			el.add("topics", topics);
			readed.add(el);
		}
		return readed;
	}

	@POST
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response newUser(JsonObject payload) {
		User user = new GsonBuilder().create().fromJson(payload, User.class);
		if(this.database.add(user.getUserName(), user))
			return Response.ok().build();
		return Response.serverError().build();
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response loginUser(JsonObject payload) {
		String userName = payload.get("userName").getAsString();
		String password = payload.get("password").getAsString();
		if(this.database.getUsers().get(userName).isLogged())
			return Response.serverError().build();
		if(this.database.getUsers().get(userName).login(password)) {
			return Response.ok().build();
		}
		return Response.serverError().build();
	}

	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logoutUser(JsonObject payload) {
		String userName = payload.get("userName").getAsString();
		if(this.database.getUsers().get(userName).isLogged()) {
			this.database.getUsers().get(userName).logout();
			return Response.ok().build();
		}
		return Response.serverError().build();
	}

	public User lookup(String userName) {
		if(this.database.getUsers().containsKey(userName))
			return this.database.getUsers().get(userName);
		return null;
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonElement listEventsAsJson() {
		JsonArray  toReturn = new JsonArray();
		for(Event event : database.getEvents()) {
			JsonObject el = new JsonObject();
			el.addProperty("name", event.getName());
			el.addProperty("description", event.getDescription());
			JsonArray topics = new JsonArray();
			for(String topic : event.getTopics()) topics.add(topic);
			el.add("topics", topics);
			toReturn.add(el);
		}
		return toReturn;
	}

	@GET
	@Path("/str")
	@Produces(MediaType.TEXT_PLAIN)
	public String listEventsAsString() {
		String toReturn = "";
		for(Event event : database.getEvents()) toReturn += event.toString() + "\n";
		return toReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#publishEvent(br.
	 * ufrn.imd.dim0614.servidor_de_eventos.classes.Event)
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response publishEvent(JsonObject payload) {
		//		String name = payload.getString("name");
		//		String description = payload.getString("description");
		//		List<String> topics = payload.getJsonArray("topics").getValuesAs(String.class);
		Event event = new GsonBuilder().create().fromJson(payload, Event.class);
		this.database.getUsers().entrySet().forEach(item -> {
			for(String topic : event.getTopics())
				if(item.getValue().getInterestTopics().contains(topic))
					item.getValue().addNotification(event);
		});

		if(this.database.add(event))
			return Response.ok().build();
		return Response.serverError().build();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.ufrn.imd.dim0614.servidor_de_eventos.interfaces.Server#addInterestTopic(
	 * java.lang.String, java.lang.String)
	 */
	@POST
	@Path("/{user_id}/topics/{topic}")
	public Response addInterestTopic(@PathParam("user_id") String userName, @PathParam("topic") String topic) {
		if (database.getUsers().containsKey(userName)) {
			database.getUsers().get(userName).addInterestTopic(topic);
			return Response.ok().build();
		}
		return Response.serverError().build();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerRESTful server = new ServerRESTful();
	}

}
