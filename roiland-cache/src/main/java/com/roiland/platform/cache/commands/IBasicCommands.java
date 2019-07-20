package com.roiland.platform.cache.commands;

import java.io.Closeable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IBasicCommands extends Closeable {

	/**
	 * Set the string value as value of the key.
	 * <p>
	 * Time complexity: O(1)
	 * 
	 * @param key
	 * @param value
	 * @return Status code reply
	 */
	String set(String key, String value);

	/**
	 * Get the value of the specified key. If the key does not exist null is
	 * returned. If the value stored at key is not a string an error is returned
	 * because GET can only handle string values.
	 * <p>
	 * Time complexity: O(1)
	 * 
	 * @param key
	 * @return Bulk reply
	 */
	String get(String key);

	/**
	 * Test if the specified key exists. The command returns "1" if the key
	 * exists, otherwise "0" is returned. Note that even keys set with an empty
	 * string as value will return "1". Time complexity: O(1)
	 * 
	 * @param key
	 * @return Boolean reply, true if the key exists, otherwise false
	 */
	Boolean exists(String key);

	/**
	 * Remove the specified keys. If a given key does not exist no operation is
	 * performed for this key. The command returns the number of keys removed.
	 * Time complexity: O(1)
	 * 
	 * @param keys
	 * @return Integer reply, specifically: an integer greater than 0 if one or
	 *         more keys were removed 0 if none of the specified key existed
	 */
	Long del(String key);

	/**
	 * Set the specified hash field to the specified value.
	 * <p>
	 * If key does not exist, a new key holding a hash is created.
	 * <p>
	 * <b>Time complexity:</b> O(1)
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return If the field already exists, and the HSET just produced an update
	 *         of the value, 0 is returned, otherwise if a new field is created
	 *         1 is returned.
	 */
	Long hset(String key, String field, String value);

	/**
	 * If key holds a hash, retrieve the value associated to the specified
	 * field.
	 * <p>
	 * If the field is not found or the key does not exist, a special 'nil'
	 * value is returned.
	 * <p>
	 * <b>Time complexity:</b> O(1)
	 * 
	 * @param key
	 * @param field
	 * @return Bulk reply
	 */
	String hget(String key, String field);

	/**
	 * Set the respective fields to the respective values. HMSET replaces old
	 * values with new values.
	 * <p>
	 * If key does not exist, a new key holding a hash is created.
	 * <p>
	 * <b>Time complexity:</b> O(N) (with N being the number of fields)
	 * 
	 * @param key
	 * @param hash
	 * @return Return OK or Exception if hash is empty
	 */
	String hmset(String key, Map<String, String> hash);

	/**
	 * Retrieve the values associated to the specified fields.
	 * <p>
	 * If some of the specified fields do not exist, nil values are returned.
	 * Non existing keys are considered like empty hashes.
	 * <p>
	 * <b>Time complexity:</b> O(N) (with N being the number of fields)
	 * 
	 * @param key
	 * @param fields
	 * @return Multi Bulk Reply specifically a list of all the values associated
	 *         with the specified fields, in the same order of the request.
	 */
	List<String> hmget(String key, String... fields);

	/**
	 * Test for existence of a specified field in a hash. <b>Time
	 * complexity:</b> O(1)
	 * 
	 * @param key
	 * @param field
	 * @return Return 1 if the hash stored at key contains the specified field.
	 *         Return 0 if the key is not found or the field is not present.
	 */
	Boolean hexists(String key, String field);

	/**
	 * Remove the specified field from an hash stored at key.
	 * <p>
	 * <b>Time complexity:</b> O(1)
	 * 
	 * @param key
	 * @param fields
	 * @return If the field was present in the hash it is deleted and 1 is
	 *         returned, otherwise 0 is returned and no operation is performed.
	 */
	Long hdel(String key, String... field);

	/**
	 * Return all the fields in a hash.
	 * <p>
	 * <b>Time complexity:</b> O(N), where N is the total number of entries
	 * 
	 * @param key
	 * @return All the fields names contained into a hash.
	 */
	Set<String> hkeys(String key);

	/**
	 * Return all the values in a hash.
	 * <p>
	 * <b>Time complexity:</b> O(N), where N is the total number of entries
	 * 
	 * @param key
	 * @return All the fields values contained into a hash.
	 */
	List<String> hvals(String key);

	/**
	 * Return all the fields and associated values in a hash.
	 * <p>
	 * <b>Time complexity:</b> O(N), where N is the total number of entries
	 * 
	 * @param key
	 * @return All the fields and values contained into a hash.
	 */
	Map<String, String> hgetAll(String key);

	/**
	 * Set a timeout on the specified key. After the timeout the key will be automatically deleted by
	 * the server. A key with an associated timeout is said to be volatile in Redis terminology.
	 * <p>
	 * Voltile keys are stored on disk like the other keys, the timeout is persistent too like all the
	 * other aspects of the dataset. Saving a dataset containing expires and stopping the server does
	 * not stop the flow of time as Redis stores on disk the time when the key will no longer be
	 * available as Unix time, and not the remaining seconds.
	 * <p>
	 * Since Redis 2.1.3 you can update the value of the timeout of a key already having an expire
	 * set. It is also possible to undo the expire at all turning the key into a normal key using the
	 * {@link #persist(String) PERSIST} command.
	 * <p>
	 * Time complexity: O(1)
	 * @see <ahref="http://code.google.com/p/redis/wiki/ExpireCommand">ExpireCommand</a>
	 * @param key
	 * @param seconds
	 * @return Integer reply, specifically: 1: the timeout was set. 0: the timeout was not set since
	 *         the key already has an associated timeout (this may happen only in Redis versions <
	 *         2.1.3, Redis >= 2.1.3 will happily update the timeout), or the key does not exist.
	 */
	Long expire(String key,int seconds);

	/**
	 * Atomically renames the key oldkey to newkey. If the source and destination name are the same an
	 * error is returned. If newkey already exists it is overwritten.
	 * <p>
	 * Time complexity: O(1)
	 * @param oldkey
	 * @param newkey
	 * @return Status code repy
	 */
	String rename(final String oldkey, final String newkey);

	/**
	 * Rename oldkey into newkey but fails if the destination key newkey already exists.
	 * <p>
	 * Time complexity: O(1)
	 * @param oldkey
	 * @param newkey
	 * @return Integer reply, specifically: 1 if the key was renamed 0 if the target key already exist
	 */
	Long renamenx(final String oldkey, final String newkey);

	/**
	 * Increment the number stored at key by one. If the key does not exist or contains a value of a
	 * wrong type, set the key to the value of "0" before to perform the increment operation.
	 * <p>
	 * INCR commands are limited to 64 bit signed integers.
	 * <p>
	 * Note: this is actually a string operation, that is, in Redis there are not "integer" types.
	 * Simply the string stored at the key is parsed as a base 10 64 bit signed integer, incremented,
	 * and then converted back as a string.
	 * <p>
	 * Time complexity: O(1)
	 * @see #incrBy(String, long)
	 * @see #decr(String)
	 * @see #decrBy(String, long)
	 * @param key
	 * @return Integer reply, this commands will reply with the new value of key after the increment.
	 */
	public Long incr(final String key);

	/**
	 * Decrement the number stored at key by one. If the key does not exist or contains a value of a
	 * wrong type, set the key to the value of "0" before to perform the decrement operation.
	 * <p>
	 * INCR commands are limited to 64 bit signed integers.
	 * <p>
	 * Note: this is actually a string operation, that is, in Redis there are not "integer" types.
	 * Simply the string stored at the key is parsed as a base 10 64 bit signed integer, incremented,
	 * and then converted back as a string.
	 * <p>
	 * Time complexity: O(1)
	 * @see #incr(String)
	 * @see #incrBy(String, long)
	 * @see #decrBy(String, long)
	 * @param key
	 * @return Integer reply, this commands will reply with the new value of key after the increment.
	 */
	public Long decr(final String key);
}
