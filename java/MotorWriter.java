import java.io.IOException;
import java.nio.ByteBuffer;

public class MotorWriter implements Runnable {
  private final long FREQ;
  private final String PORT;
  private SerialCom ser;
  private StorageBox box;
  private RoboClaw mc1; //controller for m1 and m2
  private RoboClaw mc2; //controller for m3
  private int maxEnc; //maximum encoder value
  private int minEnc; // minimum encoder value

  public MotorWriter(long freq, StorageBox box, String address) {
    this.PORT = address;
    startSerialCom();
    this.mc1 = new RoboClaw((byte)0x80);
    this.mc2 = new RoboClaw((byte)0x81);
    this.FREQ = freq;
    this.box = box;
    resetMotorsAndEncoders();
    minEnc = 0;
    maxEnc = 100;
  }

  private void startSerialCom() {
    try {
      this.ser = new SerialCom(PORT, 115200);
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    } catch (IOException IOe) {
      throw new RuntimeException("error in thead: " + Thread.currentThread().getName() +
          "\n error Message: " + IOe.getMessage());
    }
  }

  public void run() {
    resetMotorsAndEncoders();
    int[] newPos;
    while (!Thread.currentThread().isInterrupted()) {
      try {
        this.wait(FREQ);
        newPos = getEncPositions(box.getMotorAngles());
        sendNewMotorPositions(newPos);
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private void sendNewMotorPositions(int[] newValues) {
    sendM1Pos(newValues[0]);
    sendM2Pos(newValues[1]);
    sendM3Pos(newValues[2]);
  }

  public void sendM1Pos(int encValue) {
    byte[] cmd = mc1.setPosM1Cmd(encValue);
    System.out.println(ByteBuffer.wrap(cmd).getInt());

    sendCommandArray(cmd);
  }

  public void sendM2Pos(int encValue) {
    byte[] cmd = mc1.setPosM2Cmd(encValue);
    System.out.println(ByteBuffer.wrap(cmd).getInt());

    sendCommandArray(cmd);
  }

  public void sendM3Pos(int encValue) {
    byte[] cmd = mc2.setPosM1Cmd(encValue);

    System.out.println(ByteBuffer.wrap(cmd).getInt());

    sendCommandArray(cmd);
  }

  private void setMotorPos(int motor, int encValue) {
    byte[] commandArray;
    switch (motor) {
      case 1:
        commandArray = mc1.setPosM1Cmd(encValue);
        break;
      case 2:
        commandArray = mc1.setPosM2Cmd(encValue);
        break;
      case 3:
        commandArray = mc2.setPosM1Cmd(encValue);
        break;

      default:
        throw new IllegalArgumentException("error int " + motor + " has no corresponding motor unit.");
    }
    sendCommandArray(commandArray);
    if (checkAch()) {
      return;
    }
    throw new RuntimeException("error motor " + motor + " did not acknowlage the message.");
  }

  private boolean checkAch() {
    try {
      byte[] ack = ser.read(1);
      int intAck = ByteBuffer.wrap(ack).getInt();
      if (intAck == 0xFF) {
        return true;
      } else return false;

    } catch (IOException IOe) {
      throw new RuntimeException(IOe.getMessage());
    }

  }


  private void resetMotorsAndEncoders() {
    //TODO: readd this mehtod
    /**
     driveM1(-100);
     boolean atEnd = false;
     while (!atEnd) {
     throw new RuntimeException("program the reset motors method you dufus!");
     //getInput form sensor
     atEnd = getInputFormSensor(1);
     }
     sendCommandArray(mc1.stopM1());
     sendCommandArray(mc1.resetEnc1Cmd());

     driveM2(-100);
     atEnd = false;
     while (!atEnd) {
     //getInput form sensor
     atEnd = getInputFormSensor(2);
     }
     sendCommandArray(mc1.stopM2());
     sendCommandArray(mc1.resetEnc2Cmd());
     driveM3(-100);
     atEnd = false;
     while (!atEnd) {
     //getInput form sensor
     atEnd = getInputFormSensor(3);
     }
     sendCommandArray(mc2.stopM1());
     sendCommandArray(mc2.resetEnc1Cmd());

     setMotorPos(1, (int) ((maxEnc - minEnc) / 2));
     setMotorPos(2, (int) ((maxEnc - minEnc) / 2));
     setMotorPos(3, (int) ((maxEnc - minEnc) / 2));*/
  }

  private void sendCommand(byte[] cmd) {
    try {
      ser.write(cmd);
    } catch (IOException IOe1) {
      try {
        Thread.sleep(100);
        ser.write(cmd);
      } catch (IOException IOe2) {
        throw new RuntimeException("IO connection error in thread: " + Thread.currentThread().getName()
            + "\nTwo tries gave error messages:" + "\n" + IOe1.getMessage() + "\n" + IOe2.getMessage());
      } catch (InterruptedException e) {
      }
    }
  }

  private void sendCommandArray(byte[] ba) {
    sendCommand(ba);
    try {
      System.out.println(ser.readLine());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * returns the encoder value of the motor given an angle for the motor.
   *
   * @param motorAngle the angleof the motor
   * @return the encodervalue
   */
  private int encPosFromAngle(double motorAngle) {
    return (int) Math.round(projectDouble(motorAngle, maxEnc, minEnc, MotorController.getOperatingAngle()));
  }

  private int[] getEncPositions(double[] motorAngels) {
    int[] returnInts = new int[motorAngels.length];
    for (int i = 0; i < motorAngels.length; i++) {
      returnInts[i] = encPosFromAngle(motorAngels[i]);
    }
    return returnInts;
  }

  private double projectDouble(double value, int max, int min, double scale) {
    return (value * ((max - min) / scale));
  }

  private void driveM1(int speed) {
    sendCommandArray(mc1.driveM1Cmd(speed));
  }

  private void driveM2(int speed) {
    sendCommandArray(mc1.driveM2Cmd(speed));
  }

  private void driveM3(int speed) {
    sendCommandArray(mc2.driveM1Cmd(speed));
  }

  private byte[] mergeByteArray(byte[] a, byte[] b) {
    byte[] r = new byte[a.length + b.length];
    System.arraycopy(a, 0, r, 0, a.length);
    System.arraycopy(b, 0, r, a.length, b.length);
    return r;
  }
}
