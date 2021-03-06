package controllers;

import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.CREATED;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.routeAndCall;
import static play.test.Helpers.running;
import static play.test.Helpers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import play.api.mvc.AsyncResult;
import play.mvc.Result;
import play.test.FakeRequest;

public class LocationControllerTest {

	@Test
	public void testAddLocation() {
		running(fakeApplication(inMemoryDatabase()), new Runnable() {
			@Override
			public void run() {
				FakeRequest fakeRequest = fakeRequest(POST, "/location");
				Map<String, String> args = new HashMap<String, String>();
				args.put("userId", "_USER_ID_");
				args.put("lat", "12.0");
				args.put("lon", "12.0");
				args.put("isStart", "" + false);

				// some plumbing to wait synchronously for the result
				Result result = routeAndCall((fakeRequest
						.withFormUrlEncodedBody(args)));

				final AsyncResult asyncResult = (AsyncResult) result
						.getWrappedResult();

				Result syncResult = new Result() {

					@Override
					public play.api.mvc.Result getWrappedResult() {
						return asyncResult.result().await().get();
					}

				};

				assertThat(status(syncResult)).isEqualTo(CREATED);
			}
		});

	}

	@Test
	public void testArea() {
		running(fakeApplication(), new Runnable() {
			@Override
			public void run() {
				FakeRequest fakeRequest = fakeRequest(GET,
						"/location/area?minLat=43.0&maxLat=44.0&minLon=1.0&maxLon=2.0");
				Result result = routeAndCall(fakeRequest);
				assertThat(status(result)).isEqualTo(200);
			}
		});
	}

	@Test
	public void testAreaBadRequest() {
		running(fakeApplication(), new Runnable() {
			@Override
			public void run() {
				FakeRequest fakeRequest = fakeRequest(GET, "/location/area");
				Result result = routeAndCall(fakeRequest);
				assertThat(status(result)).isEqualTo(400);
			}
		});
	}

	@Test
	public void testBadAddLocation() {
		Result result = callAction(controllers.routes.ref.LocationController
				.addLocation());
		assertThat(status(result)).isEqualTo(400);
	}

	@Test
	public void testCenter() {
		Result result = callAction(controllers.routes.ref.LocationController
				.centerMap());
		assertThat(status(result)).isEqualTo(400);
	}

	@Test
	public void testBadCenter() {
		FakeRequest fakeRequest = fakeRequest(GET, "/location/center");
		Result result = routeAndCall(fakeRequest);
		assertThat(result).isNull();
	}
}
