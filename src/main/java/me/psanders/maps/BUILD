java_library(
    name = "maps",
    srcs = glob(["*.java"]),
    deps = [
        "//src/main/java/me/psanders/graph:graph",
        "@google_maps_services//jar",
        "@commons_cli//jar"
    ],
    visibility = [
        "//:__pkg__",
        "//src/main/java/me/psanders:__pkg__",
        "//src/test/java/me/psanders/maps:__pkg__",
    ]
)
