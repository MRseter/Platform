public class MotorController implements Runnable {

  private final int maxEnc; //maximum encoder value
  private final int minEnc; // minimum encoder value
  private final double smalestStep; // the minimum amount the motor can move;
  private static final double OPERATING_ANGLE = Math.PI * 8 / 10;// the operting angles of the motor
  private PID pid;
  private Broadcast broadcaster;
  private boolean recived;
  private Axies axie;
  private StorageBox box;

  public MotorController(Broadcast broadcaster, Axies axie, StorageBox box, int minEnc, int maxEnc) {
    this.axie = axie;
    this.broadcaster = broadcaster;
    this.box = box;
    this.recived = true;
    this.maxEnc = maxEnc;
    this.minEnc = minEnc;
    this.smalestStep = (maxEnc - minEnc) * OPERATING_ANGLE;
  }

  /**
   * @param angle
   * @return
   */
  private double getRotations(double angle) {
    double returnvalue = 0;
    if (PlatformControler.PI / 2 > Math.abs(angle)) {
      returnvalue = Math.asin(PlatformControler.ANGLE_CONSTANT * angle);
    } else if (angle >= 0) returnvalue = PlatformControler.MAX_ANGLE;
    else returnvalue = -PlatformControler.MAX_ANGLE;
    return returnvalue;
  }

  /**
   * waits for new angles from the gyro then sends the angles to the storage box.
   */
  public void run() {
    //set up variabels
    double GyroValue;
    double newGyroValue;
    double lastGyroValue = box.getStartPossition(axie);
    double error;
    double hertz = 0.1;
    double delta = 0; //= box.getAxies(axie);
    double newAngle;
    //while thread is running wait for broadcast then proses info and send it to writer
    while (!Thread.currentThread().isInterrupted()) {

      while (!recived) {
        broadcaster.await();
        recived = true;
      }
      newAngle = box.getAxies(axie);
      // calculates the angle we want the motor to change to.
      newGyroValue = broadcaster.recive(axie);
      delta = (newGyroValue - lastGyroValue)/(hertz);

      angle = angle - error;

      //TODO: add regualtion???


      box.setAxies(axie, angle);
      lastGyroValue = newGyroValue;
      recived = false;
    }
  }

  /**
   * returns the angle of the motor givena encoder value
   *
   * @param encoderValue the encoder value.
   * @return angle of the motor
   */
  private double calculateMotorAngle(int encoderValue) {
    return (encoderValue - minEnc) / maxEnc * Math.PI;
  }

  public static double getOperatingAngle(){
    return OPERATING_ANGLE;
  }
}
