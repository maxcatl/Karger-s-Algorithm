package fr.istic.se.projet;

import java.util.logging.Level;

import static java.util.logging.Logger.getGlobal;

public class Main
{
    public static void main(String[] args)
    {
        //Create a graph and add edges
        Graph graph = new Graph();
        graph.addEdges(new String[]{"1 -- 2", "2 -- 3", "3 -- 4", "4 -- 1"});

        //Find the mincut in sequential and multithreading mode and print it
        //if the mincuts found are not the same, increase the number of iterations
        getGlobal().log(Level.INFO, "sequential mincut found : {0}", Karger.minCut(graph, 50));
        getGlobal().log(Level.INFO, "multithreading mincut found : {0}", Karger.minCut(graph, 50, true));
    }
}
