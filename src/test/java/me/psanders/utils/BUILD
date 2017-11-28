java_library(
    name = "utils_tests",
    srcs = glob(["*.java"]),
    deps = [
        "//src/main/java/me/psanders/utils:utils",
        "@junit//jar"
    ],
    visibility = [
        "//src/test/java/me/psanders/utils:__pkg__"
    ]
)


java_test(
    name = "LexicographicPermuterTest",
    size = "small",
    runtime_deps = [
        ":utils_tests"
    ]
)
