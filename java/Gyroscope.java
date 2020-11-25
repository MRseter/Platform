import java.io.IOException;

public class Gyroscope implements Runnable {
  private static long readTimer;
  private double[] gyroValues;
  private Broadcast broadcaster;
  private SerialCom ser;
  private boolean isReady;

  public Gyroscope(Broadcast broadcaster, long readTimerMillis, String tty) {
    this.broadcaster = broadcaster;
    this.readTimer = readTimerMillis;
    this.gyroValues = new double[6];
    this.isReady = false;

    try {
      this.ser = new SerialCom(tty, 115200);
    } catch (IOException IOe) {
      System.out.println(IOe.getMessage());
      throw new RuntimeException("error in gyrothread: during creation" + IOe.getMessage());
    }
  }

  /**
   * collect and bradcasts data form the serial communication on a set time interval.
   *
   */
  public void run() {
    startUp();
    long start = System.currentTimeMillis();
    while (!Thread.currentThread().isInterrupted()) {
      updateGyroValues();
     // broadcastGyroValues();

      broadcaster.awaitTimed(readTimer - (System.currentTimeMillis() - start));
    }
    System.out.println("gyro thread interupted during run time");
  }

  /**
   * reads the input of the serial gyro comunication until the startup messages are done and prints the startup messages.
   */
  private void startUp() {
    try {
      String loggerOutput = ser.readLine();
      System.out.println(loggerOutput);
      broadcaster.awaitTimed(250);
      while (!loggerOutput.matches("[\\d,.-]+")) {
        loggerOutput = ser.readLine();
        System.out.println(loggerOutput);
        broadcaster.awaitTimed(250);
      }
      System.out.println("startup is done thread has no more characters " +
          loggerOutput + ser.readLine() + "\n starting gyro readings");
      broadcaster.awaitTimed(250);
      isReady = true;
    } catch (IOException IOe) {
      throw new RuntimeException("error in gyrothread start up method: " + IOe.getMessage());
    }
  }

  private void updateGyroValues() {
    try {
      String[] gyroInfo = ser.readLine().split(",");
      broadcaster.send(parseStringArrayToDouble(gyroInfo));

    } catch (IOException IOe) {
      throw new RuntimeException("error in gyrothread when updating values: " + IOe.getMessage());
    }
  }
/*
  private void broadcastGyroValues() {
    broadcaster.send(gyroValues);
  }
*/

  /**
   * gets a string array of floating numbers and returns an array of doubles
   * @param s the string array.
   * @return a double array with athe values provided.
   */
  private double[] parseStringArrayToDouble(String[] s) {
    double[] d = new double[s.length];
    for (int i = 0; i < s.length; i++) {
      d[i] = Double.parseDouble(s[i]);
    }
    return d;
  }

  public boolean isReady() {
    return isReady;
  }
}
