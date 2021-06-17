# Lint
`$ sbt scalafmtAll`

# Build
`$ docker build --rm --tag dagger:latest .`

# Run
Pass in `.json` file as an argument for either docker or sbt. File must be located in root of project directory as shown
here with "example.json."

`$ docker run dagger "example.json"`

`$ sbt 'run "example.json"'`

# Test
*NOTE: Seems to have broken after getting docker to run*

`$ sbt test`