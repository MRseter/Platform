public class PlatformControler {
  //static math classes

  static final public double PI = Math.PI;

  // platform constants
  static final public double PLATFORM_LENGTH = 10; //platform diameter in cm.
  static final public double ARM_LENGTH = 5;       //arm length in cm.
  static final public double ANGLE_CONSTANT = PLATFORM_LENGTH / (ARM_LENGTH * 4);
  static final public double MAX_ANGLE = Math.asin(ANGLE_CONSTANT * Math.sin(PI / 2));

  /**
   * runs the program
   *
   * @param args
   */
  public static void main(String[] args) {

    //initialize storage box
    StorageBox box = new StorageBox();

    //initialize broadcaster
    Broadcast broadcaster = new Broadcast(3, box);

    // initialize producer
    Gyroscope IOReadingThread = new Gyroscope(broadcaster, 100, "/dev/ttyUSB0");
    // initialize consumer threads

    MotorController XCalculator = new MotorController(broadcaster, Axies.X, box, 0, 1);
    MotorController YCalculator = new MotorController(broadcaster, Axies.Y, box, 0, 1);
    MotorController ZCalculator = new MotorController(broadcaster, Axies.Z, box, 0, 1);

    // initialise writer
    MotorWriter motorWriter = new MotorWriter(20, box, "/dev/ttyAMA0");

    // initialise threads
    Thread thread1 = new Thread(IOReadingThread, "Gyro reader thread");
    Thread thread2 = new Thread(XCalculator, "X controller thread");
    Thread thread3 = new Thread(YCalculator, "Y controller thread");
    Thread thread4 = new Thread(ZCalculator, "Z controller thread");
    Thread thread5 = new Thread(motorWriter, "Motor writer thread");

    // set up error handling to stop the program in the case of a communication error
    Thread.UncaughtExceptionHandler h = (t, e) -> {
      thread1.interrupt();
      thread2.interrupt();
      thread3.interrupt();
      thread4.interrupt();
      thread5.interrupt();
      System.out.println("Error: closed all threads\nError message: " + e.getMessage() + "\n " +
          "\nthread1 closed:" + thread1.isInterrupted() +
          "\nthread2 closed:" + thread2.isInterrupted() +
          "\nthread3 closed:" + thread3.isInterrupted() +
          "\nthread4 closed:" + thread4.isInterrupted() +
          "\nthread5 closed:" + thread5.isInterrupted());
      //TODO:Add ALARM
      System.exit(1);
    };
    thread1.setUncaughtExceptionHandler(h);
    thread2.setUncaughtExceptionHandler(h);
    thread3.setUncaughtExceptionHandler(h);
    thread4.setUncaughtExceptionHandler(h);
    thread5.setUncaughtExceptionHandler(h);

    // start Threads
    thread1.start();
    thread2.start();
    thread3.start();
    thread4.start();

    System.out.println("gyro running:        " + thread1.isAlive());
    System.out.println("controller1 running: " + thread2.isAlive());
    System.out.println("controller1 running: " + thread3.isAlive());
    System.out.println("controller1 running: " + thread4.isAlive());

  }
}
