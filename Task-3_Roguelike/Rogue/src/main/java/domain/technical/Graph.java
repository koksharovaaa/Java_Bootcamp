package domain.technical;

import domain.items.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Graph {
  ArrayList<Integer> graph[];

  @SuppressWarnings("unchecked")
  public Graph(int size) {
    graph = new ArrayList[size];
    for (int i = 0; i < graph.length; i++) {
      graph[i] = new ArrayList<>();
    }
  }

  public void fillGraph(ArrayList<Edge> corridors) {
    for (Edge c : corridors) {
      graph[c.r1].add(c.r2);
      graph[c.r2].add(c.r1);
    }
  }

  public int keyRoom(int src, int dst, ArrayList<Edge> c, HashMap<Integer, Item> k) {
    Set<Integer> visited = new HashSet<>();
    Queue<Integer> queue = new LinkedList<>();
    queue.add(src);
    visited.add(src);

    int parent[] = new int[graph.length];
    Arrays.fill(parent, -1);
    parent[src] = src;

    ArrayList<Integer> keyList = new ArrayList<>();
    while (!queue.isEmpty()) {
      int node = queue.poll();
      if (node == dst) {
        continue;
      }

      keyList.add(node);
      for (int next : graph[node]) {
        if (!visited.contains(next) && isCorridorOpen(node, next, c, k)) {
          visited.add(next);
          queue.add(next);
          parent[next] = node;
        }
      }
    }

    if (keyList.isEmpty()) {
      return -1;
    }

    if (keyList.size() == 1) {
      return dst;
    }

    keyList.removeFirst();
    int keyRoom = keyList.get((int) (Math.random() * keyList.size()));
    return keyRoom;
  }

  private boolean isCorridorOpen(int r1, int r2, ArrayList<Edge> c, HashMap<Integer, Item> k) {
    Edge corridor = null;
    for (Edge cr : c) {
      if (cr.r1 == r1 && cr.r2 == r2 || cr.r2 == r1 && cr.r1 == r2) {
        corridor = cr;
        break;
      }
    }

    if (corridor == null) {
      return false;
    }

    if (corridor.door == null) {
      return true;
    }

    for (HashMap.Entry<Integer, Item> e : k.entrySet()) {
      if (e.getValue().icon().colour == corridor.door.icon().colour) {
        return true;
      }
    }

    return false;
  }
}
