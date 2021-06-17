FROM hseeberger/scala-sbt:graalvm-ce-21.1.0-java8_1.5.4_2.13.6
RUN mkdir /app
WORKDIR /app
COPY . /app
CMD ["sbt", "run"]git st