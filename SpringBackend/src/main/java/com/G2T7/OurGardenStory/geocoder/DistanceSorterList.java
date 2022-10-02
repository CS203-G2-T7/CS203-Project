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

    public void add(double distance) {

    }

    public void remove(double distance) {
        
    }


}
