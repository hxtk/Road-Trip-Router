java_library(
    name = "pathfinder",
    srcs = glob(["*.java"]),
    deps = [
        "//src/main/java/me/psanders/graph:graph",
        "//src/main/java/me/psanders/graph/path:path",
        "@google_maps_services//jar",
        "@commons_cli//jar"
    ],
    visibility = [
        "//:__pkg__",
        "//src/test/java/me/psanders/graph/path:__pkg__"
    ]
)
