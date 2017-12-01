java_library(
    name = "utils_tests",
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
        ":utils_tests"
    ]
)
