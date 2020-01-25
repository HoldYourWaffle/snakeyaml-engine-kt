package nl.rdd.snakeyamlkt


//TEST perhaps an end-to-end test could be useful
//	Individual unit-tests are useless because the only "userland" thing is the casting, which is already tested


/** A wrapped YAML value with helpers for type-safe list & map casting */
class YamlObject(
	val value: Any?
) {

	inline fun <reified K> asMap(): YamlMap<K> {
		return YamlMap((value as Map<*, *>).castMap(valueTransformer = ::YamlObject));
	}

	fun asList(): YamlList {
		return YamlList((value as List<*>).castList(::YamlObject));
	}

	inline fun <reified T: Enum<T>> asEnum() = enumValueOf<T>(value as String);

}


/**
 * Base class for collections of [YamlObject]'s.
 * Contains type-safe get & require methods for primitives, enums, lists and maps.
 *
 * Implementations only need to provide a [get] methods.
 * The type-safe methods cast the result of this method to the desired type.
 *
 * @see YamlList
 * @see YamlMap
 */
abstract class YamlCollection<K> {

	//XXX there's no distinction between "no key" and "key with null value", does this matter?
	abstract fun get(key: K): YamlObject?;



	fun getInt(key: K) = get(key)?.value as Int?;
	fun getLong(key: K) = get(key)?.value as Long?;
	fun getByte(key: K) = get(key)?.value as Byte?;
	fun getShort(key: K) = get(key)?.value as Short?;
	fun getDouble(key: K) = get(key)?.value as Double?;
	fun getFloat(key: K) = get(key)?.value as Float?;
	fun getString(key: K) = get(key)?.value as String?;
	fun getChar(key: K) = get(key)?.value as Char?;
	fun getBoolean(key: K) = get(key)?.value as Boolean?;

	inline fun <reified T: Enum<T>> getEnum(key: K): T? {
		val str = getString(key);
		return if (str == null) {
			null;
		} else {
			enumValueOf<T>(str);
		}
	}


	fun requireInt(key: K, errorMessage: String? = null) = getInt(key) ?: throwNSEE(key, errorMessage);
	fun requireLong(key: K, errorMessage: String? = null) = getLong(key) ?: throwNSEE(key, errorMessage);
	fun requireByte(key: K, errorMessage: String? = null) = getByte(key) ?: throwNSEE(key, errorMessage);
	fun requireShort(key: K, errorMessage: String? = null) = getShort(key) ?: throwNSEE(key, errorMessage);
	fun requireDouble(key: K, errorMessage: String? = null) = getDouble(key) ?: throwNSEE(key, errorMessage);
	fun requireFloat(key: K, errorMessage: String? = null) = getFloat(key) ?: throwNSEE(key, errorMessage);
	fun requireString(key: K, errorMessage: String? = null) = getString(key) ?: throwNSEE(key, errorMessage);
	fun requireChar(key: K, errorMessage: String? = null) = getChar(key) ?: throwNSEE(key, errorMessage);
	fun requireBoolean(key: K, errorMessage: String? = null) = getBoolean(key) ?: throwNSEE(key, errorMessage)

	inline fun <reified T: Enum<T>> requireEnum(key: K, errorMessage: String? = null) =
		getEnum<T>(key) ?: throw NoSuchElementException(errorMessage ?: "No $key definition available"); // can't use throwNSEE because of private inlining



	inline fun <reified mK> getMap(key: K): YamlMap<mK>? = get(key)?.asMap();
	inline fun <reified E> getList(key: K): YamlList? = get(key)?.asList();


	inline fun <reified mK> requireMap(key: K, errorMessage: String? = null) =
		getMap<mK>(key) ?: throw NoSuchElementException(errorMessage ?: "No $key definition available"); // can't use throwNSEE because of private inlining

	inline fun <reified E> requireList(key: K, errorMessage: String? = null) =
		getList<E>(key) ?: throw NoSuchElementException(errorMessage ?: "No $key definition available"); // can't use throwNSEE because of private inlining



	private fun throwNSEE(key: K, errorMessage: String?) {
		//XXX why can't I publicily inline private inline functions?
		throw NoSuchElementException(errorMessage ?: "No $key definition available");
	}
	
}


/**
 * Type-safe wrapper for a map in a YAML document
 * @see YamlCollection
 */
class YamlMap<K>(
	map: Map<K, YamlObject>
): Map<K, YamlObject> by map, YamlCollection<K>();


/**
 * Type-safe wrapper for a list in a YAML document
 * @see YamlCollection
 */
@Suppress("DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES") // it's very unlikely that you're going to call list.get(i) with named arguments
class YamlList(
	list: List<YamlObject>
): List<YamlObject> by list, YamlCollection<Int>();