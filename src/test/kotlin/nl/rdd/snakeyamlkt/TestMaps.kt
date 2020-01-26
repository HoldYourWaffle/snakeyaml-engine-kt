package nl.rdd.snakeyamlkt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.Exception


class TestMaps {

	val mapStr = fixture[0].asMap<String>();
	val mapInt = fixture[1].asMap<Int>();


	@Test
	fun safe() {
		assertEquals(mapStr.getInt("a"), 1);
		assertEquals(mapInt.getString(1), "a");
	}

	@Test
	fun unsafe() {
		// Unsafe key-casting
		assertThrows<ClassCastException> { fixture[0].asMap<Int>() }
		assertThrows<ClassCastException> { fixture[1].asMap<String>() }

		// Safe key-casting, unsafe getting
		assertThrows<ClassCastException> { mapStr.getString("a") }
		assertThrows<ClassCastException> { mapInt.getInt(1) }
	}

	@Test
	fun required() {
		assertThrows<NoSuchElementException> { mapStr.require("404") }
	}

	@Test
	fun structure() {
		assertEquals(mapStr.raw(), mapOf("a" to 1));
		assertEquals(mapInt.raw(), mapOf(1 to "a"));
	}

}