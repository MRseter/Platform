/**
 * Handler for commands for a Roboclaw Motor controller;
 * creates commands that can be sent to the controller to control the motors
 *
 * @author fredborg
 * @version 0
 */
public class RoboClaw {

  private final byte address; // address of this Motor controller on the buss.
  private final Commands cmds; // a map of the commands that the motor controller can access.
  private byte cmd;
  private int speed;// speed of the position control.
  private int accel;//acceleration of the position control.
  private int deccel;//deceleration of the position control.
  private byte[] stoppBothMotorCommand; // stop command is pre generated for speedy delivery;

  /**
   * @param address
   */
  public RoboClaw(byte address) {
    this.address = address;
    this.cmds = new Commands();
    this.speed = 1000;
    this.accel = 1000;
    this.deccel = 1000;
    this.stoppBothMotorCommand = generateStopAllCommand();
  }

  public int getAddress() {
    return address;
  }

  /**
   * Resets the value of the Encoder 1 register. Useful when homing motor 1. This command applies to
   * quadrature encoders only.
   *
   * @return [Address, 22, Value(0), CRC(2 bytes)]
   */
  public byte[] resetEnc1Cmd() {
    cmd = cmds.get(Cmd.SET_ENC1);
    return getCmdArray(new byte[]{address, cmd}, 0);
  }

  /**
   * Resets the value of the Encoder 2 register. Useful when homing motor 2. This command applies to
   * quadrature encoders only.
   *
   * @return [Address, 22, Value(0), CRC(2 bytes)]
   */
  public byte[] resetEnc2Cmd() {
    cmd = cmds.get(Cmd.SET_ENC2);
    return getCmdArray(new byte[]{address, cmd});
  }


  /**
   * Read M1 encoder count/position.
   * <p>
   * Receive: [Enc1(4 bytes), Status, CRC(2 bytes)]
   * Quadrature encoders have a range of 0 to 4,294,967,295. Absolute encoder values are
   * converted from an analog voltage into a value from 0 to 2047 for the full 2v range.
   *
   * @return [Address, 16]
   */
  public byte[] getEnc1Cmd() {
    cmd = cmds.get(Cmd.READ_ENC1);
    return getCmdArray(new byte[]{address, cmd});
  }

  /**
   * Read M2 encoder count/position.
   * Receive: [EncCnt(4 bytes), Status, CRC(2 bytes)]
   * Quadrature encoders have a range of 0 to 4,294,967,295. Absolute encoder values are
   * converted from an analog voltage into a value from 0 to 2047 for the full 2v range.
   *
   * @return [Address, 17]
   */
  public byte[] getEnc2Cmd() {
    cmd = cmds.get(Cmd.READ_ENC2);
    return getCmdArray(new byte[]{address, cmd});
  }


  /**
   * Drive M1 With Signed Speed
   * Drive M1 using a speed value. The sign indicates which direction the motor will turn. This
   * command is used to drive the motor by quad pulses per second. Different quadrature encoders
   * will have different rates at which they generate the incoming pulses. The values used will differ
   * from one encoder to another. Once a value is sent the motor will begin to accelerate as fast as
   * possible until the defined rate is reached.
   *
   * @return [Address, 35, Speed(4 Bytes), CRC(2 bytes)]
   */

  private int addIntToArry(byte[] b, int i, int startPos) {
    byte[] b2 = getIntBytes(i);
    for (byte b3 : b2) {
      b[startPos] = b3;
      startPos++;
    }
    return startPos;
  }

  private int addShortToArry(byte[] b, int i, int startPos) {
    byte[] b2 = new byte[]{(byte) ((i >> 8) & 0xff), (byte) ((i) & 0xff)};
    for (byte b3 : b2) {
      b[startPos] = b3;
      startPos++;
    }
    return startPos;
  }

  private int addByteToArry(byte[] b, byte b1, int startPos) {
    b[startPos] = b1;
    startPos++;
    return startPos;
  }

  public byte[] driveM1Cmd(int speed) {
    int index = 0;
    cmd = cmds.get(Cmd.DRIVE_M1_SIGN_SPD);
    byte[] portBytes = new byte[]{address, cmd};
    byte[] b = getCmdArray(portBytes, speed);
    System.out.println(" :: finding cmd " + cmd + "correct size: " + (b.length == (index)));
    System.out.println(" :: returning cmd");
    printCmd(b);

    return b;
  }

