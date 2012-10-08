package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Location extends Model {

	private static final long serialVersionUID = 1973445298197201545L;

	@SuppressWarnings("unused")
	@Id
	private Long id;

	@Column
	private boolean isStart = false;

	@Column(nullable = false)
	private Date serverDate = null;

	@Embedded
	private User user = null;

	@Column(nullable = false)
	protected Float lat = 0.0f;

	@Column(nullable = false)
	protected Float lon = 0.0f;

	public Location() {

		this.serverDate = new Date();
	}

	public Location(final User aUser, final Float aLat, final Float aLon,
			final Boolean aIsStart) {
		this.user = aUser;
		this.lat = aLat;
		this.lon = aLon;
		this.isStart = aIsStart;
		this.serverDate = new Date();
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(final boolean isStart) {
		this.isStart = isStart;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public Date getServerDate() {
		return serverDate;
	}

	public void setServerDate(final Date serverDate) {
		this.serverDate = serverDate;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(final Float x) {
		this.lat = x;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(final Float y) {
		this.lon = y;
	}

	private static Finder<Long, Location> finder = new Finder<Long, Location>(
			Long.class, Location.class);

	public static void saveLocation(final Location l) {
		l.save();
	}

	public static int locationsCount() {
		return finder.findRowCount();
	}

	public static List<Location> getBoundedLocations(final Float minLat,
			final Float maxLat, final Float minLon, final Float maxLon) {

		List<Location> locations = finder.where()
				.between("lat", minLat, maxLat).between("lon", minLon, maxLon)
				.orderBy("serverDate").findList();

		// TODO NDE: add the start points of each track

		return locations;

	}
}
