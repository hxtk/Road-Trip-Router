# Road Trip Router [![Build Status](https://travis-ci.org/hxtk/Road-Trip-Router.png?branch=master)](https://travis-ci.org/hxtk/Road-Trip-Router) [![Code Climate](https://codeclimate.com/github/hxtk/Road-Trip-Router/badges/gpa.svg)](https://codeclimate.com/github/hxtk/Road-Trip-Router)

Using genetic algorithms, we build a route that is *probably* almost optimal for visiting several places.

## Installing / Getting started

Install [Bazel](https://bazel.build/) and compile this project by running `bazel build RoutePlanner`.

## Developing

We try to follow the [Google Java Styleguide](https://google.github.io/styleguide/javaguide.html), but it is not necessary to be perfect. Feel free to submit pull requests or issues regarding code style and idioms if they are strictly incorrect, else prefer consistency with code already found.

Bazel `BUILD` and `WORKSPACE` files should follow the best practices defined at [Bazel and Java](https://docs.bazel.build/versions/master/bazel-and-java.html) and [Best Practices](https://docs.bazel.build/versions/master/best-practices.html).

Finally, the entry point is in `me.psanders.TspSolver`.

### Deploying / Publishing

You must supply your own Google Maps API key under the `--key` flag, as I cannot afford to support the large number of API requests this generates ;) (`O(n²)` as the number of destinations).

```BASH
java -jar RoutePlanner_deploy.jar --key "..."
```

## Features

### Finished

- Find a pseudo-optimal path by distance using a genetic algorithm
- Flags for various route parameters (i.e., "avoid tolls/ferries/highways",
	cost by time instead of distance

### In Progress

- Compound flags, e.g., `--scenic` or `-S` avoids highways/tolls and sorts by distance (open issues for compound flags you would like to see).

### Road Map

> Nothing to see here.

## Contributing

As mentioned in **Developing**, please try to follow the Google Java Style guide roughly. We're not Google, so there is no need to be completely strict.

Be aware that the maintainer reserves the right to deny pull requests. To make sure this doesn't happen to you, please open an issue and get an okay from the maintainer before putting your time into a feature you would like to see added. Bug fix and test case pull requests will generally be accepted if they contain no breaking changes, but when in doubt open an issue.

## Licensing

Where this section contradicts the information in `LICENSE`, one should defer to the information in `LICENSE`. Where the licenses of this project's dependencies contradict `LICENSE` and are more restrictive, defer to those projects' respective licenses.

This project is under an MIT license. Make free use of the code, but reproductions must retain credit to the listed authors. This software is free as in free speech, but due to the limitations of the Google Maps Services API, it may not be free as in free beer for your use case (i.e., free users are limited to 2500 requests daily at the time of this writing).
