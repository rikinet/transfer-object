package jp.rikinet.util.dto;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by kiminori on 15/05/03.
 */
public class SimpleDtoTest {
	private static class SimpleDto extends Root {
		private static final String CLASS_NAME = "SimpleDto";
		public SimpleDto() {
			super();
			jsonObj.put(DtoFactory.KEY_CLASS, CLASS_NAME);
		}
		public SimpleDto(JSONObject jobj) {
			super(jobj);
			jsonObj.put(DtoFactory.KEY_CLASS, CLASS_NAME);
		}
		public static String getClassName() {
			return CLASS_NAME;
		}
		public String getTitle() {
			return jsonObj.getString("title");
		}
		public long getAge() {
			return jsonObj.getLong("age");
		}
		public void setTitle(String title) {
			jsonObj.put("title", title);
		}
		public void setAge(long age) {
			jsonObj.put("age", age);
		}
	}

	@Before
	public void init() {
		DtoFactory.register(SimpleDto.class);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void badClassNameTest() {
		String source = "{\"_class_\":\"BadName\",\"title\":\"foobar\",\"age\":105}";
		SimpleDto dto = (SimpleDto) DtoFactory.deserialize(source);
		assertNull(dto);
	}

	@Test
	public void simpleClassTest() {
		String source = "{\"_class_\":\"SimpleDto\",\"title\":\"foobar\",\"age\":105}";
		SimpleDto dto = DtoFactory.deserialize(source);
		assertEquals("foobar", dto.getTitle());
		assertEquals(105, dto.getAge());
	}
}