  private byte[] getIntBytes(int i) {
    byte[] b = new byte[]{
        (byte) ((i >>> 24) & 0xff),
        (byte) ((i >>> 16) & 0xff),
        (byte) ((i >>> 8) & 0xff),
        (byte) ((i) & 0xff)};
    return b;
  }


  /**
   * Drive M2 With Signed Speed
   * Drive M2 using a speed value. The sign indicates which direction the motor will turn. This
   * command is used to drive the motor by quad pulses per second. Different quadrature encoders
   * will have different rates at which they generate the incoming pulses. The values used will differ
   * from one encoder to another. Once a value is sent the motor will begin to accelerate as fast as
   * possible until the defined rate is reached.
   *
   * @param speed speed of motor
   * @return [Address, 35, Speed(4 Bytes), CRC(2 bytes)]
   */
  public byte[] driveM2Cmd(int speed) {
    cmd = cmds.get(Cmd.DRIVE_M2_SIGN_SPD);
    return setDriveMtrCmd(speed, cmd);
  }

  /**
   * Buffered Drive M1 with signed Speed, Accel, Deccel and Position
   * Move M1 position from the current position to the specified new position and hold the new
   * position. Accel sets the acceleration value and deccel the decceleration value. QSpeed sets the
   * speed in quadrature pulses the motor will run at after acceleration and before decceleration.
   * Receive: [0xFF]
   *
   * @param encPos the new ecoder pos for the motor
   * @return [Address, 65, Accel(4 bytes), Speed(4 Bytes), Deccel(4 bytes), Position(4 Bytes), Buffer, CRC(2 bytes)]
   */
  public byte[] setPosM1Cmd(int encPos) {
    return setPosCmd(encPos, Cmd.DRIVE_M1_SPD_ACL_DCL_POS);
  }

  /**
   * Buffered Drive M2 with signed Speed, Accel, Deccel and Position
   * Move M2 position from the current position to the specified new position and hold the new
   * position. Accel sets the acceleration value and deccel the decceleration value. QSpeed sets the
   * speed in quadrature pulses the motor will run at after acceleration and before decceleration.
   * Receive: [0xFF]
   *
   * @param encPos the new ecoder pos for the motor
   * @return [Address, 65, Accel(4 bytes), Speed(4 Bytes), Deccel(4 bytes), Position(4 Bytes), Buffer, CRC(2 bytes)]
   */
  public byte[] setPosM2Cmd(int encPos) {
    return setPosCmd(encPos, Cmd.DRIVE_M2_SPD_ACL_DCL_POS);
  }

  /**
   * Sets Motor 1 speed to 0
   *
   * @return [Address, 7,0,crc(2 bytes)]
   */
  public byte[] stopM1() {
    cmd = cmds.get(Cmd.DRIVE_FORWARD_M1);
    return stopMotor(cmd);
  }

  /**
   * Sets Motor 2 speed to 0
   *
   * @return [Address, 7,0,crc(2 bytes)]
   */
  public byte[] stopM2() {
    cmd = cmds.get(Cmd.DRIVE_FORWARD_M2);
    return stopMotor(cmd);
  }

  /**
   * returns a premade array that stops both motors.
   *
   * @return [address, 7, 0, crc]
   */
  public byte[] stopAllMotorsCmd() {
    return this.stoppBothMotorCommand;
  }

  private byte[] getCmdArray(byte[] portBytes, int[] cmdsValues, String order) {

    byte[] ret = buildCmdArray(cmdsValues, portBytes, order);
    addShortToArry(ret, calculateCrc(ret), portBytes.length + cmdsValues.length - 1);
    return ret;
  }

  private byte[] getCmdArray(byte[] portBytes, int cmdValue) {

    byte[] ret = buildCmdArray(cmdValue, portBytes, "114");
    addShortToArry(ret, calculateCrc(ret), portBytes.length + 4 - 1);
    return ret;
  }

  private byte[] getCmdArray(byte[] portBytes) {
    byte[] ret = new byte[portBytes.length + 2];
    int i = 0;
    for (byte b : portBytes) {
      ret[i] = b;
      i++;
    }
    addShortToArry(ret, calculateCrc(ret), portBytes.length - 1);
    return ret;
  }

