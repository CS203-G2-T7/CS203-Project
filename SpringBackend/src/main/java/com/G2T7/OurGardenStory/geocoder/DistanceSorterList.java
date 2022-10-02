package com.G2T7.OurGardenStory.geocoder;

public class DistanceSorterList {
    
    private static class DistanceSorter {
        private double distance;
        private DistanceSorter next;
    
        public DistanceSorter(double distance, DistanceSorter next) {
            this.distance = distance;
            this.next = next;
        }
    
        public double getDistance() {
            return distance;
        }
    
        public DistanceSorter getNext() {
            return next;
        }
    
        public void setNext(DistanceSorter n) {
            next = n;
        }
    
    }

    private DistanceSorter head = null;
    private DistanceSorter tail = null;
    private int size = 0;

    public DistanceSorterList() {

    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public double first() {
        return head.getDistance();
    }

    public double last() {
        return tail.getDistance();
    }

    public void addFirst(double distance) {
        head = new DistanceSorter(distance, head);

        if (size == 0) {
            tail = head;
        }

        size++;
    }

    public void addLast(double distance) {
        DistanceSorter newest = new DistanceSorter(distance, null);

        if (isEmpty()) {
            head = newest;
        } else {
            tail.setNext(newest);
        }

        tail = newest;
        size++;
    }

    public double removeFirst() {
        if (isEmpty()) {
            return 0.0;
        }

        double answer = head.getDistance();
        head = head.getNext();

        size--;
        if (size == 0) {
            tail = null;
        }

        return answer;
    }

    public double removeLast() {
        if (isEmpty()) {
            return 0.0;
        }

        double answer = tail.getDistance();

        if (head == tail) {
            head = null;
            tail = null;
        } else {
            DistanceSorter walk = head;
            while(walk.getNext() != tail) {
                walk = walk.getNext();
            }
            walk.setNext(null);
            tail = walk;
        }
        size--;
        return answer;
    }

    //TODO: need to implement the add and remove methods for any parituclar distance in the linkedlist

    public void sortList()
    {

        // Node current will point to head
        DistanceSorter current = head;
        DistanceSorter index = null;

        double temp;

        if (head == null) {
            return;
        }
        else {
            while (current != null) {
                // Node index will point to node next to
                // current
                index = current.next;

                while (index != null) {
                    // If current node's data is greater
                    // than index's node data, swap the data
                    // between them
                    if (current.distance > index.distance) {
                        temp = current.distance;
                        current.distance = index.distance;
                        index.distance = temp;
                    }

                    index = index.next;
                }
                current = current.next;
            }
        }
    }

    public void add(double distance) {
        DistanceSorter new_node = new DistanceSorter(distance, null);

        //If the Linked List is empty, then make the new node as head
        if (head == null) {
            head = new DistanceSorter(distance, null);
            return;
        }

        //Else traverse till the last node
        DistanceSorter current = head;
        while (current.next != null) {
            current = current.next;
        }

        //Change the next of last node
        current.next = new_node;
        size++;

        sortList();
    }

    public void remove(double distance) {

        DistanceSorter temp = head;
        DistanceSorter prev = null;

        // Delete head node
        if (temp != null && temp.distance == distance) {
            head = temp.next; // Changed head
            return;
        }

        //delete node that is not head or tail
        while (temp != null && temp.distance != distance) {
            prev = temp;
            temp = temp.next;
        }

        // If distance is not inside the linked list
        if (temp == null) {
            return;
        }
        // Unlink the node from linked list
        prev.next = temp.next;
        size--;

        sortList();
    }


}
