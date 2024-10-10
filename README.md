# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design

[![Sequence Diagram](server-design.png)]
(https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5TKAB4ktO7oBIRDAAxGjAVJYwAEoo9kiqYLSYMDD2UBAArtghYRHRsfEWckgQaCEA7gAWSInJKfDI5jAAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0GQZQADpoAN4ARLOUYQC2KCu9KzArADQHuOrl0By7BwfHKyibwEgIV-srAL51KcJdLe1snNwejBVusoFsdnsbid9KpzlBLpCjgd7o9noiPvVWOwuJRfh0RCpelACglKAAKGJxUlQGIARwyajAAEpPviqKI8bJ5EoVOpevYUGAAKpzMmg8EszFcxTKNSqPHGboxYAcGAiyjSmA6SLi4DbVnSnlylptNmiXpoDIIBCs74c02G2V8kDEuQodVQMVzaWS+qO3ny02KuCuxJq73aW2de3tf1y7oulBuhQZMCVMnAVOVH0G7RG9QKnTdENJsMptPSqMEwzNU3-HFAymFSgxVRWrCY+uAk3tb6UXogubgl5QlaZtOjCAAa3QI-eVZ+tfaiFQGF6ACYnE5FoONnqIdckWOs5OZ2g5xiYOgOKYUmlMtlQuFIk2ElBiqUYGSAKJQdJAvRVWJelGSZCpqlqTEVyaU0+yBQYRgmaZQR3NYh33OdblOWELkw5EHieC9WS7XElzZftgRWbZ1GAAUXl-f9ekAmBgIZBJ50xOCewaVcwF6AAWLdUOo1RaIPFYGOgJiVRYlAQPYy9r1vVJ0iyHJn3yKkigIT8fz-KSYGABBXQ4SJ5DPMDgiqGoxCgxpSFgzoKIQsZJhmOZUN1bY8JWbC4QRQ9bhRQj0QXUjTWgtcYEEgBmYS5TE+j9KBIyTLM4Azw4q9OGU+81KfPJX20koyj0xiYEs6zIPqSLmEc6hnOGVzkI85Y0L3bzESwmF-J84K0UPS8vic2AyNq3oAFYhLakTEr2SSgTJDg1BdYgdLKCAADMr2SpksqUswcrMHxsV0bgAGoQmgdgBRgAAZCA4jQXLVMfa6HEMB6nvAmzWVqvE4N6FykKmAx1BKzz0M6wLoTOXDQs4ka8RIoFdzBDCuthnD4SIztsW7er2UJGAEEepA0DJL7ybpNjmQXGMZDzJ1VATUMUHLdNx2zbRfRSKYphgUYhmkIZegUDhVVBQys3MQgP2eqUmYDQtizZmAOcrRHq24lHeiptBW3bYj8fC3sRoHdr0eh15bi509ZwR+ouLG+y+JgTdtzarzxNHO3pwdwa6gOu9Xqu98PvusnSoW3oMjCLNoCQAAvFAOEqiDbJq12AfN-ompBsHVAhr2oZ9o8-PhwOtcXCLXYEpwAEZ4pouj5uS2P47TROU8uRSjpDh8w5uz6o6-GOKp+6qUn+wnGsQtzC+LtHh0x3yesr14hvI0ba94ybptWWbW4OcelpWqA1pKmAtp2-89r7m9Dsf7xfDOlBLuCNIUHQSP7EyLA6h5UfF-H+X1-6T0ztPbOs94LSG-Hdb8oxvwLzlMXLm3d5a9AADx+zPK0MKo0-gmxSief2Ct6goxztWXopM-6pkpo9f+NNQL0xUJyJW8ZUiCg5hmUhFlczcmZirfIMkNbaC1JEXB6ABEymVmRO0xMuaaydtGNhDoOF8iMCgbgiQeFKJ5jI-MgZ2iKhkNowUhgxHyFYTWOsxC9aMNTIbBAHYKHEO4oDYEW9na73MBuA+D8XqD0-sSUBjjmBlQMnHdBF8e7p1+nZXiVCuhAzgQgpBKDwalEWDE5OmCYA4L4egfB1dTY8T8dFRuzdRLHwku3GA0SE6xNTvtfuKlgkgLKGA1MY96nxKnuUhyZsGqwPgYg5B0xF7ZM7pUDB61sFSLQCUlRIzuLjRgFNT2h8Eq1NPstVQq1sDrWvttSgd9WnPyOi-U6-h34hGJKqO6hQYAAHF9waEAaHYIDz7rPLedRCBf1oHDJSf0F5iC3L2H3DkppeT5kFMWcs4aqy7EAgoos42aLCFmmJsgBIlNCjMISLzHFNZYwaJZlwsAeiiloBJXGAsQYiwiNVFY4AEjpYTjIYYoR8jVEoF6Po6x1cGYMspXisA-y1BknpRS4RypVRGQQK8-cyiUhio8fy3oUrpR9HGDYnW9jfkJGca4lIlC+UjItlC6iVw+irBtSgAAktIK4Dd1wxX4rcKq7pS4+X8KAKcHoV4wxWI6gAchjQa4xvFIxdnvd2B8w3vLtQ6-cLq3Ueq9QcH1wao02wOAGkAQa-Wrwjfmg4bwY1BzaUA+5qdjXMClfKSJQJGld2aWnQFiSYIgucuCkGjqYUdrhSVBZtKkXbzWXXSpTcZo7PEuPdtszO0XKCflH5TyEgqoBa2juuS4ndqzkkmBQMB2QuhTMuZY6EUToIdOhNmzqlzRPvUs+ByL5HKvjfM50B741suc-E6nA35BGCNgDIUBsDcHgGrKV67HwQagzBksbod2GCshnIFJ6+3wXzhe7Yw6V2jtKOOrleDULlutpW+9qKGyCqzJAM8xw03bEjdRrK5r3GE3NDARMboySocSESumIq1HksEQGfk3Csy8PI+gWVknjRMt6Aq9WWZNTak5ZUe25D1VystUTAV2m1WkvYUpzRApJX7jJFRlAinZHKZMcytTOrxFabszyuRPHiaWmtAa9RFnKX8cSFK2z+52P2a805owzKhOGDc8KlZRnDVYt6PF01mKGyaqtZRR1Gbejus9bGlFviooe0o+m11hWs2BM+cEpD0HDDxfQ708qzFWKgSPVAnD28gb4emEOq9sT8mFPk0syrbGK2canfGipgktlUQXUldrMlOsKQAwhkIjWUNwf3G1qJw3k6p36ZAwZdVcP9fnoNy9B7RuIsmygSLuNktlPWYJOd2yW6Lvqcu7uLS6sD3yjt5re3tgHaBKd7Dva+t52u1MIbd34VjZ02Q1oj3nuO2RTXZcM6n3zu+ytgy77DnHJ-btNdT9PDAb8BdEIlhtGk3KDAAAUhAcm6HMB1uCAz60EBmds451KkIRapyYBnpdvOQpB23dhUne7E7ULQeAAzqAcAICkygC8AA6iwJ1SCpgACE7oKDgAAaSPPl6rMAiv8Q+D43HCaKvLA+AdbnvOmes-Z2UYXe6tRrbkrTSyovxfAth4MaXBGUBEevaR29430dtWV6r9Xmudd64N8b03FvbhW8zcVzADvzv10W0fH7q2gKB8ZPON3XyPf8690L-bfu-uduDwgQNofeueIjzLwjR35fI4e0njvKeNfQHT-r78RuTfm8t1V-PdvC9xrK27D7z7dm-YHwDqnXO6+M4b4Ln3zfx6pSTKZGA5kFMi478WrvMOe8DEjzd-vSOb0o904n1YyfKCp4n3sXXKfGfbPefbYArG3WrZfUrR3ebJwOKAnGpcvAyM-FUdKTKV3I6d3A-AXb3VrP3dvTvCXcPJ-PvaPAfBXBPJXUfX-cfLXAAjPafLPOfXPBfGrAvIvd7OAjfJAxafZUnb9U5CnDAm8A6a5EDW5D+NIFXBoJMWAYAbAaDOWdaPfDpd8SIRAWQwyBQ5AEAeWQFIgnvNJcZNyYwKAnHYvRNLZYQ0wbnKQ9Q7gPALQxQ3Q45fA-QsPQwsZDJaYUwjgvHJNMvInXg8+S+T8cnc5awpSIAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
