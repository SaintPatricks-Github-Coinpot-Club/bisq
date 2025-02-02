## Inventory monitor

This is a simple headless node with a http server which requests periodically from the seed nodes their inventory (not
the full data but just the information about their network data).

### Run inventory monitor

Run the Gradle task:

```sh
./gradlew inventory:run
```

Or create a run scrip by:

```sh
./gradlew inventory:startBisqApp
```

And then run:

```sh
./bisq-inventory
```

### Customize inventory monitor

To configure it with custom parameters append optional program arguments in the following order:

Arguments: `port cleanupTorFiles intervalSec shutdownIntervalDays useLocalhostForP2P network`
If `shutdownIntervalDays` is 0 the app will not be shut down. Otherwise, it's the number of days for shutting down (run
script need to ensure the node gets started again).

Values: `Integer 0|1 Integer Integer 0|1 BTC_MAINNET|BTC_TESTNET|BTC_REGTEST`

Example for localhost, regtest on port 8080: `8080 0 10 5 1 BTC_REGTEST`

Example for production node with cleanupTorFiles=true: `80 1`

Append the program arguments to the run script:

```sh
./bisq-inventory 8080 0 10 1 BTC_REGTEST
```
