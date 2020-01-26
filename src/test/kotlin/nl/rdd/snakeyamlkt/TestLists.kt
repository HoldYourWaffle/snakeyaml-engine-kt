package nl.rdd.snakeyamlkt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class TestLists {

	val listStr = fixture[2].asList();
	val listInt = fixture[3].asList();

	@Test
	fun safe() {
		assertEquals(listStr.getString(0), "a");
		assertEquals(listInt.getInt(0), 1);
	}

	@Test
	fun unsafe() {
		// Not a list
		assertThrows<ClassCastException> { fixture[0].asList() }

		// Unsafe getting
		assertThrows<ClassCastException> { listInt.getString(0) }
		assertThrows<ClassCastException> { listStr.getInt(0) }
	}

	@Test
	fun outOfBounds() {
		assertThrows<IndexOutOfBoundsException> { listInt[28] }
	}

	@Test
	fun structure() {
		assertEquals(listStr.raw(), listOf("a"));
		assertEquals(listInt.raw(), listOf(1));
	}

}