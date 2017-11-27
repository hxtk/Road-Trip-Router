# Road Trip Router


## Installing / Getting started

Install [Bazel](https://bazel.build/) and compile this project by running `bazel build //:RoutePlanner`.

## Developing

We try to follow the [Google Java Styleguide](https://google.github.io/styleguide/javaguide.html). The project 

### Deploying / Publishing

You must supply your own Google Maps API key under the `--key` flag, as I cannot afford to support the large number of API requests this generates ;) (`n(n+1)/2` as the number of destinations).

```BASH
java -jar RoutePlanner_deploy.jar --key "..."
```

## Features

### In progress

- Find a pseudo-optimal path by distance using a genetic algorithm

### Roadmap

- Flags for various route parameters (i.e., "avoid tolls/ferries/highways",
	cost by time instead of distance
- Compound flags, e.g., `--scenic` or `-S` avoids highways/tolls and sorts by distance (open issues for compound flags you would like to see).

## Contributing

As mentioned in **Developing**, please try to follow the Google Java Style guide roughly. We're not Google, so there is no need to be completely strict.

Be aware that the maintainer reserves the right to deny pull requests. To make sure this doesn't happen to you, please open an issue and get an okay from the maintainer before putting your time into a feature you would like to see added. Bug fix and test case pull requests will generally be accepted if they contain no breaking changes, but when in doubt open an issue.

## Licensing

All rights reserved. This code is free to use (with credit to its author(s)) for non-commercial purposes insofar as its various dependencies and their respective licenses allow. For commercial licensing, contact the maintainer.
