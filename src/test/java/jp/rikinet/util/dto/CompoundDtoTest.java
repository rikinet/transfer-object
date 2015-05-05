package jp.rikinet.util.dto;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by kiminori on 15/05/04.
 */
public class CompoundDtoTest {
	private static class CompoundSub extends Root {
		/* { "name": "foobar", "age": 105 } */
		public CompoundSub() {
			super();
			jsonObj.put(DtoFactory.KEY_CLASS, "CompoundSub");
		}
		public CompoundSub(JSONObject jObj) {
			super(jObj);
			jsonObj.put(DtoFactory.KEY_CLASS, "CompoundSub");
		}
		public static String getClassName() {
			return "CompoundSub";
		}
		public String getName() {
			return jsonObj.getString("name");
		}
		public void setName(String name) {
			jsonObj.put("name", name);
		}
		public long getAge() {
			return jsonObj.getInt("age");
		}
		public void setAge(long age) {
			jsonObj.put("age", age);
		}
	}
	private static class CompoundRoot extends Root {
		/*
		 * { "from": { "name": "alice", "age": 33 },
		 *   "to": { "name": "bob", "age": 31 } }
		 */
		private CompoundSub from;
		private CompoundSub to;
		public CompoundRoot() {
			super();
			jsonObj.put(DtoFactory.KEY_CLASS, "CompoundRoot");
		}
		public CompoundRoot(JSONObject jObj) {
			super(jObj);
			jsonObj.put(DtoFactory.KEY_CLASS, "CompoundRoot");

		}
		public static String getClassName() {
			return "CompoundRoot";
		}
		public CompoundSub getFrom() {
			return this.from;
		}
		public CompoundSub getTo() {
			return this.to;
		}
		public void setFrom(CompoundSub fromDto) {
			this.from = fromDto;
			jsonObj.put("from", fromDto.jsonObj);
		}
		public void setTo(CompoundSub toDto) {
			this.to = toDto;
			jsonObj.put("to", toDto.jsonObj);
		}
	}

	@Before
	public void init() {
		try {
			DtoFactory.register(CompoundRoot.class);
			DtoFactory.register(CompoundSub.class);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void createTest() {
		String source = "{ \"_class_\": \"CompoundRoot\""
				+ ", \"from\": { \"_class_\": \"CompoundSub\", \"name\": \"alice\", \"age\": 33 }"
				+ ", \"to\": { \"_class_\": \"CompoundSub\", \"name\": \"bob\", \"age\": 31 } }";
		CompoundRoot root = DtoFactory.deserialize(source);
		assertNotNull(root);
		assertEquals("alice", root.getFrom().getName());
		assertEquals(31, root.getTo().getAge());
	}

	@Test
	public void writeTest() {
		CompoundRoot root = new CompoundRoot();
		CompoundSub subFrom = new CompoundSub();
		subFrom.setName("cyndi");
		subFrom.setAge(41);
		CompoundSub subTo = new CompoundSub();
		subTo.setName("dave");
		subTo.setAge(43);
		root.setFrom(subFrom);
		root.setTo(subTo);
		String str = root.toString();
		System.out.println(str);
		assertNotNull(str);
	}
}
