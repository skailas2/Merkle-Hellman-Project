public class ObjectNode
{
    private Object data;
    private ObjectNode link;


    /**
     * Initialize a node with a specified initial data and link to the next
     * node. Note that the initialLink may be the null reference,
     * which indicates that the new node has nothing after it.
     * @param initialData
     *   the initial data of this new node
     * @param initialLink
     *   a reference to the node after this new node--this reference may be null
     *   to indicate that there is no node after this new node.
     * @postcondition
     *   This node contains the specified data and link to the next node.
     **/
    public ObjectNode(Object initialData, ObjectNode initialLink)
    {
        data = initialData;
        link = initialLink;
    }

    /**
     * Modification method to add a new node after this node.
     * @param item
     *   the data to place in the new node
     * @postcondition
     *   A new node has been created and placed after this node.
     *   The data for the new node is item. Any other nodes
     *   that used to be after this node are now after the new node.
     * @exception OutOfMemoryError
     *   Indicates that there is insufficient memory for a new
     *   ObjectNode.
     **/
    public void addNodeAfter(Object item)
    {
        link = new ObjectNode(item, link);
    }


    /**
     * Accessor method to get the data from this node.
     * @return
     *   the data from this node
     **/
    public Object getData( )
    {
        return data;
    }

    /**
     * Accessor method to get a reference to the next node after this node.
     * @return
     *   a reference to the node after this node (or the null reference if there
     *   is nothing after this node)
     **/

    //The Big Theta (1)
    public ObjectNode getLink()
    {
        return link;
    }

    public String toString(ObjectNode head){
        StringBuilder sb= new StringBuilder();
        ObjectNode cur=head;
        while(cur!=null){
            sb.append(cur.getData());
            cur=cur.link;
        }
        return sb.toString();
    }

    /**
     * Copy a list.
     * @param source
     *   the head of a linked list that will be copied (which may be
     *   an empty list in where source is null)
     * @return
     *   The method has made a copy of the linked list starting at
     *   source. The return value is the head reference for the
     *   copy.
     * @exception OutOfMemoryError
     *   Indicates that there is insufficient memory for the new list.
     **/
    public static ObjectNode listCopy(ObjectNode source)
    {
        ObjectNode copyHead;
        ObjectNode copyTail;

        // Handle the special case of the empty list.
        if (source == null)
            return null;

        // Make the first node for the newly created list.
        copyHead = new ObjectNode(source.data, null);
        copyTail = copyHead;

        // Make the rest of the nodes for the newly created list.
        while (source.link != null)
        {
            source = source.link;
            copyTail.addNodeAfter(source.data);
            copyTail = copyTail.link;
        }

        // Return the head reference for the new list.
        return copyHead;
    }

    public static ObjectNode listCopy_rec(ObjectNode source){
        if(source==null)
            return null;

        ObjectNode node= new ObjectNode(source.data,null);
        node.link=listCopy_rec(source.link);
        return node;
    }


    /**
     * Copy a list, returning both a head and tail reference for the copy.
     * @param source
     *   the head of a linked list that will be copied (which may be
     *   an empty list in where source is null)
     * @return
     *   The method has made a copy of the linked list starting at
     *   source.  The return value is an
     *   array where the [0] element is a head reference for the copy and the [1]
     *   element is a tail reference for the copy.
     * @exception OutOfMemoryError
     *   Indicates that there is insufficient memory for the new list.
     **/
    public static ObjectNode[ ] listCopyWithTail(ObjectNode source)
    {
        ObjectNode copyHead;
        ObjectNode copyTail;
        ObjectNode[ ] answer = new ObjectNode[2];

        // Handle the special case of the empty list.
        if (source == null)
            return answer; // The answer has two null references .

        // Make the first node for the newly created list.
        copyHead = new ObjectNode(source.data, null);
        copyTail = copyHead;

        // Make the rest of the nodes for the newly created list.
        while (source.link != null)
        {
            source = source.link;
            copyTail.addNodeAfter(source.data);
            copyTail = copyTail.link;
        }

        // Return the head and tail references.
        answer[0] = copyHead;
        answer[1] = copyTail;
        return answer;
    }


    /**
     * Compute the number of nodes in a linked list.
     * @param head
     *   the head reference for a linked list (which may be an empty list
     *   with a null head)
     * @return
     *   the number of nodes in the list with the given head
     * @note
     *   A wrong answer occurs for lists longer than Int.MAX_VALUE.
     **/
    public static int listLength(ObjectNode head)
    {
        ObjectNode cursor;
        int answer;

        answer = 0;
        for (cursor = head; cursor != null; cursor = cursor.link)
            answer++;

        return answer;
    }
    public static int listLength_rec(ObjectNode head){
        if (head==null){
            return 0;
        }
        return 1+listLength_rec(head.link);
    }

    public void displayEveryThird(){
        int count=0;
        ObjectNode node=this;
        while (node!=null){
            count+=1;
            if (count%3==0){
                System.out.println(node.data);
            }
            node=node.link;
        }

    }



    /**
     * Copy part of a list, providing a head and tail reference for the new copy.
     * @precondition
     *   start and end are non-null references to nodes
     *   on the same linked list,
     *   with the start node at or before the end node.
     * @return
     *   The method has made a copy of the part of a linked list, from the
     *   specified start node to the specified end node. The return value is an
     *   array where the [0] component is a head reference for the copy and the
     *   [1] component is a tail reference for the copy.
     * @param start
     *   first node to copy
     * @param end
     *   final node to copy
     * @exception IllegalArgumentException
     *   Indicates that start and end are not references
     *   to nodes on the same list.
     * @exception NullPointerException
     *   Indicates that start is null.
     * @exception OutOfMemoryError
     *   Indicates that there is insufficient memory for the new list.
     **/
    public static ObjectNode[ ] listPart(ObjectNode start, ObjectNode end)
    {
        ObjectNode copyHead;
        ObjectNode copyTail;
        ObjectNode cursor;
        ObjectNode[ ] answer = new ObjectNode[2];

        // Make the first node for the newly created list. Notice that this will
        // cause a NullPointerException if start is null.
        copyHead = new ObjectNode(start.data, null);
        copyTail = copyHead;
        cursor = start;

        // Make the rest of the nodes for the newly created list.
        while (cursor != end)
        {
            cursor = cursor.link;
            if (cursor == null)
                throw new IllegalArgumentException
                        ("end node was not found on the list");
            copyTail.addNodeAfter(cursor.data);
            copyTail = copyTail.link;
        }

        // Return the head and tail references
        answer[0] = copyHead;
        answer[1] = copyTail;
        return answer;
    }


    /**
     * Find a node at a specified position in a linked list.
     * @param head
     *   the head reference for a linked list (which may be an empty list in
     *   which case the head is null)
     * @param position
     *   a node number
     * @precondition
     *   position &gt; 0.
     * @return
     *   The return value is a reference to the node at the specified position in
     *   the list. (The head node is position 1, the next node is position 2, and
     *   so on.) If there is no such position (because the list is too short),
     *   then the null reference is returned.
     * @exception IllegalArgumentException
     *   Indicates that position is not positive.
     **/
    public static ObjectNode listPosition(ObjectNode head, int position)
    {
        ObjectNode cursor;
        int i;

        if (position <= 0)
            throw new IllegalArgumentException("position is not positive");

        cursor = head;
        for (i = 1; (i < position) && (cursor != null); i++)
            cursor = cursor.link;

        return cursor;
    }


    /**
     * Search for a particular piece of data in a linked list.
     * @param head
     *   the head reference for a linked list (which may be an empty list in
     *   which case the head is null)
     * @param target
     *   a piece of data to search for
     * @return
     *   The return value is a reference to the first node that contains the
     *   specified target. If there is no such node, the null reference is
     *   returned.
     **/
    public static ObjectNode listSearch(ObjectNode head, Object target)
    {
        ObjectNode cursor;

        for (cursor = head; cursor != null; cursor = cursor.link)
            if (target == cursor.data)
                return cursor;

        return null;
    }


    /**
     * Modification method to remove the node after this node.
     * @precondition
     *   This node must not be the tail node of the list.
     * @postcondition
     *   The node after this node has been removed from the linked list.
     *   If there were further nodes after that one, they are still
     *   present on the list.
     * @exception NullPointerException
     *   Indicates that this was the tail node of the list, so there is nothing
     *   after it to remove.
     **/
    public void removeNodeAfter( )
    {
        link = link.link;
    }


    /**
     * Modification method to set the data in this node.
     * @param newData
     *   the new data to place in this node
     * @postcondition
     *   The data of this node has been set to newData.
     **/
    public void setData(Object newData)
    {
        data = newData;
    }


    /**
     * Modification method to set the link to the next node after this node.
     * @param newLink
     *   a reference to the node that should appear after this node in the linked
     *   list (or the null reference if there is no node after this node)
     * @postcondition
     *   The link to the node after this node has been set to newLink.
     *   Any other node (that used to be in this link) is no longer connected to
     *   this node.
     **/
    public void setLink(ObjectNode newLink)
    {
        link = newLink;
    }

    public static void main(String[] args) {
        ObjectNode head= new ObjectNode('a',null);
        ObjectNode cur= head;
        for (char c='b';c<='z';c++){
            cur.link=new ObjectNode(c,null);
            cur=cur.link;
        }
        System.out.println("Linked List: " + head.toString());
        head.displayEveryThird();

        int sizeIterative = ObjectNode.listLength(head);
        System.out.println("Size of the list (iterative): " + sizeIterative);

        int sizeRecursive = ObjectNode.listLength_rec(head);
        System.out.println("Size of the list (recursive): " + sizeRecursive);

        ObjectNode k = ObjectNode.listCopy(head);
        System.out.println("Copied List: " + k.toString());

        System.out.println("Number of nodes in k = " + ObjectNode.listLength(k));
        System.out.println("Number of nodes in k = " + ObjectNode.listLength_rec(k));

        ObjectNode k2 = ObjectNode.listCopy_rec(head);

        System.out.println(k2.toString());

        System.out.println("Number of nodes in k2 = " + ObjectNode.listLength(k2));
        System.out.println("Number of nodes in k2 = " + ObjectNode.listLength_rec(k2));

    }
}
class SinglyLinkedList {
    private ObjectNode head;
    private ObjectNode tail;
    private ObjectNode iterator;

