public class BinarySearchTree<T extends Comparable<T>> {
  private BinaryNode root;
  private int size;

  public BinarySearchTree() {
    this.root = null;
    this.size = 0;
  }

  public boolean isEmpty() {
    return root == null;
  }

  // returns size
  public int size() {
    return this.size;
  }

  // returns smallest item in the tree
  public T findMin() {
    return findMin(root).data;
  }

  // returns largest item in the tree
  public T findMax() {
    return findMax(root).data;
  }

  // method for insertion of data into the tree
  public void insert(T item) {
    root = insert(item, root);
    size++;
  }

  // confirms if certain data is present in the tree
  public boolean search(T item) {
    return search(item, root);
  }

  // method for deletion of data from the tree
  public void delete(T item) {
    root = delete(item, root);
    if (!isEmpty())
      size--;
  }

  // recursive helper method for insertion of data
  private BinaryNode insert(T item, BinaryNode node) {
		if (node == null) {
      return new BinaryNode(item);
    }

    int comparison = item.compareTo(node.data);
    if (comparison < 0)
      node.left = insert(item, node.left);
    else if (comparison > 0)
      node.right = insert(item, node.right);
    return node;
  }

  // recursive helper method, confirms if an item is present in a subtree
  private boolean search (T item, BinaryNode node) {
    if (node == null)
      return false;
    int comparison = item.compareTo(node.data);
    if (comparison < 0)
      return search(item, node.left);
    else if (comparison > 0)
      return search(item, node.right);
    else
      return true;
  }

  // recursive helper method, finds largest node in subtree rooted by parameter
  private BinaryNode findMax (BinaryNode node) {
    if (node == null)
      return null;
    if (node.right == null)
      return node;
    return findMax(node.right);
  }

  // recursive helper method, finds smallest node in subtree rooted by parameter
  private BinaryNode findMin (BinaryNode node) {
    if (node == null)
      return null;
    if (node.left == null)
      return node;
    return findMin(node.left);
  }

  // recursive helper method used in "delete" method
  // deletes smallest node in subtree with parameter as root
  private BinaryNode deleteMin (BinaryNode node) {
    if (node == null)
      return null;
    if (node.left == null) {
      BinaryNode temp = node;
      node = null;
      return temp;
    }
    return deleteMin(node.left);
  }

  // another recursive helper method used in "delete" method
  private BinaryNode delete(T item, BinaryNode node) {
    if (node == null)
      return node; // if data to be deleted not found

    int comparison = item.compareTo(node.data);
    if (comparison < 0)
      node.left = delete(item, node.left);
    else if (comparison > 0)
      node.right = delete(item, node.right);

    // node found at this point
    else if (node.left != null && node.right != null) // node has two children
      node.data = deleteMin(node.right).data; // replacement by minimum in right subtree
    else // one child or no children
      node = (node.left != null) ? node.left : node.right; // replacement by child or null
    return node;
  }

  // sets depth for each node in the tree
  public void setDepths() {
    if (root != null) root.assignDepth(0);
  }

  // returns the depth of a node in the tree with the biggest depth value
  public int getMaxDepth() {
    return root.maxDepth(-1);
  }

  // counts the number of node at each depth
  public int[] getDepthCounts() {
    int[] depthsArray = new int[getMaxDepth()+1];
    return root.countDepths(depthsArray);
  }

  // returns average depth of all nodes
  public double getAvgDepth() {
    return (double) root.sumDepth(0)/this.size;
  }

  // inner node class
  class BinaryNode {
    T data;
    int depth;
    BinaryNode left; // left child
    BinaryNode right; // right child

    BinaryNode(T data, BinaryNode left, BinaryNode right) {
      this.data = data;
      this.depth = -1;
      this.left = left;
      this.right = right;
    }

    BinaryNode(T data) {
      this(data, null, null);
    }

    // assigns depth to all nodes in the subtree rooted by this node
    void assignDepth(int depth) {
     this.depth = depth;
     if (left != null) left.assignDepth(depth+1);
     if (right != null) right.assignDepth(depth+1);
    }

    // returns depth of this node
    int getDepth() {
      return this.depth;
    }

    // returns maximum depth found in subtree rooted by this node
    int maxDepth(int max) {
      int leftDepth = (left != null) ? left.maxDepth(left.depth) : this.depth;
      int rightDepth = (right != null) ? right.maxDepth(right.depth) : this.depth;
      return Math.max(leftDepth, rightDepth);
    }

    // sums depths off all nodes
    int sumDepth(int sum) {
      int leftSum = (left != null) ? left.sumDepth(0) : 0;
      int rightSum = (right != null) ? right.sumDepth(0) : 0;
      return this.depth + leftSum + rightSum;
    }

    // counts number of nodes at each depth in subtree rooted by this node
    int[] countDepths(int[] depthsArray) {
      if (left != null) depthsArray = left.countDepths(depthsArray);
      if (right != null) depthsArray = right.countDepths(depthsArray);
      depthsArray[this.depth]++;
      return depthsArray;
    }
  }

}
