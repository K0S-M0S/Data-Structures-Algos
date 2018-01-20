import java.util.List;
import java.util.ArrayList;

public class Task {
  private int id, time, staff;
  private String name;
  private List<Task> inEdges;
  private int inDegree;

  public boolean checked;
  public int earliestStart, latestStart, slack;

  public Task() {
    this.id = 0;
    this.time = 0;
    this.staff = 0;
    this.name = null;
    this.inEdges = new ArrayList<Task>();
    this.inDegree = 0;
    this.checked = false;
    this.earliestStart = 0;
    this.latestStart = 0;
    this.slack = 0;
  }

  public void setProperties(int id, int time, int staff, String name) {
    this.id = id;
    this.time = time;
    this.staff = staff;
    this.name = name;
  }

  public int getId() {
    return this.id;
  }

  public int getTime() {
    return this.time;
  }

  public int getStaff() {
    return this.staff;
  }

  public String getName() {
    return this.name;
  }

  public void setEarliestStart(int earliestStart) {
    this.earliestStart = earliestStart;
  }

  public int getEarliestStart() {
    return this.earliestStart;
  }

  public void setLatestStart(int latestStart) {
    this.latestStart = latestStart;
  }

  public int getLatestStart() {
    return this.latestStart;
  }

  public List<Task> getInEdges() {
    return this.inEdges;
  }

  public void setInDegree(int inDegree) {
    this.inDegree = inDegree;
  }

  public int getInDegree() {
    return this.inDegree;
  }

  public int decrementInDegree() {
    return --this.inDegree;
  }

  public String toString() {
    return this.name;
  }

}
