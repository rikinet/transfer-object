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
		public boolean getFlag() {
			return jsonObj.getBoolean("flag");
		}
		public double getRate() {
			return jsonObj.getDouble("rate");
		}
		public void setTitle(String title) {
			jsonObj.put("title", title);
		}
		public void setAge(long age) {
			jsonObj.put("age", age);
		}
		public void setFlag(boolean flag) {
			jsonObj.put("flag", flag);
		}
		public void setRate(double rate) {
			jsonObj.put("rate", rate);
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
		String source = "{\"_class_\":\"BadName\""
				+ ",\"title\":\"foobar\",\"age\":105"
				+ ", \"flag\": false, \"rate\": 1.25 }";
		SimpleDto dto = (SimpleDto) DtoFactory.deserialize(source);
		assertNull(dto);
	}

	@Test
	public void simpleClassTest() {
		String source = "{\"_class_\":\"SimpleDto\""
				+ ",\"title\":\"foobar\",\"age\":105"
				+ ", \"flag\": false, \"rate\": 1.25 }";
		SimpleDto dto = DtoFactory.deserialize(source);
		assertEquals("foobar", dto.getTitle());
		assertEquals(105, dto.getAge());
		assertFalse(dto.getFlag());
		assertEquals(1.25d, dto.getRate(), 0.01d);
	}

	@Test
	public void writeTest() {
		SimpleDto dto = new SimpleDto();
		dto.setTitle("new title");
		dto.setAge(42);
		dto.setFlag(true);
		dto.setRate(1.005d);
		String str = dto.toString();
		System.out.println(str);
		assertNotNull(str);
	}
}
