package stats.entropy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class CounterMap<K> implements Iterable<Map.Entry<K, Integer>>, Cloneable {

    private Map<K, Integer> map = new HashMap();

    private static Logger log = Logger.getLogger(CounterMap.class.getName());

    /**
     * Increments the counter value associated with {@code key}, and returns the
     * new value.
     *
     * @param key The key to increment
     * @return The incremented value.
     */
    public int incrementAndGet(K key) {
        return incrementAndGet(key, 1);
    }

    public Map<K, Integer> getMap() {
        return map;
    }

    public void setMap(Map<K, Integer> map) {
        this.map = map;
    }

    public static Logger getLog() {
        return log;
    }

    /**
     * Increments the value associated with {@code key} by {@code value},
     * returning the new value.
     *
     * @param key The key to increment
     * @return The incremented value.
     */
    public static void setLog(Logger log) {
        CounterMap.log = log;
    }

    public int incrementAndGet(K key, int count) {
        Integer value = map.get(key);
        if (value == null) {
            value = 0;
        }
        int newValue = count + value;
        map.put(key, newValue);
        return newValue;
    }

    /**
     * Gets the value associated with a key.
     *
     * @param key The key to look up.
     * @return The counter value stored for {@code key}, or 0 if no mapping
     * exists.
     */
    public int get(K key) {
        if (!map.containsKey(key)) {
            return 0;
        }

        return map.get(key);
    }

    /**
     * Assigns a value to a key.
     *
     * @param key The key to assign a value to.
     * @param newValue The value to assign.
     */
    public void set(K key, int newValue) {
        // Preconditions.checkNotNull(key);
        map.put(key, newValue);
    }

    /**
     * Resets the value for {@code key}. This will remove the key from the
     * counter.
     *
     * @param key The key to reset.
     */
    public void reset(K key) {
        map.remove(key);
    }

    /**
     * Gets the number of entries stored in the map.
     *
     * @return The size of the map.
     */
    public int size() {
        return map.size();
    }

    /**
     * Gets an iterator for the mapped values.
     *
     * @return Iterator for mapped values.
     */
    public Iterator<Map.Entry<K, Integer>> iterator() {
        return map.entrySet().iterator();
    }

    public void clearMap() {
        this.map.clear();
    }

    public Collection<Integer> values() {
        return map.values();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public String toString() {
        StringBuilder strVal = new StringBuilder();
        for (Map.Entry<K, Integer> entry : this) {
            strVal.append(entry.getKey().toString()).append(": ").append(entry.getValue()).append('\n');
        }
        return strVal.toString();
    }

    public Map<K, Integer> toMap() {
        return map;
    }

    @Override
    public CounterMap<K> clone() {
        CounterMap<K> newInstance = new CounterMap<K>();
        newInstance.map.putAll(map);
        return newInstance;
    }
}
