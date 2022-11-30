# Karger's mincut - Maxime Le Gal - User guide

## Introduction
There are two ways to test the implementation of the Karger's algorithm. The first one is with the [already implemented test class](#test-class), the other one is to implement your own tests by [using the main class](#main-class)

<br/>

---
## Test class
There is a JUnit 5  [test class](src/test/java/fr/istic/se/projet/KargerTest.java) with already four implemented graphs to test the Karger's algorithm with known graphs with known expected results. Each test is made in sequential and multithreading mode, and is validated if they both give the right result.

The last test is made to test the algorithm with much bigger graphs, where the number of edges and vertices can be selected and also the number of iterations/threads the algorithm must provide to find the mincut. By default there are 50 vertices and 2000 edges, and the algorithm is run 500 times.  
The number of iterations must be chosen regardly the number of edges and vertices in the graph. The greater the difference between the number of edges and vertices is, the greater the number of iteration must be to provide the right result with a greater probability.

<br/>

---
## Main class
This is a guide to let you know how to implement your own test of the Karger's mincut implementation with the provided graph class, by using the [Main.java](src/main/java/fr/istic/se/projet/Main.java) file. Here is explained how to use the Karger and Graph class to do so.

<br/>

### Graph class
The graphs are represented by the [class graph](src/main/java/fr/istic/se/projet/Graph.java) which I made. The class provide everything the Karger's algorithm needs to find the mincut.  
The only way to add a vertex inside the graph is to add an edge connected to this vertex. This way, there can not be isolated vertices in the graph, the graph is therefore connnected. The easiest way to add edges inside the graph is by using the method `addEdges(String[] listEdges)` But be careful, if an edge is not added to the graph, no exception is thrown, but the number of effectively added edges is returned, so the test should be on your side.

Each edge inside the `listEdges` parameter must be with the following format :
``` java
"labelVertex1 -- labelVertex2"
```

Example of implementation :
``` java
graph.addEdges(new String[]{"1 -- 2", "2 -- 3", "3 -- 4", "4 -- 1"});
```

<br/>

### Karger class
The [Karger class](src/main/java/fr/istic/se/projet/Karger.java) is a class which provide two static methods to find the mincut of a graph.

The main one is `int mincut(Graph graph, int numberOfIteration, boolean multithreading)`. It returns the mincut as an `int`.    
The `graph` parameter is the graph in which the mincut must be found.  
The `numberOfIteration` parameter determines the number of times the algorithm is played to find the mincut. This parameter depends on the size of the graphs.  
The `multitreading` parameter determines if the mincut is found in multithreading or sequential mode (true = multithreading).

The second one is `int mincut(Graph graph, int numberOfIteration)` and is an overload of the previous method, without the multithreading parameter. All it does is calling the previous method with the `multithreading` parameter set as `false`, so the mincut is found in sequential mode. It also returns the mincut as an `int`.

<br/>

### Main class
You can implement your own tests of the Karger's algorithm by using the [Main.java](src/main/java/fr/istic/se/projet/Main.java) file. You have to use the [graph class](#graph-class) to store your graphs in order to use the [Karger class](#karger-class). Instructions are reminded in the file with an example.