    public SinglyLinkedList() {
        head = null;
        tail = null;
        iterator = null;
    }
    public void add(Object data) {
        ObjectNode newNode = new ObjectNode(data, null);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setLink(newNode);
            tail = newNode;
        }
    }
    public void reset() {
        iterator = head;
    }

    public boolean hasNext() {
        return iterator != null;
    }

    public Object next() {
        if (!hasNext()) {
            throw new IllegalStateException("No more elements to iterate.");
        }
        Object data = iterator.getData();
        iterator = iterator.getLink();
        return data;
    }
    /**
     * Displays the contents of the list.
     */
    public void display() {
        ObjectNode current = head;
        while (current != null) {
            System.out.print(current.getData() + " -> ");
            current = current.getLink();
        }
        System.out.println("null");
    }

    /**
     * Returns the size of the list.
     *
     * @return The number of nodes in the list.
     */
    public int size() {
        return ObjectNode.listLength(head);
    }

    public static void main(String[] args) {
        // Test the SinglyLinkedList class
        SinglyLinkedList list = new SinglyLinkedList();

        // Add elements to the list
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");


        System.out.println("List contents:");
        list.display();

        System.out.println("Size of the list: " + list.size());

        System.out.println("Iterating through the list:");
        list.reset();
        while (list.hasNext()) {
            System.out.println(list.next());
        }
    }
}

