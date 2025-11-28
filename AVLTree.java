package Project_212;

public class AVLTree<T> {

	private AVLNode<T> root;

	public AVLTree() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	// i know that we have a height attribute but in case the node is null we most
	// check every time unlike using this method
	private int height(AVLNode<T> node) {
		return (node == null) ? -1 : node.height;
	}

	// same as math.max but in case they don't want us to use it
	private int max(int a, int b) {
		return (a > b) ? a : b;
	}

	// RR Rotation
	private AVLNode<T> rrRotation(AVLNode<T> A) {
		AVLNode<T> B = A.right;
		A.right = B.left;
		B.left = A;

		A.height = max(height(A.left), height(A.right)) + 1;
		B.height = max(height(B.right), A.height) + 1;

		return B;
	}

	// LL Rotation
	private AVLNode<T> llRotation(AVLNode<T> A) {
		AVLNode<T> B = A.left;
		A.left = B.right;
		B.right = A;

		A.height = max(height(A.left), height(A.right)) + 1;
		B.height = max(height(B.left), A.height) + 1;

		return B;
	}

	// LR Rotation
	private AVLNode<T> lrRotation(AVLNode<T> A) {
		A.left = rrRotation(A.left);
		return llRotation(A);
	}

	// RL Rotation
	private AVLNode<T> rlRotation(AVLNode<T> A) {
		A.right = llRotation(A.right);
		return rrRotation(A);
	}

	// the insert method takes two nodes the newNode and the root so we prepare
	// newNode in this public method
	public void insert(int key, T data) {
		AVLNode<T> newNode = new AVLNode<>(key, data);
		root = insert(newNode, root);
	}

	private AVLNode<T> insert(AVLNode<T> newNode, AVLNode<T> r) {
		if (r == null) {
			r = newNode;
		} else if (newNode.key < r.key) {
			r.left = insert(newNode, r.left);

			if (height(r.right) - height(r.left) == -2) {
				if (newNode.key < r.left.key)
					r = llRotation(r);
				else
					r = lrRotation(r);
			}

		} else if (newNode.key > r.key) {
			r.right = insert(newNode, r.right);

			if (height(r.right) - height(r.left) == 2) {
				if (newNode.key > r.right.key)
					r = rrRotation(r);
				else
					r = rlRotation(r);
			}

		} else {

		}

		r.height = max(height(r.left), height(r.right)) + 1;
		return r;
	}

	// kinda similar to the insert, user gives key only but
	// we need a node (root) to start searching
	public T search(int key) {
		AVLNode<T> node = searchNode(root, key);
		return (node == null) ? null : node.data;
	}

	private AVLNode<T> searchNode(AVLNode<T> r, int key) {
		if (r == null)
			return null;
		if (key < r.key)
			return searchNode(r.left, key);
		if (key > r.key)
			return searchNode(r.right, key);
		return r;
	}

	// InOrder print
	public void inOrderPrint() {
		inOrder(root);
	}

	private void inOrder(AVLNode<T> r) {
		if (r == null)
			return;
		inOrder(r.left);
		System.out.println("Key: " + r.key + " Data: " + r.data);
		inOrder(r.right);
	}
}
