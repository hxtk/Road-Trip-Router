java_binary(
    name = "RoutePlanner",
    main_class = "me.psanders.TspSolver",
    srcs = ["src/main/java/me/psanders/TspSolver.java"],
    deps = [
        "//src/main/java/me/psanders/graph:graph",
        "//src/main/java/me/psanders/graph/pathfinder:pathfinder",
        "@google_maps_services//jar"
    ],
    runtime_deps = [
        "@joda_time//jar",
        "@gson//jar",
        "@okhttp//jar",
        "@okio//jar",
        "@slf4j//jar",
    ]
)
