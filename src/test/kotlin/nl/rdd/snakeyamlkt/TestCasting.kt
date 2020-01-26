package nl.rdd.snakeyamlkt

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.lang.ClassCastException


class TestCasting {
	
	@Test
	fun cleanList() {
		assert(testList<Int>(listOf(2, 4, 5, 1))); // clean non-nullable
		assert(testList<Int?>(listOf(2, 4, null, 1))); // clean nullable

		assert( // clean customly transformed
			testList<Int>(listOf(2, 4, "5", 1)) { Integer.valueOf(it.toString()) }
		)
	}
	
	@Test
	fun pollutedList() {
		assertFalse(testList<Int>(listOf(2, 4, "bruh", 1))); // mixed types
		assertFalse(testList<Int>(listOf(2, 4, null, 1))); // illegal nullable
	}
	
	
	@Test
	fun cleanMap() {
		assert( // clean non-nullable
			testMap<String, Int>(mapOf(
				"a" to 2,
				"b" to 3
			))
		)

		assert( // clean nullable
			testMap<String, Int?>(mapOf(
				"a" to 2,
				"b" to null
			))
		)


		assert( // clean customly key-transformed
			testMap<String, Int>(
				mapOf(
					"a" to 2,
					5 to 3
				),
				keyTransformer = { k -> k.toString() }
			)
		)

		assert( // clean customly value-transformed
			testMap<String, Int>(
				mapOf(
					"a" to 2,
					"b" to "3"
				),
				valueTransformer = { Integer.parseInt(it.toString()) }
			)
		)
	}
	
	@Test
	fun pollutedMapKeys() {
		assertFalse( // mixed types
			testMap<String, Int>(mapOf(
				"a" to 2,
				5 to 3
			))
		)

		assertFalse( // illegal nullable
			testMap<String, Int>(mapOf(
				"a" to 2,
				null to 3
			))
		)
	}
	
	@Test
	fun pollutedMapValues() {
		assertFalse( // mixed types
			testMap<String, Int>(mapOf(
				"a" to 2,
				"b" to "bruh"
			))
		)

		assertFalse( // illegal nullable
			testMap<String, Int>(mapOf(
				"a" to 2,
				"b" to null
			))
		)
	}


	
	/** @return if the cast was successful */
	private inline fun <reified E> testList(
		list: List<*>,
		transformer: (Any?) -> E = { it as E }
	): Boolean {
		return try {
			list.castList(transformer);
			true;
		} catch(e: ClassCastException) {
			false;
		}
	}

	/** @return if the cast was successful */
	private inline fun <reified K, reified V> testMap(
		map: Map<Any?, Any?>,
		//FIXME noinline because of weird reified bug(?)
		noinline keyTransformer: (Any?) -> K = { it as K },
		noinline valueTransformer: (Any?) -> V = { it as V }
	): Boolean {
		return try {
			map.castMap(keyTransformer, valueTransformer);
			true;
		} catch(e: ClassCastException) {
			false;
		}
	}
	
}