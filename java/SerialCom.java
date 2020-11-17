import com.pi4j.io.serial.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * connects to and sends information to the MCU. usint ttyAMA0 with 115200 bandthith
 *
 * @author sophu
 * @version 0
 */
public class SerialCom {

  private com.pi4j.io.serial.Serial ser;
  private SerialConfig config;
  private BufferedReader bfReader;
  private OutputStream out;

  public SerialCom(String tty, int baud) throws IOException, IllegalArgumentException {
    setUpSerial(tty, baud);
  }

  public void enableFlowControll() {
    config.flowControl(FlowControl.SOFTWARE);
  }

  private void setUpSerial(String tty, int baud) throws IOException, IllegalArgumentException {
    Baud b = Baud.getInstance(baud);
    if (b == null) {
      throw new IllegalArgumentException("baudrate: " + baud + " is NOT a valid baudrate");
    }
    ser = SerialFactory.createInstance();
    config = new SerialConfig();
    config.device(tty).baud(b).dataBits(DataBits._8).parity(Parity.NONE).stopBits(StopBits._1).flowControl(FlowControl.NONE);
    
		System.out.println("Serial is configerated now opening");
    
    ser.open(config);
    
    bfReader = new BufferedReader(new InputStreamReader(ser.getInputStream()));
    out = ser.getOutputStream();
		System.out.println("serial com open "+ Thread.currentThread().getName());
  
  }


  public void close() throws IllegalStateException, IOException {
    ser.close();

  }

  public boolean isOpen() {
    return ser.isOpen();
  }

  public boolean isClosed() {
    return ser.isClosed();
  }

  public void flush() throws IllegalStateException, IOException {
    out.flush();

  }

  public void discardInput() throws IllegalStateException, IOException {
    ser.discardInput();
  }

  public void discardOutput() throws IllegalStateException, IOException {
    ser.discardOutput();
  }
public String readLine()throws IOException{
    return bfReader.readLine();
}
  public void discardAll() throws IllegalStateException, IOException {
    ser.discardAll();
  }

  public void sendBreak(int i) throws IllegalStateException, IOException {
    ser.sendBreak(i);
  }

  public void sendBreak() throws IllegalStateException, IOException {
    ser.sendBreak();
  }

  public void setBreak(boolean b) throws IllegalStateException, IOException {
    ser.setBreak(b);
  }

  public boolean getRTS() throws IllegalStateException, IOException {
    return ser.getRTS();
  }

  public void setRTS(boolean b) throws IllegalStateException, IOException {
    ser.setRTS(b);
  }

  public void addListener(SerialDataEventListener... serialDataEventListeners) {
    ser.addListener(serialDataEventListeners);
  }

  public void removeListener(SerialDataEventListener... serialDataEventListeners) {
    ser.removeListener(serialDataEventListeners);
  }

  public int getFileDescriptor() {
    return ser.getFileDescriptor();
  }

  public InputStream getInputStream() {
    return ser.getInputStream();
  }

  public OutputStream getOutputStream() {
    return ser.getOutputStream();
  }

  public boolean isBufferingDataReceived() {
    return ser.isBufferingDataReceived();
  }

  public void setBufferingDataReceived(boolean b) {
    ser.setBufferingDataReceived(b);
  }

  public int available() throws IllegalStateException, IOException {
    return ser.available();
  }

  public void discardData() throws IllegalStateException, IOException {
    ser.discardData();
  }

  public byte[] read() throws IllegalStateException, IOException {
    return ser.read();
  }

  public byte[] read(int i) throws IllegalStateException, IOException {
    return ser.read(i);
  }

  public void read(ByteBuffer byteBuffer) throws IllegalStateException, IOException {
    ser.read(byteBuffer);
  }

  public void read(int i, ByteBuffer byteBuffer) throws IllegalStateException, IOException {
    ser.read(i, byteBuffer);
  }

  public void read(OutputStream outputStream) throws IllegalStateException, IOException {
    ser.read(outputStream);
  }

  public void read(int i, OutputStream outputStream) throws IllegalStateException, IOException {
    ser.read(i, outputStream);
  }

  public void read(Collection<ByteBuffer> collection) throws IllegalStateException, IOException {
    ser.read(collection);
  }

  public void read(int i, Collection<ByteBuffer> collection) throws IllegalStateException, IOException {
    ser.read(i, collection);
  }

  public CharBuffer read(Charset charset) throws IllegalStateException, IOException {
    return ser.read(charset);
  }

  public CharBuffer read(int i, Charset charset) throws IllegalStateException, IOException {
    return ser.read(i, charset);
  }

  public void read(Charset charset, Writer writer) throws IllegalStateException, IOException {
    ser.read(charset, writer);
  }

  public void read(int i, Charset charset, Writer writer) throws IllegalStateException, IOException {
    ser.read(i, charset, writer);
  }

  public void write(byte[] bytes, int i, int i1) throws IllegalStateException, IOException {
    ser.write(bytes, i, i1);
  }

  public void write(byte... bytes) throws IllegalStateException, IOException {
    ser.write(bytes);
  }

  public void write(byte[]... bytes) throws IllegalStateException, IOException {
    ser.write(bytes);
  }

  public void write(ByteBuffer... byteBuffers) throws IllegalStateException, IOException {
    ser.write(byteBuffers);
  }

  public void write(InputStream inputStream) throws IllegalStateException, IOException {
    ser.write(inputStream);
  }

  public void write(Charset charset, char[] chars, int i, int i1) throws IllegalStateException, IOException {
    ser.write(charset, chars, i, i1);
  }

  public void write(Charset charset, char... chars) throws IllegalStateException, IOException {
    ser.write(charset, chars);
  }

  public void write(char... chars) throws IllegalStateException, IOException {
    ser.write(chars);
  }

  public void write(Charset charset, CharBuffer... charBuffers) throws IllegalStateException, IOException {
    ser.write(charset, charBuffers);
  }

  public void write(CharBuffer... charBuffers) throws IllegalStateException, IOException {
    ser.write(charBuffers);
  }

  public void write(Charset charset, CharSequence... charSequences) throws IllegalStateException, IOException {
    ser.write(charset, charSequences);
  }

  public void write(CharSequence... charSequences) throws IllegalStateException, IOException {
    ser.write(charSequences);
  }

  public void write(Charset charset, Collection<? extends CharSequence> collection) throws IllegalStateException, IOException {
    ser.write(charset, collection);
  }

  public void write(Collection<? extends CharSequence> collection) throws IllegalStateException, IOException {
    ser.write(collection);
  }

  public void writeln(Charset charset, CharSequence... charSequences) throws IllegalStateException, IOException {
    ser.writeln(charset, charSequences);
  }

  public void writeln(CharSequence... charSequences) throws IllegalStateException, IOException {
    ser.writeln(charSequences);
  }

  public void writeln(Charset charset, Collection<? extends CharSequence> collection) throws IllegalStateException, IOException {
    ser.writeln(charset, collection);
  }

  public void writeln(Collection<? extends CharSequence> collection) throws IllegalStateException, IOException {
    ser.writeln(collection);
  }
}
