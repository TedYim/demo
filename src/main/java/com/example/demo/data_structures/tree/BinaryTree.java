package com.example.demo.data_structures.tree;

public class BinaryTree {

    public BinaryTree() {
        this.init();
    }

    private void init() {
        TreeNode nodeA = new TreeNode(1, "A");
        TreeNode nodeB = new TreeNode(2, "B");
        TreeNode nodeC = new TreeNode(3, "C");
        TreeNode nodeD = new TreeNode(4, "D");
        TreeNode nodeE = new TreeNode(5, "E");
        TreeNode nodeF = new TreeNode(6, "F");
        nodeA.setLeftChild(nodeB);
        nodeA.setRightChild(nodeC);
        nodeB.setLeftChild(nodeD);
        nodeB.setRightChild(nodeE);
        nodeC.setLeftChild(nodeF);

    }




    private class TreeNode {
        private Integer  index;
        private Object   msg;
        private TreeNode leftChild;
        private TreeNode rightChild;

        public TreeNode(Integer index, Object msg) {
            this.index = index;
            this.msg = msg;
        }

        public TreeNode getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(TreeNode leftChild) {
            this.leftChild = leftChild;
        }

        public TreeNode getRightChild() {
            return rightChild;
        }

        public void setRightChild(TreeNode rightChild) {
            this.rightChild = rightChild;
        }
    }
}
