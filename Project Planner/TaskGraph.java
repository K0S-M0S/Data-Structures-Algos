import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class TaskGraph {
  public boolean foundCycle = false; // helper variable
  private int numVertices = 0;
  private Map<Task, ArrayList<Task>> adjMap = new HashMap<Task, ArrayList<Task>>();


  // sets up adjacency map from vertices and their in-edges
  public void initAdjMap() {
    for (Task vertex : adjMap.keySet()) {
      for (Task inVertex : vertex.getInEdges()) {
        adjMap.get(inVertex).add(vertex);
      }
    }
  }

  public Map<Task, ArrayList<Task>> getAdjMap() {
    return this.adjMap;
  }

  public void setNumVertices(int numVertices) {
    this.numVertices = numVertices;
  }

  public int getNumVertices() {
    return this.numVertices;
  }

  // attempts to find a cycle in the graph, returns a list
  // of tasks in a cycle if successful
  // runs in ~ O(|V|^2) time
  public List<Task> detectCycle() {
    List<Task> vertexCycle = new ArrayList<Task>();
    Task start = minInDegreeTask();
    detectCycleUtil(vertexCycle, start);

    // remove the tasks occuring before the cycle
    if (vertexCycle.size() > 0) {
      Task repeated = vertexCycle.get(vertexCycle.size()-1);
      for (int i = 0; i < vertexCycle.indexOf(repeated); i++)
        vertexCycle.remove(i);
    }
    return vertexCycle;
  }

  // helper method, detects a cycle in the graph
  private List<Task> detectCycleUtil (List<Task> taskChain, Task current) {
    current.checked = true;
    taskChain.add(current);

    for (Task adjacent : adjMap.get(current)) {
      if (!this.foundCycle) {
        if (!adjacent.checked) {
          detectCycleUtil(taskChain, adjacent);
        } else if(taskChain.contains(adjacent)){
          this.foundCycle = true;
          taskChain.add(adjacent);
          return taskChain;
        }
      }
    }
    if (!this.foundCycle) taskChain.remove(current);
    return taskChain;
  }

  // finds a task with a minimal indegree
  public Task minInDegreeTask() {
    int minDegree = this.numVertices+1;
    Task minVertex = null;
    for (Task vertex : adjMap.keySet()) {
      if ((vertex.getInDegree() < minDegree) && (vertex.getInDegree() >= 0)) {
        minDegree = vertex.getInDegree();
        minVertex = vertex;
      }
    }
    return minVertex;
  }

  // sorts the tasks topologically, works only on acyclic graphs
  // runs in O(|v| + |E|) time
  private List<Task> topologicalSort() {
    List<Task> sortedVertices = new ArrayList<Task>();
    Queue<Task> verticesQ = new LinkedList<Task>();
    int count = 0;

    for (Task vertex : adjMap.keySet()) {
      if (vertex.getInDegree() == 0)
        verticesQ.add(vertex);
    }

    while(!verticesQ.isEmpty()) {
      Task vertex = verticesQ.remove();
      sortedVertices.add(vertex);
      count++;

      for (Task adjacent : adjMap.get(vertex)) {
        if (adjacent.decrementInDegree() == 0)
          verticesQ.add(adjacent);
      }
    }
    return sortedVertices;
  }

  // sets earliest and latest start as well as slack times for all tasks
  // runs in O(|V| + |E|) time... probably
  public void setTimes() {
    List<Task> sortedTasks = topologicalSort();

    // setting earliest start time
    for (Task t : sortedTasks) {
      for (Task adjacent : adjMap.get(t)) {
        if (adjacent.earliestStart <= t.earliestStart + t.getTime())
          adjacent.earliestStart = t.earliestStart + t.getTime();
      }
    }

    // setting latest start time
    for (Task t : sortedTasks) {
      t.latestStart = t.earliestStart;
    }

    int lastIndex = sortedTasks.size()-1;
    for (int i = lastIndex; i >= 0; i--) {
      Task t = sortedTasks.get(i);
      for (Task preReq : t.getInEdges()) {
        if (adjMap.get(t).size() == 0 && i != lastIndex){ // separate condition for leaves
          preReq = t;
          t = sortedTasks.get(sortedTasks.size()-1);
          preReq.slack = t.latestStart + t.getTime() - preReq.earliestStart - preReq.getTime();
        } else {
          preReq.slack = t.latestStart - preReq.earliestStart - preReq.getTime();
        }
        preReq.latestStart = preReq.earliestStart + preReq.slack;
      }
    }

  }

  // returns minimal completion time of the project
  // only works after a successful call to dijkstra()
  // runs in O(|V|) time
  public int minTime() {
    int minTime = 0;
    Task last = null;
    for (Task vertex : adjMap.keySet()) {
      if (vertex.earliestStart > minTime) {
        minTime = vertex.earliestStart;
        last = vertex;
      }
    }
    minTime += last.getTime();
    return minTime;
  }
}
