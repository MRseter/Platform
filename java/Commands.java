import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Commands {
  private final Map<Cmd, Byte> commands;

  public Commands() {
    commands = new HashMap<Cmd, Byte>();

    //--------------------COMMANDS--------------------------------------------------

    commands.put(Cmd.READ_ENC1, (byte) 16);
    commands.put(Cmd.READ_ENC2, (byte) 17);
    commands.put(Cmd.DRIVE_FORWARD, (byte) 8);
    commands.put(Cmd.DRIVE_BACKWARD, (byte) 9);
    commands.put(Cmd.DRIVE_FORWARD_M1, (byte) 0);
    commands.put(Cmd.DRIVE_BACKWARDS_M1, (byte) 1);
    commands.put(Cmd.DRIVE_FORWARD_M2, (byte) 4);
    commands.put(Cmd.DRIVE_BACKWARDS_M2, (byte) 5);
    commands.put(Cmd.READ_M1_SPEED, (byte) 18);//Read M1 Speed in Encoder Counts Per Second.
    commands.put(Cmd.READ_M2_SPEED, (byte) 19);//Read M2 Speed in Encoder Counts Per Second.
    commands.put(Cmd.READ_CUR_1, (byte) 30);//Read Current M1 Raw Speed
    commands.put(Cmd.READ_CUR_2, (byte) 31); //Read Current M2 Raw Speed
    commands.put(Cmd.READ_ENC_CNT, (byte) 78); //Read Encoders Counts
    commands.put(Cmd.READ_RAW_MTR_SPD, (byte) 79); //Read Raw Motor Speeds
    commands.put(Cmd.READ_AVG_MTR_SPD, (byte) 108); //Read Motor Average Speeds
    commands.put(Cmd.READ_SPD_ERR, (byte) 111); //Read Speed Errors
    commands.put(Cmd.READ_POS_ERR, (byte) 114); //Read Position Errors
    commands.put(Cmd.READ_BUFFER_LEN, (byte) 47);//Read Buffer Length.
    commands.put(Cmd.READ_M1_VEL_PID_CONST, (byte) 55);//Read Motor 1 Velocity PID Constants
    commands.put(Cmd.READ_M2_VEL_PID_CONST, (byte) 56);//Read Motor 2 Velocity PID Constants
    commands.put(Cmd.READ_POS_PID_M1, (byte) 63);//Read Motor 1 Position PID Constants
    commands.put(Cmd.READ_POS_PID_M2, (byte) 64);//Read Motor 2 Position PID Constants
    //set commands
    commands.put(Cmd.SET_ENC1, (byte) 22);//Set Encoder 1 Register(Quadrature only).
    commands.put(Cmd.SET_ENC2, (byte) 23);//Set Encoder 2 Register(Quadrature only).
    commands.put(Cmd.SET_VEl_PID_CONST1, (byte) 28); //Set Velocity PID Constants for M1.
    commands.put(Cmd.SET_VEl_PID_CONST2, (byte) 29);//Set Velocity PID Constants for M2.
    commands.put(Cmd.SET_POS_PID_M1, (byte) 61);//Set Position PID Constants for M1.
    commands.put(Cmd.SET_POS_PID_M2, (byte) 62);//Set Position PID Constants for M2
    //reset commands
    commands.put(Cmd.RESET_ENCS, (byte) 20);//Resets Encoder Registers for M1 and M2(Quadrature only).
    //drive  commands
    commands.put(Cmd.DRIVE_M1_DUTY_CYCL, (byte) 32);//Drive M1 With Signed Duty Cycle. (Encoders not required)
    commands.put(Cmd.DRIVE_M2_DUTY_CYCL, (byte) 33);//Drive M2 With Signed Duty Cycle. (Encoders not required)
    commands.put(Cmd.DRIVE_BOTH_DUTY_CYCL, (byte) 34);//Drive M1 / M2 With Signed Duty Cycle. (Encoders not required)
    commands.put(Cmd.DRIVE_M1_SIGN_SPD, (byte) 35);//Drive M1 With Signed Speed.
    commands.put(Cmd.DRIVE_M2_SIGN_SPD, (byte) 36);//Drive M2 With Signed Speed.
    commands.put(Cmd.DRIVE_BOTH_SIGN_SPD, (byte) 37);//Drive M1 / M2 With Signed Speed.
    commands.put(Cmd.DRIVE_M1_SIGN_SPD_ACL, (byte) 38);//Drive M1 With Signed Speed And Acceleration.
    commands.put(Cmd.DRIVE_M2_SIGN_SPD_ACL, (byte) 39);//Drive M2 With Signed Speed And Acceleration.
    commands.put(Cmd.DRIVE_BOTH_SIGN_SPD_ACL, (byte) 40);//Drive M1 / M2 With Signed Speed And Acceleration.
    commands.put(Cmd.DRIVE_M1_SIGN_SPD_DIST, (byte) 41);//Drive M1 With Signed Speed And Distance. Buffered.
    commands.put(Cmd.DRIVE_M2_SIGN_SPD_DIST, (byte) 42);//Drive M2 With Signed Speed And Distance. Buffered.
    commands.put(Cmd.DRIVE_BOTH_SIGN_SPD_DIST, (byte) 43);//Drive M1 / M2 With Signed Speed And Distance. Buffered.
    commands.put(Cmd.DRIVE_M1_SIGN_SPD_DIST_ACL, (byte) 44);//Drive M1 With Signed Speed, Acceleration and Distance. Buffered.
    commands.put(Cmd.DRIVE_M2_SIGN_SPD_DIST_ACL, (byte) 45);//Drive M2 With Signed Speed, Acceleration and Distance. Buffered.
    commands.put(Cmd.DRIVE_BOTH_SIGN_SPD_DIST_ACL, (byte) 46);//Drive M1 / M2 With Signed Speed, Acceleration And Distance. Buffered.
    commands.put(Cmd.DRIVE_BOTH_INDIVIDUAL_SIGN_SPD_ACL, (byte) 50);//Drive M1 / M2 With Individual Signed Speed and Acceleration
    commands.put(Cmd.DRIVE_BOTH_INDIVIDUAL_SIGN_SPD_ACL_DIST, (byte) 51);//Drive M1 / M2 With Individual Signed Speed, Accel and Distance
    commands.put(Cmd.DRIVE_M1_SIGN_DUTY_SPD, (byte) 52);//Drive M1 With Signed Duty and Accel. (Encoders not required)
    commands.put(Cmd.DRIVE_M2_SIGN_DUTY_SPD, (byte) 53);//Drive M2 With Signed Duty and Accel. (Encoders not required)
    commands.put(Cmd.DRIVE_BOTH_SIGN_DUTY_SPD, (byte) 54);//Drive M1 / M2 With Signed Duty and Accel. (Encoders not required)
    commands.put(Cmd.DRIVE_M1_SPD_ACL_DCL_POS, (byte) 65);//Drive M1 with Speed, Accel, Deccel and Position
    commands.put(Cmd.DRIVE_M2_SPD_ACL_DCL_POS, (byte) 66);// Drive M2 with Speed, Accel, Deccel and Position
    commands.put(Cmd.DRIVE_BOTH_SPD_ACL_DCL_POS, (byte) 67);//Drive M1 / M2 with Speed, Accel, Deccel and Position
    commands.put(Cmd.DRIVE_M1_POS, (byte) 119); //Drive M1 with Position.
    commands.put(Cmd.DRIVE_M2_POS, (byte) 120); //Drive M2 with Position.
    commands.put(Cmd.DRIVE_BOTH_POS, (byte) 121); //Drive M1/M2 with Position.
    commands.put(Cmd.DRIVE_M1_SPD_POS, (byte) 122); //Drive M1 with Speed and Position.
    commands.put(Cmd.DRIVE_M2_SPD_POS, (byte) 123); //Drive M2 with Speed and Position.
    commands.put(Cmd.DRIVE_BOTH_SPD_PSO, (byte) 124); //Drive M1/M2 with Speed and Postion.);
  }

  /**
   * Returns the number of key-value mappings in this map.  If the
   * map contains more than {@code Integer.MAX_VALUE} elements, returns
   * {@code Integer.MAX_VALUE}.
   *
   * @return the number of key-value mappings in this map
   */
  public int size() {

    return commands.size();
  }

  /**
   * Returns {@code true} if this map contains no key-value mappings.
   *
   * @return {@code true} if this map contains no key-value mappings
   */
  public boolean isEmpty() {
    return false;
  }

  /**
   * Returns {@code true} if this map contains a mapping for the specified
   * key.  More formally, returns {@code true} if and only if
   * this map contains a mapping for a key {@code k} such that
   * {@code Objects.equals(key, k)}.  (There can be
   * at most one such mapping.)
   *
   * @param key key whose presence in this map is to be tested
   * @return {@code true} if this map contains a mapping for the specified
   * key
   * @throws ClassCastException   if the key is of an inappropriate type for
   *                              this map
   *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified key is null and this map
   *                              does not permit null keys
   *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
   */
  public boolean containsKey(Cmd key) {
    return commands.containsKey(key);
  }

  /**
   * Returns {@code true} if this map maps one or more keys to the
   * specified value.  More formally, returns {@code true} if and only if
   * this map contains at least one mapping to a value {@code v} such that
   * {@code Objects.equals(value, v)}.  This operation
   * will probably require time linear in the map size for most
   * implementations of the {@code Map} interface.
   *
   * @param value value whose presence in this map is to be tested
   * @return {@code true} if this map maps one or more keys to the
   * specified value
   * @throws ClassCastException   if the value is of an inappropriate type for
   *                              this map
   *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified value is null and this
   *                              map does not permit null values
   *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
   */
  public boolean containsValue(byte value) {
    return commands.containsValue(value);
  }

  /**
   * Returns the value to which the specified key is mapped,
   * or {@code null} if this map contains no mapping for the key.
   *
   * <p>More formally, if this map contains a mapping from a key
   * {@code k} to a value {@code v} such that
   * {@code Objects.equals(key, k)},
   * then this method returns {@code v}; otherwise
   * it returns {@code null}.  (There can be at most one such mapping.)
   *
   * <p>If this map permits null values, then a return value of
   * {@code null} does not <i>necessarily</i> indicate that the map
   * contains no mapping for the key; it's also possible that the map
   * explicitly maps the key to {@code null}.  The {@link #containsKey
   * containsKey} operation may be used to distinguish these two cases.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped, or
   * {@code null} if this map contains no mapping for the key
   * @throws ClassCastException   if the key is of an inappropriate type for
   *                              this map
   *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified key is null and this map
   *                              does not permit null keys
   *                              (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
   */
  public byte get(Cmd key) {
    return commands.get(key);
  }


  /**
   * Returns a {@link Set} view of the keys contained in this map.
   * The set is backed by the map, so changes to the map are
   * reflected in the set, and vice-versa.  If the map is modified
   * while an iteration over the set is in progress (except through
   * the iterator's own {@code remove} operation), the results of
   * the iteration are undefined.  The set supports element removal,
   * which removes the corresponding mapping from the map, via the
   * {@code Iterator.remove}, {@code Set.remove},
   * {@code removeAll}, {@code retainAll}, and {@code clear}
   * operations.  It does not support the {@code add} or {@code addAll}
   * operations.
   *
   * @return a set view of the keys contained in this map
   */
  public Set<Cmd> keySet() {
    return commands.keySet();
  }

  /**
   * Returns a {@link Collection} view of the values contained in this map.
   * The collection is backed by the map, so changes to the map are
   * reflected in the collection, and vice-versa.  If the map is
   * modified while an iteration over the collection is in progress
   * (except through the iterator's own {@code remove} operation),
   * the results of the iteration are undefined.  The collection
   * supports element removal, which removes the corresponding
   * mapping from the map, via the {@code Iterator.remove},
   * {@code Collection.remove}, {@code removeAll},
   * {@code retainAll} and {@code clear} operations.  It does not
   * support the {@code add} or {@code addAll} operations.
   *
   * @return a collection view of the values contained in this map
   */
  public Collection<Byte> values() {
    return commands.values();
  }

  /**
   * Returns a {@link Set} view of the mappings contained in this map.
   * The set is backed by the map, so changes to the map are
   * reflected in the set, and vice-versa.  If the map is modified
   * while an iteration over the set is in progress (except through
   * the iterator's own {@code remove} operation, or through the
   * {@code setValue} operation on a map entry returned by the
   * iterator) the results of the iteration are undefined.  The set
   * supports element removal, which removes the corresponding
   * mapping from the map, via the {@code Iterator.remove},
   * {@code Set.remove}, {@code removeAll}, {@code retainAll} and
   * {@code clear} operations.  It does not support the
   * {@code add} or {@code addAll} operations.
   *
   * @return a set view of the mappings contained in this map
   */
  public Set<Map.Entry<Cmd, Byte>> entrySet() {
    return commands.entrySet();
  }

  /**
   * Compares the specified object with this map for equality.  Returns
   * {@code true} if the given object is also a map and the two maps
   * represent the same mappings.  More formally, two maps {@code m1} and
   * {@code m2} represent the same mappings if
   * {@code m1.entrySet().equals(m2.entrySet())}.  This ensures that the
   * {@code equals} method works properly across different implementations
   * of the {@code Map} interface.
   *
   * @param o object to be compared for equality with this map
   * @return {@code true} if the specified object is equal to this map
   */
  public boolean equals(Object o) {
    return commands.equals(o);
  }

  /**
   * Returns the hash code value for this map.  The hash code of a map is
   * defined to be the sum of the hash codes of each entry in the map's
   * {@code entrySet()} view.  This ensures that {@code m1.equals(m2)}
   * implies that {@code m1.hashCode()==m2.hashCode()} for any two maps
   * {@code m1} and {@code m2}, as required by the general contract of
   * {@link Object#hashCode}.
   *
   * @return the hash code value for this map
   * @see Object#equals(Object)
   * @see #equals(Object)
   */
  public int hashCode() {
    return commands.hashCode();
  }
}