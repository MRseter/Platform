import java.io.IOException;
import java.nio.ByteBuffer;
import com.pi4j.io.*;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

public class MotorWriter implements Runnable {
  private final long FREQ;
  private final String PORT;
  private SerialCom ser;
  private StorageBox box;
  private RoboClaw mc1; //controller for m1 and m2
  private RoboClaw mc2; //controller for m3
  private int maxEnc; //maximum encoder value
  private int minEnc; // minimum encoder value

  final GpioController gpio = GpioFactory.getInstance();
  Gpio.pinMode(0,Gpio.INPUT);//-->RaspPi pin 11
  Gpio.pinMode(1,Gpio.INPUT);//-->RaspPi pin 12
  Gpio.pinMode(2,Gpio.INPUT);//-->RaspPi pin 13
  Gpio.pinMode(3,Gpio.INPUT);//-->RaspPi pin 15
  Gpio.pinMode(4,Gpio.INPUT);//-->RaspPi pin 16
  Gpio.pinMode(5,Gpio.INPUT);//-->RaspPi pin 18

  public MotorWriter(long freq, StorageBox box, String address) {
    this.PORT = address;
    startSerialCom();
    this.mc1 = new RoboClaw(0x80);
    this.mc2 = new RoboClaw(0x81);
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

  /**
   * @param newValues
   */
  private void sendNewMotorPositions(int[] newValues) {
    sendM1Pos(newValues[0]);
    sendM2Pos(newValues[1]);
    sendM3Pos(newValues[2]);
  }

  /**
   * @param encValue
   */
  public void sendM1Pos(int encValue) {
    byte[] cmd = mc1.setPosM1Cmd(encValue);
    System.out.println(ByteBuffer.wrap(cmd).getInt());

    sendCommandArray(cmd);
  }

  /**
   * @param encValue
   */
  public void sendM2Pos(int encValue) {
    byte[] cmd = mc1.setPosM2Cmd(encValue);
    System.out.println(ByteBuffer.wrap(cmd).getInt());

    sendCommandArray(cmd);
  }

  /**
   * @param encValue
   */
  public void sendM3Pos(int encValue) {
    byte[] cmd = mc2.setPosM1Cmd(encValue);

    System.out.println(ByteBuffer.wrap(cmd).getInt());

    sendCommandArray(cmd);
  }

  /**
   * @param motor
   * @param encValue
   */
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

  /**
   * @return
   */
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
    int cmd = cmds.get(Cmd.READ_ENC1);
    return getCmdArray(new int[]{address, cmd});
  }

  /**
   *For calibration purposes only. Sets max and min encoder value for all motors
   *
   */
  private void resetMotorsAndEncoders() {
    //boolean atStart = false
    byte[] minMaxPosition;
    ///////////////////////////////////
    boolean atEnd = false;//while false run motors
    while (!atEnd) {
      driveM1(-100);//set rotational speed
      //getInput form sensor
      atEnd = getInputFormSensor(0);
    }
    sendCommandArray(mc1.stopM1());//Stop motor 1
    sendCommandArray(mc1.resetEnc1Cmd());
    atEnd = false;//while false run motors
    while (!atEnd) {
      driveM1(100);//set rotational speed
      //getInput form sensor
      atEnd = getInputFormSensor(1);
    }
    sendCommandArray(mc1.stopM1());//Stop motor 1
    minMaxPosition = mc1.getEnc1Cmd();//Gets motor position as encoder value
    maxEnc = Bytbuffer.wrap(minMaxPosition).getInt(); //Saves maxEnc value
    setMotorPos(1, (int) ((maxEnc - minEnc) / 2));
    ///////////////////////////////////
    atEnd = false;//while false run motors
    while (!atEnd) {
      driveM2(-100);//set rotational speed
      //getInput form sensor
      atEnd = getInputFormSensor(2);
    }
    sendCommandArray(mc1.stopM2());//Stop motor 2
    sendCommandArray(mc1.resetEnc2Cmd());
    atEnd = false;//while false run motors
    while (!atEnd) {
      driveM2(100);//set rotational speed
      //getInput form sensor
      atEnd = getInputFormSensor(3);
    }
    sendCommandArray(mc1.stopM2());//Stop motor 2
    minMaxPosition = mc1.getEnc2Cmd();//Gets motor position as encoder value
    maxEnc = Bytbuffer.wrap(minMaxPosition).getInt();//Saves maxEnc value
    setMotorPos(2, (int) ((maxEnc - minEnc) / 2));
    ///////////////////////////////////
    atEnd = false;//while false run motors
    while (!atEnd) {
      driveM3(-100);//set rotational speed
      //getInput from sensor
      atEnd = getInputFormSensor(4);
    }
    sendCommandArray(mc2.stopM1());//Stop motor 3
    sendCommandArray(mc2.resetEnc1Cmd);
    atEnd = false;//while false run motors
    while(!atEnd){
      driveM3(100);//set rotational speed
      //getInput from sensor
      atEnd = getInputFormSensor(5);
    }
    sendCommandArray(mc2.stopM1());//Stop motor 3
    minMaxPosition = mc2.getEnc1Cmd();//Gets motor position as encoder value
    maxEnc = Bytbuffer.wrap(minMaxPosition).getInt();//Saves maxEnc value
    setMotorPos(3, (int) ((maxEnc - minEnc) / 2));
    ///////////////////////////////////
  }

  /**
   * @param cmd
   */
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

  /**
   * @param ba
   */
  private void sendCommandArray(byte[] ba) {
    sendCommand(ba);
    try {
      System.out.println(ser.readLine());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * @param sensor
   * @return
   */
  private boolean getInputFormSensor(int sensor){
    if(sensor == 0) {
      int value = Gpio.digitalRead(sensor)
      if(value != 1)
      {
        return false;
      }
      else{
        return true;
      }
    }
    if(sensor == 1){
      int value = Gpio.digitalRead(sensor)
      if(value != 1)
      {
        return false;
      }
      else{
        return true;
      }
    }
    if(sensor == 2){
      int value = Gpio.digitalRead(sensor)
      if(value != 1)
      {
        return false;
      }
      else{
        return true;
      }
    }
    if(sensor == 3){
      int value = Gpio.digitalRead(sensor)
      if(value != 1)
      {
        return false;
      }
      else{
        return true;
      }
    }
    I
    if(sensor == 4){
      int value = Gpio.digitalRead(sensor)
      if(value != 1)
      {
        return false;
      }
      else{
        return true;
      }
    }
    I
    if(sensor == 5){
      int value = Gpio.digitalRead(sensor)
      if(value != 1)
      {
        return false;
      }
      else{
        return true;
      }
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
