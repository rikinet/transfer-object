package jp.rikinet.util.dto;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by kiminori on 15/05/02.
 */
public class RootTest {

	@Test
	public void newObject1() {
		Root obj = new Root();
		assertEquals("{\"_class_\":\"_root_\"}", obj.toString());
	}

	@Test
	public void newObject2() {
		JSONObject jo = new JSONObject();
		Root dto = new Root(jo);
		assertEquals("{\"_class_\":\"_root_\"}", dto.toString());
	}
}
