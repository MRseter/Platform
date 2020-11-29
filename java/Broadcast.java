/**
 * lass for handling data reception form, other classes from different threads.
 * The amount of classes that write and receive data this way is specified on creation so that the class can make sure
 * that all listeners receive the data exactly one time.
 * @autor fredborg
 * @version  1
 */
public class Broadcast {
  private double[] message;
  private boolean arrived;
  private int waiting;
  private int numberOfConsumers;
  private StorageBox box;

  public Broadcast(int numberOfConsumers, StorageBox box) {
    this.numberOfConsumers = numberOfConsumers;
    this.arrived = false;
    this.waiting = 0;
    this.box = box;
  }

  /**
   * an be accessed to add data to a storage box class.  When calling the send method,
   * the values of its input to the storage box; It then notifies listeners that are waiting for input.
   * The broadcaster will not update the values if all receivers have not received the previous value;
   * in this case, it returns false; in all other cases, it returns true.
   * @param sendMessage teh message to be stored
   * @return true if message was sent, false otherwise
   */
  public synchronized boolean send(double[] sendMessage) {
    boolean sent = false;
    if ((waiting >= 1) && (!arrived)) {
      box.setFromOpenLog(sendMessage);
      arrived = true;
      this.notifyAll();
      sent = true;

      System.out.println("sendt numbers");
    } else {
      System.out.println("error: waiting:" + waiting + " || arrived: " + arrived);
    }
    return sent;
  }

  /**
   * can be accessed to get input from the storage box class.
   * The method controllers how many times it has been accessed and
   * compares it with how many listeners that got access to it.
   * If all the listeners have already accessed the data,
   * the method waits for new inputs.
   * @param axie the axie that wasnts the data
   * @return
   */

  public synchronized double recive(Axies axie) {

    try {
      while (!arrived) {
        System.out.println("added to waiting... " + waiting + " in waiting");
        waiting++;
        this.wait();
      }
      countDown();

    } catch (InterruptedException e) {
      countDown();
    }
    return box.getPlatofrmAxie(axie);
  }

  private void countDown() {
    waiting--;
    if (waiting < 1) {
      arrived = false;
      waiting = numberOfConsumers;
    }
  }

  public synchronized void await() {
    try {
      this.wait();
    } catch (InterruptedException e) {
    } catch (IllegalMonitorStateException e2) {
      System.out.println(e2.getMessage());

    }
  }

  public synchronized void awaitTimed(long time) {
    try {
      this.wait(time);
    } catch (InterruptedException e) {
    } catch (IllegalMonitorStateException e2) {
      System.out.println(e2.getMessage());

    }
  }
}
