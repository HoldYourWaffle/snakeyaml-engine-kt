package nl.rdd.snakeyamlkt


//TODO pretty sure there are more primitives
//TEST getting each primitive (correct type & incorrect type [cce])
//TEST requiring each primitive (correct type, incorrect type [cce] & not found [nsee])


fun YamlMap.getInt(key: String) =
    this[key] as Int?;

fun YamlMap.getDouble(key: String) =
    this[key] as Double?;

fun YamlMap.getLong(key: String) =
    this[key] as Long?;

fun YamlMap.getString(key: String) =
    this[key] as String?;

fun YamlMap.getBoolean(key: String) =
    this[key] as Boolean?;

inline fun <reified T: Enum<T>> YamlMap.getEnum(key: String): T? {
    val str = getString(key);

    return if (str == null) {
        null;
    } else {
        enumValueOf<T>(str);
    }
}


fun YamlMap.requireInt(key: String, errorMessage: String? = null) =
    getInt(key) ?: throwNSEE(key, errorMessage);

fun YamlMap.requireDouble(key: String, errorMessage: String? = null) =
    getDouble(key) ?: throwNSEE(key, errorMessage);

fun YamlMap.requireLong(key: String, errorMessage: String? = null) =
    getLong(key) ?: throwNSEE(key, errorMessage);

fun YamlMap.requireString(key: String, errorMessage: String? = null) =
    getString(key) ?: throwNSEE(key, errorMessage);

fun YamlMap.requireBoolean(key: String, errorMessage: String? = null) =
    getBoolean(key) ?: throwNSEE(key, errorMessage)

inline fun <reified T: Enum<T>> YamlMap.requireEnum(key: String, errorMessage: String? = null) =
    getEnum<T>(key) ?: throw NoSuchElementException(errorMessage ?: "No $key definition available"); // can't use throwNSEE because of private inlining


//XXX why can't I publicily inline private inline functions?
internal fun throwNSEE(key: String, errorMessage: String?) {
    throw NoSuchElementException(errorMessage ?: "No $key definition available");
}