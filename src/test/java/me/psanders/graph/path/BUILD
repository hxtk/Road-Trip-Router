java_library(
    name = "optimization_tests",
    srcs = glob(["*.java"]),
    deps = [
        "//src/main/java/me/psanders/graph:graph",
        "//src/main/java/me/psanders/graph/path:path",
        "@junit//jar"
    ],
    visibility = []
)

java_test(
    name = "GeneticOptimizationStrategyTest",
    size = "large",
    runtime_deps = [
        ":optimization_tests"
    ]
)