  private byte[] buildCmdArray(int[] ints, int[] shorts, byte[] bytes, String order) throws IllegalArgumentException {
    //reserve memory length: 4 bytes per ints, 2 bytes per shorts, 1 byte per bytes, and 2 bytes for crc value at the end.
    byte[] ret = new byte[ints.length * 4 + shorts.length * 2 + bytes.length + 2];
    int index = 0;
    int byteIndex = 0;
    int intIndex = 0;
    int shortIndex = 0;

    if (ints.length + shorts.length + bytes.length != order.length() + 2) {
      throw new IllegalArgumentException("the length of the order of the data does not match the amount of data");
    }
    for (char c : order.toCharArray()) {
      switch (c) {
        case 1:
          index = addByteToArry(ret, bytes[byteIndex], index);
          byteIndex++;
          break;
        case 2:
          index = addShortToArry(ret, shorts[shortIndex], index);
          shortIndex++;
          break;
        case 4:
          index = addIntToArry(ret, ints[intIndex], index);
          intIndex++;
          break;
        default:
          throw new IllegalArgumentException("Error: " + c + " is not a valid order argument... " +
              " the only valid order arguments are 1,2 and 4");
      }

    }
    return ret;
  }


  private byte[] buildCmdArray(int[] ints, byte[] bytes, String order) throws IllegalArgumentException {

    //reserve memory length: 4 bytes per ints ,1 byte per bytes, and 2 bytes for crc value at the end
    byte[] ret = new byte[ints.length * 4 + bytes.length + 2];
    int index = 0;
    int byteIndex = 0;
    int intIndex = 0;

    if (ints.length + bytes.length != order.length() + 2) {
      throw new IllegalArgumentException("the length of the order of the data does not match the amount of data");
    }
    for (char c : order.toCharArray()) {
      switch (c) {
        case 1:
          index = addByteToArry(ret, bytes[byteIndex], index);

          byteIndex++;
          break;
        case 4:
          index = addIntToArry(ret, ints[intIndex], index);

          intIndex++;
          break;
        default:
          throw new IllegalArgumentException("Error: " + c + " is not a valid order argument... " +
              " the only valid order arguments are 1,2 and 4");
      }

    }
    return ret;
  }

  private byte[] buildCmdArray(int integer, byte[] bytes, String order) throws IllegalArgumentException {

    //reserve memory length: 4 bytes for the int ,1 byte per bytes, and 2 bytes for crc value at the end
    byte[] ret = new byte[4 + bytes.length];
    int index = 0;
    int byteIndex = 0;

    if (1 + bytes.length != order.length()) {
      throw new IllegalArgumentException("the length of the order of the data does not match the amount of data");
    }
    for (char c : order.toCharArray()) {
      switch (c) {
        case 1:
          index = addByteToArry(ret, bytes[byteIndex], index);
          byteIndex++;
          break;
        case 4:
          index = addIntToArry(ret, integer, index);
          break;
        default:
          throw new IllegalArgumentException("Error: " + c + " is not a valid order argument... " +
              " the only valid order arguments are 1,2 and 4");
      }

    }
    int crc = calculateCrc(ret, index);
    addShortToArry(ret, crc, index);
    return ret;
  }

  private byte[] setDriveMtrCmd(int speed, byte cmd) {
    byte[] ret = getCmdArray(new byte[]{address, cmd}, new int[]{speed}, "114");
    printCmd(ret);
    System.out.println(" :: returning cmd");
    return ret;
  }

  private void printCmd(byte[] cmd) {
    for (byte b : cmd) {
      System.out.print((b & 0xff) + "  ");
    }
    System.out.println(" :: ");
  }


  private byte[] setPosCmd(int encPos, Cmd m) {
    cmd = cmds.get(m);
    //adding values
    byte[] ret = getCmdArray(new byte[]{address, cmd, 1}, new int[]{accel, speed, deccel, encPos}, "1144441");
    return ret;
  }

  private byte[] stopMotor(byte cmd) {
    this.cmd = cmd;
    int[] ints = new int[]{0};
    return getCmdArray(new byte[]{address, cmd}, ints, "114");
  }


  private byte[] generateStopAllCommand() {
    cmd = cmds.get(Cmd.DRIVE_FORWARD);
    byte[] bytes = new byte[]{address, cmd};
    byte[] ret = getCmdArray(bytes, 0);
    printCmd(ret);
    return ret;
  }

  private byte calculateCrc(byte[] data) {
    int crc = 0;
    for (int i = 0; i < data.length; i++) {
      crc += (data[i]);
    }
    System.out.println("crc: " + crc);
    return (byte) (crc & 0x7f);

  }

  private byte calculateCrc(byte[] data, int nBytes) {
    int crc = 0;
    for (int i = 0; i < nBytes; i++) {
      crc += (data[i]);
    }
    System.out.println("crc: " + crc);
    return (byte) (crc & 0x7f);
  }

  public byte getLastCmd() {
    return cmd;
  }
}
