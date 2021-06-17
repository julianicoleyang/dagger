FROM ubuntu:18.04

ARG SBT_VERSION=1.4.6
ARG SCALA_VERSION=2.13.6

# downloads scala/sbt/java jdk
RUN apt-get update && \
    apt-get -y install curl \
    build-essential \
    openjdk-8-jdk-headless

RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  curl -L -o scala-$SCALA_VERSION.deb https://downloads.lightbend.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.deb && \
  dpkg -i scala-$SCALA_VERSION.deb && \
  rm scala-$SCALA_VERSION.deb && \

# copy the whole project into /app in the container
RUN mkdir /app
COPY . /app

# cd to /app
WORKDIR /app

# run sbt
RUN sbt run

