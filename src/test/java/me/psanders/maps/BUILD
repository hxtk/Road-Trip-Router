java_library(
    name = "maps_tests",
    srcs = glob(["*.java"]),
    deps = [
        "//src/main/java/me/psanders/maps:maps",
        "@google_maps_services//jar",
        "@commons_cli//jar",
        "@junit//jar",
    ],
    visibility = []
)

java_test(
    name = "LocationGraphFactoryTest",
    size = "small",
    runtime_deps = [
        ":maps_tests",
        "@gson//jar",
        "@okhttp//jar",
        "@okio//jar",
        "@slf4j_api//jar",
        "@slf4j_simple//jar",
    ]
)
