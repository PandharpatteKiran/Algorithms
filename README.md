# Algorithms: 
1) Google's Page Rank
2) Klienberg's HITS Algorithm

The input for this problem is a graph represented through an adjacency list representation.
The command-line interface that is used is as follows. 
The first two of the three parameters hold integer values; the last parameter is a filename.

% ./pgrk     iterations initialvalue filename
% java pgrk  iterations initialvalue filename

-In order to run the algorithm we either run it for fixed number of iterations and iterations in command-line determines that, or for a fixed errorate of 10^-5 when the iteratons in command-line is set to 0. 
If iterations is 0, this algorthm runs till pagerank converges to the errorate value for all the nodes of the graph.
-The second parameter initialValue shows initial values of the ranks. If it is 0, all ranks are initialised to 0. if it is 1 they are initialized to 1. If it is -1, all ranks are initialised to 1/N and if it is -2 they are initialized to 1/sqrt(N), where N is number of web-pages (vertices of the graph).
-The third parameter filename is the input (directed) graph. It is in the form:
4 4
0 2
0 3
1 0
2 1
where first two numbers shows number of vertices and number of edges respectively. The following pairs are the directed edges of the graph in the form source->destination.
-Instructions to compile as mentioned below.
ex. 
java hits4006 15 -1 samplegraph.txt
java pgrk4006 15 -1 samplegraph.txt
=========================================================
