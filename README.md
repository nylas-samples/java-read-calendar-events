# java-read-calendars-events

This sample will show you to read calendars events using the Nylas Java SDK.

## Setup

### System dependencies

- Java 18.0.2
- Maven 3.8.6

### Gather environment variables

You'll need the following values:

```text
ACCESS_TOKEN = ""
CALENDAR_ID = ""
```

Add the above values to a new `.env` file:

```bash
$ touch .env # Then add your env variables
```

### Install dependencies

```bash
org.slf4j / slf4j-simple / 1.7.25
com.nylas.sdk / nylas-java-sdk / 1.15.0
io.github.cdimascio / dotenv-java / 2.2.4
```

# Compilation

To compile the comment we need to use this `maven` command:

```bash
mvn clean compile
```

## Usage

Run the application using the `maven` command:

```bash
$ mvn exec:java -Dexec.mainClass="ReadEvents"
```

If successful, all your events from an specific will be displayed.


## Learn more

Read the blog post [How to Manage Calendar Events with the Nylas Java SDK](https://www.nylas.com/blog/how-to-manage-calendar-events-with-the-nylas-java-sdk-dev/)
Visit our [Nylas Java SDK documentation](https://developer.nylas.com/docs/developer-tools/sdk/java-sdk/) to learn more.
