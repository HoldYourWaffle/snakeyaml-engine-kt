package nl.rdd.snakeyamlkt


//TEST perhaps an end-to-end test could be useful
//	Individual unit-tests are useless because the only "userland" thing is the casting, which is already tested


/** A wrapped YAML value with helpers for type-safe casting */
data class YamlObject(
	val value: Any?
) {

	/** @see YamlMap */
	inline fun <reified K> asMap(): YamlMap<K> {
		return YamlMap((value as Map<*, *>).castMap(valueTransformer = ::YamlObject));
	}

	/**
	 * @see asMap
	 * @see YamlMap.raw
	 */
	inline fun <reified K> asRawMap() = asMap<K>().raw();


	/** @see YamlList */
	fun asList(): YamlList {
		return YamlList((value as List<*>).castList(::YamlObject));
	}

	/**
	 * @see asList
	 * @see YamlList.raw
	 */
	fun asRawList() = asList().raw();


	inline fun <reified T: Enum<T>> asEnum() = enumValueOf<T>(value as String);

}


/**
 * Base class for collections of [YamlObject]'s.
 * Contains type-safe get methods for primitives, enums, lists and maps.
 *
 * Implementations only need to provide a [get] methods.
 * The type-safe methods cast the result of this method to the desired type.
 *
 * @see YamlList
 * @see YamlMap
 */
abstract class YamlCollection<K> {

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

	inline fun <reified mK> getMap(key: K): YamlMap<mK>? = get(key)?.asMap();
	fun getList(key: K): YamlList? = get(key)?.asList();

}


/**
 * Type-safe wrapper for a map in a YAML document
 * @see YamlCollection
 */
class YamlMap<K>(
	map: Map<K, YamlObject>
): Map<K, YamlObject> by map, YamlCollection<K>() {

	/**
	 * @return A raw map of all objects contained in this YAML-map
	 * @see castMap
	 */
	fun raw(): Map<K, Any?> = mapValues { it.value.value }


	//XXX there's no distinction between "no key" and "key with null value", does this matter?
	fun require(key: K, errorMessage: String? = null) = get(key) ?: throw NoSuchElementException(errorMessage ?: "No $key definition available");

	fun requireInt(key: K, errorMessage: String? = null) = require(key, errorMessage).value as Int;
	fun requireLong(key: K, errorMessage: String? = null) = require(key, errorMessage).value as Long;
	fun requireByte(key: K, errorMessage: String? = null) = require(key, errorMessage).value as Byte;
	fun requireShort(key: K, errorMessage: String? = null) = require(key, errorMessage).value as Short;
	fun requireDouble(key: K, errorMessage: String? = null) = require(key, errorMessage).value as Double;
	fun requireFloat(key: K, errorMessage: String? = null) = require(key, errorMessage).value as Float;
	fun requireString(key: K, errorMessage: String? = null) = require(key, errorMessage).value as String;
	fun requireChar(key: K, errorMessage: String? = null) = require(key, errorMessage).value as Char;
	fun requireBoolean(key: K, errorMessage: String? = null) = require(key, errorMessage).value as Boolean

	inline fun <reified T: Enum<T>> requireEnum(key: K, errorMessage: String? = null) = require(key, errorMessage).asEnum<T>();
	inline fun <reified mK> requireMap(key: K, errorMessage: String? = null) = require(key, errorMessage).asMap<mK>();
	fun requireList(key: K, errorMessage: String? = null) = require(key, errorMessage).asList();

}


/**
 * Type-safe wrapper for a list in a YAML document
 * @see YamlCollection
 */
@Suppress("DIFFERENT_NAMES_FOR_THE_SAME_PARAMETER_IN_SUPERTYPES") // it's very unlikely that you're going to call list.get(i) with named arguments
class YamlList(
	list: List<YamlObject>
): List<YamlObject> by list, YamlCollection<Int>() {

	/**
	 * @return A raw list of all objects contained in this YAML-list
	 * @see castList
	 */
	fun raw(): List<Any?> = map { it.value }

}