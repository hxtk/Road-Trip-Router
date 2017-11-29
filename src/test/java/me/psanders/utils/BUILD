java_library(
    name = "utils_tests",
    srcs = glob(["*.java"]),
    deps = [
        "//src/main/java/me/psanders/utils:utils",
        "@junit//jar"
    ],
    visibility = []
)

java_test(
    name = "LexicographicPermuterTest",
    size = "small",
    runtime_deps = [
        ":utils_tests"
    ]
)