class OrderedLinkedListOfIntegers{
    private ObjectNode head;
    public OrderedLinkedListOfIntegers(){
        ObjectNode head =null;
        ObjectNode link =null;

    }
    public void sortedAdd(int data){
        ObjectNode newnode=new ObjectNode(data,null);
        if (head==null){
            head=newnode;
        }
        else{
            ObjectNode cur=head;
            ObjectNode prev=null;

            if (data<(Integer)cur.getData()){
                newnode.setLink(cur);
                head=newnode;


            }

            else{
                while(cur!=null && (Integer)cur.getData()>=data)
                {
                    prev=cur;
                    cur=cur.getLink();
                }
                prev.setLink(newnode);
                newnode.setLink(cur);
            }}
    }

    public static ObjectNode merge(ObjectNode head1,ObjectNode head2)
    {
        ObjectNode dummy= new ObjectNode(0,null);
        ObjectNode cur= dummy;
        while(head1!=null &&  head2!=null){
            if ((Integer)head1.getData()<=(Integer)head2.getData()){
                cur.setLink(head1);
                cur=cur.getLink();
                head1=head1.getLink();
            }
            else{
                cur.setLink(head2);
                cur=cur.getLink();
                head2=head2.getLink();
            }
            if (head1==null){
                cur.setLink(head2);

            }

            else if (head2==null){
                cur.setLink(head1);
            }
        }
        return dummy.getLink();
    }
}
