package nl.rdd.snakeyamlkt

import java.lang.NullPointerException


//TEST getting each collection (no-col [cce], clean, polluted(key+value) [cce])
//TEST requiring each collection (no-col [cce], clean, polluted(key+value) [cce], not found [nsee])


fun YamlMap.getMap(key: String): YamlMap? =
	(this[key] as Map<*, *>?)?.castMap(
		keyTransformer = { k ->
			if (k == null)
				throw NullPointerException("'null' key in map $key");

			when (k) {
				is String -> k
				is Int -> k.toString()
				else -> throw ClassCastException("Non-string/int key: $k (${k.javaClass.simpleName}");
			}
		}
	)

inline fun <reified E> YamlMap.getList(key: String) =
	(this[key] as List<*>?)?.castList<Any?, E>();

inline fun <reified E> YamlMap.getSet(key: String) =
	getList<E>(key)?.toSet(); //CHECK does snakeyaml-engine ever actually output a Set?



fun YamlMap.requireMap(key: String, errorMessage: String? = null) =
	getMap(key) ?: throwNSEE(key, errorMessage);

inline fun <reified E> YamlMap.requireList(key: String, errorMessage: String? = null) =
	getList<E>(key) ?: throw NoSuchElementException(errorMessage ?: "No $key definition available"); // can't use throwNSEE because of private inlining

inline fun <reified E> YamlMap.requireSet(key: String, errorMessage: String? = null) =
	getSet<E>(key) ?: throw NoSuchElementException(errorMessage ?: "No $key definition available"); // can't use throwNSEE because of private inlining
