/**
 * stores the height of each motor and changes them based on angle inputs.
 *
 * @author fredborg
 * @version 0.1
 */
public class StorageBox {
  // max height of the motors
  private static double MAX_HEIGHT;
  // min height of the motors
  private static double MIN_HEIGHT;
  // height of the motors
  private double[] motors = new double[3];
  private double[] gyro = new double[3];
  private double[] gyroFlat = new double[3];
  private double[] acl = new double[3];

  public StorageBox() {
    this.motors[0] = 0;
    this.motors[1] = 0;
    this.motors[2] = 0;
    this.gyro[0] = 0;
    this.gyro[1] = 0;
    this.gyro[2] = 0;
    this.acl[0] = 0;
    this.acl[1] = 0;
    this.acl[2] = 0;
  }

  /**
   * @param startValues
   */
  public void setSartPossitions(double[] startValues) {
    this.gyroFlat[0] = startValues[0];
    this.gyroFlat[1] = startValues[1];
    this.gyroFlat[2] = startValues[2];
  }

  public double getStartPossition(Axies axie) {
    return getSpesificAxie(axie, gyroFlat);
  }


  /**
   * sets an axie to a spesific value. fex x angle to 30 deg or z to 10 cm
   *
   * @param newValues the new value for the axie, angle for x and y, mm for z.
   * @throws IllegalArgumentException
   */
  public synchronized void setFromOpenLog(double[] newValues) throws IllegalArgumentException {
    if (newValues.length == 6) {
      this.gyro[0] = newValues[0];
      this.gyro[1] = newValues[1];
      this.gyro[2] = newValues[2];
      this.acl[0] = newValues[3];
      this.acl[1] = newValues[4];
      this.acl[2] = newValues[5];
    } else throw new IllegalArgumentException("value at storage box had" + newValues.length + "parameters, not 6");
  }

  public double getMotor1() {
    return motors[0];
  }

  public double getMotor2() {
    return motors[1];
  }

  public double getMotor3() {
    return motors[2];
  }

  public double[] getGyro() {
    return gyro;
  }

  public double getGyroAxie(Axies axies) {
    return getSpesificAxie(axies, gyro);
  }

  public synchronized void addToAxies(double value1, double value2, double value3) {
    motors[0] += value1;
    motors[1] += value2;
    motors[2] += value3;
  }


  /**
   * returnes the angle or height of an axie.
   *
   * @param axies the axie.
   * @return the angle or heigth.
   * @throws IllegalArgumentException if the axie is not valid
   */
  public double getAxies(Axies axies) throws IllegalArgumentException {
    switch (axies) {
      case X:
        return Math.asin((getMotor1() - (getMotor2() + getMotor3()) / 2) / 2);

      case Y:
        return Math.asin((getMotor2() - getMotor3()) / 2);

      case Z:
        return (getMotor3() + getMotor2() + getMotor1());

      default:
        throw new IllegalArgumentException("only X,Y and Z enums are valid inputs");
    }
  }

  /**
   * @param axies
   * @param value
   * @throws IllegalArgumentException
   */
  public synchronized void regulateAxies(Axies axies, double value) throws IllegalArgumentException {
    switch (axies) {
      case X:
        addToAxies(value / 2, -value / 2, -value / 2);
      case Y:
        addToAxies(0, value / 2, -value / 2);
      case Z:
        addToAxies(value, value, value);
      default:
        throw new IllegalArgumentException("only X,Y and Z enums are valid inputs");
    }
  }


  /**
   * @return
   */
  public double[] getMotorAngles() {
    return motors;
  }

  /**
   * checks that the value provided is inside the limits of the system
   *
   * @param value
   * @return
   */
  private boolean isInsideLimits(double value) {
    return isBetween(MIN_HEIGHT, MAX_HEIGHT, value);
  }

  /**
   * checks that a value is greater than the min value provided and less that the max value provided.
   *
   * @param value the value that you want to check
   * @return true if the value is between the min and max value
   */
  private boolean isBetween(double min, double max, double value) {
    return (value > min) && (max > value);

  }

  /**
   * returns a double represeting a spesific axie,
   *
   * @param axie the axie
   * @param d    the list of possible axie values
   * @return the value corespondingwith our axie
   */
  private double getSpesificAxie(Axies axie, double[] d) {
    switch (axie) {
      case X:
        return d[0];
      case Y:
        return d[1];
      case Z:
        return d[2];
      default:
        throw new IllegalArgumentException(" not an valid axies");
    }
  }

  /**
   * returns the spesific gyro axie of the platform
   * @param axie
   */
  public double getPlatofrmAxie(Axies axie) {
    return getSpesificAxie(axie, gyro);
  }
}
