# TODO
    Protoype app
        need A* with map you created or assigned
        gotta draw

    A* will be public
        it will have HashMap of nodes it can view <String ID, Node node>
        nodes will have all the nodes they can get to in an edges list
        dependency injection will make it so the A* list of nodes is updated whenever needed
        otherwise A* only needs to access its own
        Node as interface
            actual node has data then getters and setters and stuff
        get distance becomes helper in A*


        EVENTUALLY - not now
        A* throws NoPathException