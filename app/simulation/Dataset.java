package simulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Location;
import models.User;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.libs.Json;

public class Dataset {

	private static String dataset;

	protected static JsonNode loadDataset() throws IOException {
		if (dataset == null) {
			dataset = IOUtils.toString(Dataset.class
					.getResourceAsStream("/simulation/dataset.json"));
		}

		return Json.parse(dataset);

	}

	public static List<Location> findLocations(final String userId) {

		List<Location> result = new ArrayList<Location>();

		User user = new User(userId);

		try {
			JsonNode dataForUser = loadDataset().findValue(userId);
			if (dataForUser == null) {
				return null;
			}
			Iterator<JsonNode> locations = dataForUser.getElements();
			while (locations.hasNext()) {
				Location location = Json.fromJson(locations.next(),
						Location.class);
				location.setUser(user);
				location.setIsStart(false);
				result.add(location);
			}
			// the first element is the starting one
			if (!result.isEmpty()) {
				result.get(0).setIsStart(true);
			}

		} catch (IOException e) {
			Logger.error("Unable to load simulation dataset", e);
			return null;
		}

		return result;
	}

}