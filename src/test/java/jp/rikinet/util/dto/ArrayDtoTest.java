package jp.rikinet.util.dto;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/*
 * Created by kiminori on 15/05/05.
 */
public class ArrayDtoTest {
	@DtoType(ElementDto.CLASS_NAME)
	static class ElementDto extends Root {
		// 配列の要素
		static final String CLASS_NAME = "elem";
		public ElementDto() {
			super();
			jsonObj.put(DtoFactory.KEY_CLASS, CLASS_NAME);
		}
		public ElementDto(JSONObject jo) {
			super(jo);
			jsonObj.put(DtoFactory.KEY_CLASS, CLASS_NAME);
		}
		public String getName() {
			return jsonObj.getString("name");
		}
		public void setName(String name) {
			jsonObj.put("name,", name);
		}
	}
	@DtoType(OuterDto.CLASS_NAME)
	static class OuterDto extends Root {
		static final String CLASS_NAME = "outer";
		private DtoArray<ElementDto> member;
		private DtoArray flags;
		private DtoArray names;
		public OuterDto() {
			super();
			jsonObj.put(DtoFactory.KEY_CLASS, CLASS_NAME);
		}
		public OuterDto(JSONObject jo) {
			super(jo);
			jsonObj.put(DtoFactory.KEY_CLASS, CLASS_NAME);

		}
		public DtoArray<ElementDto> getMember() {
			return member;
		}
		public void setMember(DtoArray<ElementDto> member) {
			this.member = member;
		}
		public DtoArray getFlags() {
			return flags;
		}
		public void setFlags(DtoArray flags) {
			this.flags = flags;
		}
		public DtoArray getNames() {
			return names;
		}
		public void setNames(DtoArray names) {
			this.names = names;
		}
	}

	@Before
	public void init() {
		DtoFactory.register(ElementDto.class);
		DtoFactory.register(OuterDto.class);
	}
	@Test
	public void objectArrayTest() {
		String source = "{ \"_class_\": \"outer\""
				+ ", \"member\": ["
				+ "{ \"_class_\": \"elem\", \"name\": \"foo\" }"
				+ ", { \"_class_\": \"elem\", \"name\": \"bar\" }"
				+ ", { \"_class_\": \"elem\", \"name\": \"baz\" } ]"
				+ ", \"flags\": [ true, false, true, false ]"
				+ ", \"names\": [ \"foo\", \"bar\", \"baz\" ] }";
		OuterDto dto = DtoFactory.deserialize(source);
		assertNotNull(dto);
		DtoArray<ElementDto> mem = dto.getMember();
		assertNotNull(mem);
		assertEquals(3, mem.size());
		assertEquals("baz", mem.get(2).getName());
	}
}
