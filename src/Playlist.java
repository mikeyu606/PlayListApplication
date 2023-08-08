/*  This class represents a Playlist of podcast episodes, where each
/*  episode is implemented as an object of type Episode. A user navigating
/*  a Playlist should be able to move between songs using next or previous references.
/*
/*  To enable flexible navigation, the Playlist is implemented as
/*  a Doubly Linked List where each episode has a link to both
/*  the next and the prev episodes in the list.
*/
import java.util.*;

public class Playlist {
    private Episode head;
    private int size;

    public Playlist() {
        head = null;
        size = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    // Ensure that "size" is updated properly in other methods, to always
    // reflect the correct number of episodes in the current Playlist
    public int getSize() {
        return size;
    }

    // Our implementation of toString() displays the Playlist forward,
    // starting at the first episode (i.e. head) and ending at the last episode,
    // while utilizing the "next" reference in each episode
    @Override
    public String toString() {
        String output = "[HEAD] ";
        Episode current = head;
        if (!isEmpty()) {
            while (current.next != null) {
                output += current + " -> ";
                current = current.next;
            }
            output += current + " [END]\n";
        } else {
            output += " [END]\n";
        }
        return output;
    }


    // This method displays the Playlist backward, starting at
    // the last episode and ending at the first episode (i.e. head),
    // while utilizing the "prev" reference in each episode
    public String toReverseString() {
        String output = "[END] ";
        Episode current = head;
        if (!isEmpty()) {
            while (current.next != null)
                current = current.next;
            // current is now pointing to last node

            while (current.prev != null) {
                output += current + " -> ";
                current = current.prev;
            }
            output += current + " [HEAD]\n";
        } else {
            output += " [HEAD]\n";
        }
        return output;
    }


    /**************************************************************/
    // A4 Part 1 Methods (Add/Delete Operations)
    public void addFirst(String title, double duration) { //this method adds an Episode to the beginning of the Doubly Linked List
        Episode newEpisode = new Episode(title, duration, head, null); //title, duration, after, before
        if (head == null) {
            head = newEpisode;
        } else {
            head.prev = newEpisode;
            head = newEpisode; //make the newEpisode the new head or first Node
        }
        size++; //increment the size since we are adding a node
    }

    public void addLast(String title, double duration) { //this method adds an Episode to the end of the Doubly Linked List
        if (isEmpty()) { //we check if the Linked List is empty in the first place
            Episode lastEpisode = new Episode(title, duration, null, null);
            head = lastEpisode;
        } else {
            Episode last = head; //represents the last Episode
            while (last.next != null) {
                last = last.next;
            }
            Episode lastEpisode = new Episode(title, duration, null, last);
            last.next = lastEpisode;
            lastEpisode.prev = last;
        }
        size++;// increment the size by one because we added a new Episode
    }

    //this method deletes and returns the first element in the Doubly Linked List
    public Episode deleteFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        Episode first = head; //represents the current head
        if (isEmpty()) { //means only contains one element because we already checked above if empty
            head = null; //remove by setting to null;
            size--;
            return first;
        }
        head = head.next;
        head.prev = null;
        size--;
        return first;
    }

    // This method returns the last element
    public Episode deleteLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (head.next == null) {
            Episode recent = head;
            head = null;
            return recent; //return the deleted element
        }
        Episode last = head;// holds the last element
        while (last.next != null) { //makes last equal to the last node
            last = last.next;
        }
        Episode deleted = last; //holds the current last Episode
        last = last.prev; //removes the current last Episode
        last.next = null;
        return deleted;
    }

    // This method takes in a title name and deletes and returns that Episode
    public Episode deleteEpisode(String title) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } //if empty throw exception

        Episode current = head;
        Episode beforeCurrent = head;
        if (current.title.equals(title)) {
            head = current.next;
        }
        //so we keep track of two Episodes until it equals title
        while (current != null && !current.title.equals(title)) {
            beforeCurrent = current;
            current = current.next;
        }
        beforeCurrent.next = current.next; //deletes current Episode
        return current; //return the deleted element
    }


    /***************************************************************/
    // A4 Part 2 Methods (Sorting the Playlist using MergeSort)

    // merge the two episodes alphabetically
    public Episode merge(Episode a, Episode b) {
        if (a == null) {
            return b;
        } //this is to check if we have exhausted all elements of a
        if (b == null) {
            return a;
        }

        Episode current; //we keep a current pointer to keep track of the titles in order
        if (a.title.compareTo(b.title) > 0) { //check if b is smaller than a, so out of order
            current = b; //the smaller element is set to the header
            current.next = merge(a, b.next); //we "increment" and move the header forward and check with the "bigger" title
            current.next.prev = current; //we set the previous node equal to
        } else {
            current = a; //same logic applies from above
            current.next = merge(a.next, b);
            current.next.prev = current;
        }
        return current;
    }


    // Finds the middle episode of the list that begins at the passed node reference
    private Episode getMiddleEpisode(Episode node) {
        if (node == null) return node;
        Episode slow = node;
        Episode fast = node;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // MergeSort starting point
    public void mergeSort() {
        if (isEmpty()) throw new RuntimeException("Cannot sort empty list.");
        head = sort(head);
    }

    // Recursively splits the list starting at a given node reference
    public Episode sort(Episode node) {
        if (node == null || node.next == null)
            return node;
        Episode middle = getMiddleEpisode(node); //get the middle of the list
        Episode left_head = node;
        Episode right_head = middle.next;

        // split the list into two halves:
        if (right_head != null) right_head.prev = null;
        middle.next = null;

        Episode left = sort(left_head);
        Episode right = sort(right_head);
        return merge(left, right);
    }
}

