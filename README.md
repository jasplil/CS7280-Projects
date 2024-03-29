**Author**: Ting Tang & Wenqing Wu

# Project 2 design & proposal
### Basic units of the NoSQL Database
**The overview design is in the later section*
![Basic](https://github.com/jasplil/CS7280-P1/assets/39994190/5aad9931-452f-4f9b-88da-066f6811dcc9)
**Initialization**

Block size: 256 bytes

File size: 1,024 Kbytes, when the current file is full, an extra file of 1,024 Kbytes will be added

### Name of NoSQL database

DataBus

### Data selection

We will be using movie dataset from https://grouplens.org/datasets/movielens/

### Type of Index

B+tree: The database system would look up an index to find on which block the corresponding record resides, and then fetch the disk block. To get the appropriate *movie* record.

### Key (integer):

Yes

### Size of records

We will be using a fixed-length of records. Consider a set of *movie* data, each record (in pseudocode) is:

Assume that each character occupies 1 byte, then one record is 135 bytes long. We would use 135 bytes out of 256 bytes, and leave the rest unused. We will allocate

```
movieId *varchar* 0 (5): {
	title *varchar*: "Toy Story (1995)" (80)
	genre *varchar*: "Adventure|Children|Fantasy" (50)
}

movieId *varchar*: 1 (5): {
	title *varchar*: "Jumanji (1995):" (80)
	genre *varchar*: "Adventure|Children|Fantasy" (50)
}

```

### Allocation method

**Linked allocation**: On insertion of a new record, we use the record pointed to by the header. We update the header pointer to point to the next available record.

### Free block method

**Bit vector**: Locate the bit in the bit vector corresponding to the block being freed.
Set the bit value to 1. This indicates that the block is now available for allocation.

### Overview of NoSQL database design

The basic structure, allocation method, free block method are stated in the above sections.
To summarize:

**File Structure and Growth**

The database starts with a single file of 1,024 Kbytes.
The first block (header) stores the header information:
- Database name
- Directory (list of FCBs)
- Blocks status
- Index file info
- Others

As records are added and the initial file fills up, additional files of 1,024 Kbytes are created to accommodate new data.
The B+ tree index updates to reflect the location of blocks across all files, ensuring efficient data retrieval.

**B+ Tree Indexing**

This indexing facilitates quick searching, as the database system can directly find the block containing the desired record, minimizing disk I/O operations.

![Design](https://github.com/jasplil/CS7280-P1/assets/39994190/a8a39633-dfce-4837-b643-681620336b97)
### Developing environment

Language: java

IDE: Intellij

Github: current repo

----
# Project 1 Overview
Our B+ tree implementation can be used with any type of comparable keys and corresponding value

## Class explanation
1. Index Nodes(internal nodes): These are not a leaf node that contains keys that act as separators to search
all the way to the leaves. These keys don't contain actual data but reference deeper internal nodes
or leaf nodes.
2. internal child keys: these are the keys contained in the child nodes of an internal node, which does not contain tbe actual value
3. Leaf child keys: the bottommost nodes of a b+tree and contain the actual data or pointers to the data. and all these
nodes are linked together as a linked list, when we conduct search function, it looks through the leaf keys to find
the desired data.

here are 4 functions:
1. insertion: insert key-value pairs while maintaining the properties of B+ tree
2. search: retrieve values associated with specific keys
3. display tree: visualize the structure of the tree
4. display node: visualize the subtree structure under that node

to use an instance of the B+ tree:
run `BPlusTree<Integer, String> tree = new BPlusTree<>();`

to insert key-value pairs into the tree:
run `tree.insert(70, "Data3");`

search for a value using a key:
run `String value = tree.search(50);`

display subtree of a key:
run `tree.display(50);`

##  Requirements
JDK 8 or higher

## Run test
use BPlusTreeTest file



