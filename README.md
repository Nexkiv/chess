# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server Design

[<img alt="Server Design" src="server-design.png">](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-IB68SnFRisDNS3YYw4O+kfYUJ84b-PPoGQsl9VRo1vlPJSULP6o2vvIfJKNAUAt6L-VybpMnALAKA8BkDoGwNIZ7TGjDlK4zLjkkcHDbKhzVMsyoqy3FvJUi-PhjNgotLysy+xTZ2WXCut0ncSVtnCycAARnFnykZgrWUipTkraZFi67zJsYs5lBg+aG2gB4luw1tnpL2QEruwTjn3IOoI5Jlz4ESvaD7GA-tBmrGaYq3W4dI5QGjlrGA8dE5G1VWnUxKjPDZw0ZwPOBc0goDoY3TIWA6g12CPGxNFdk0bK8VI0g7cgR9GkABUuAFRgAVola0oixhV+ucU1NAvQAA8ij0CtG3L5eJbM7y0KrqkrlKKUC9CAcm7J9hk25NerEveiMfpgGIRg4Rvaz7AovsqVUxCNRakbD29cZCZRgwaV-JscDxUzuBb0CO3BEgLpPd6fd1YAx4JhdIFA17DCbopY6u048VLFKzSmUpHCF71ILXOK6XtdxSpgMed1T4KrhpzjGrROiM1lEMWBHub51WwTQ3S8dKYdW6yFY4utbijWbPqD4hK-ji2lvLZWo51blV1obc21taB22Up6b+2lHHOWTjNZ8gDYAx0Tt+TvbjmLEEHsPgKedmZF3NmXUi1d0KIYbswTfbd-H4rIo+Q2C0VoMU-q7f+gjYAgMQKJkq2tLiOV1IHWBu5nqWbEZY-Z0VGLfKSsFtKuVQyFVubaiRlZZGvOYjVVVcxuHiSZos0Rtq+tcQGqgBR3NJFTXOaLSWstFaglMbQDW0jLi2MwBbbutt3nJEurdfK7K-LiY+tUFHcBAag2peTpM1R9CUhK369GvwgQQjElVKXQoMAADil4NCppiyNtxZcJvTaCjm1uWXdkTUm2W2i9hLzFbC6VmO7HKucY7Ttbh1DTsgYHQZodK9Cgb0KJOwy717QXtBbe07K7yG4KMDC9dO7yzaciLp30anB1XeB+i6dH8gW-ZBcgBIK21Bkh+7Jv7QZAcuQQFNy8p794I+5RklH0o+jjAYfzW542EhWZu4JhpAU9tBSuH0VYzOUAAElpBXBlYeEWqVDiLCuSLg4Jt3SJNksL0XMSVj+FAK2P6trXhC7KDL+KKwOcADkBGXXGBBsevnpEwdkZrmbrP2eXm57z-ngvpfq8xCscXSvdc3Htw7lIcuEAK5d-IqXauPee+16794+uM49fUZ+FDC3VQYeMZVFIabRtLYSHj1bOsQsebWelpS1Htr+O213DnB22WseO+VjjXHiKQbM0D5Te7HMM7u8Ox7NPLMSbyVJ89CO51faXb1VTRP1Owrr1u0H129MQ+bzAIz1ou8f148w57DLrNLLs2sgTgJid7I9cM4LQJQul881065UG-NpVlfV4OrTbMlbWaG18yt5vBGT23tPahEtAmS7ATra2TWCzNS2x22mGLyzzKwq37yqykx82dWgzq0Cway9Tama1axjkDQTk6wf2wwjSzkfyQyG3zhCGwAyCgGwG4GDEIUMBR2rmf2INIPIIIXxRRz-yow2xo32SL32zAPLwgPrzbQYmDz92iUdU7RpXMibEgHXGOEt22B1yEIOCukXmxWdBLDJEYMSBe0p3ex70+0Uw43R0fTXVllHxBzrxEQfWfiPQbDvRh2-Th0JwxxBQTDdBRzJEEJQAMMsOfSLBLHfwJ302UI525y0MX0onUJQDp37QZ2cxkK5x516D5wFwNx4yNxSlgwEKt3iJgESNSngzDV62fEGzzh0ToLIMMDjywyfxmWCFKIYN8OYIzwijGQ72ZBYK2QAOy07l2y4PX3AMrwyNkJDzFVP1rwkN7WkLN0GPkJPyUOXmcMSDUJLE0Pn2pVuXCKsyZV1gqT-lVHcM3xgPYN3yCxDl1naRaJP0g1SKFgv3dV5UQP3wKmwnOO6wT1SFoJILKIoKYMvE-16EPxFRz3-zIk20LS6JAJ6Lvz6NO1aAGJQDkOnmGNiVENuTGKkLuXcJSS4Vu2UPmJQEWMoOWOrypRk0fV7z0O+0H0cKMMKhsOAFvjB0fipKsPu1nxMwX1r3WJX02Mz3X0W32MkXzxcz3xOJ5LvzcQuMN1gPP3SgCyOPuJFIPyz3FJeKqIsVqMMHCPf1+L1n1STjaNYI6JBP8TBKmFAN6J4P6LtgxIdXFQONq1N1cwVLDgjhaz9TaxQg6yTiwMQ2zijSj2G2CEsDfVXnKBgAACkIBHJ38aDqigyrQIBQyIyozmDgh5cQBWx1tDT2DBghRODtgS9VkoTIDOMGIyDgAgyoA4AIBV4oAXgAB1FgTnctKYAAIVLgUDgAAGkphZ8bwgisicjki4kxDKIONVdVgyyKyqyaz6zGzmy2yOzuzezbh+ybckj6ct9IcYAAArSMtAMkJM1hFo17Yk7Qxwsk9BfQiww9bwkfWk+kifcHIfLc2krQ+Hc80FVw-szwm8-7DTVIfHUw-s68ihLcjnAnN7afXFf+fFScygac6ARYeIRsZyJ4PON8hw0ktUbAABFAL8zIn8ihW8oUHC-FcCr9M9dkkcoEQ8yIrE6Io04EIcu06DdI5YPIiPPrIo6PQM4MhMgxVmIxSonDEIOMkM8MvcrUxox4oqF6PqNMjMoE-NRinMvMlAAssvLWE7YsmEu2OCys6s6AWcpsgCVs9srsnsy0Z4FczItc3I6rG5S7Mw8Y0s73Kcwy2svYBskysyxcyyq0Ps2yhI23YY2YqlXoXc9eWi48tk1Ypy6K9hVfZlbYqpdyTyfk7fAKR0m-U45ol6CUlIqU43AZK-RrZlM4-KlUkS3i+MxMyShotfMU9ZVM73dM3PNgwU1S7o-M7grSivaE1y8s+Cjy4y+c8ypcqywK7Ya3YK9c6Ai7P9ZyqQwa9ymcryuc0yhciy5cg4Vc2a+yxvTc6fSK-c6KuS2Kkkr6C8tMK8yfIfW87HLTaGHTR8xkww5k80Kyi6wpeKvcjYmzdzXknrKIzcmIu46-AVQGpqgqp1PNa4mU0qpAxUoGrA1U2CMS-iw8qS6WDqWWSmLmdAeS1qxSs2Dqy2Lq8Enq80vq3gkRXSicty4atag4bysavynayYuIuy4YmvaiqHPgtAccpaRmgy5mlYVmza8a-y6y3aoK7IkKjKrck6g8v6mKlYy65BXQy8iku6pkh64w+8l64syk96l8p69GdWs8rC+TfC6a6QSFNTfWwqci56yIYC3W026fF2i2okgpKCnY2CkWhChYNAZC8IVEdCy20Imi1WxK7koEEADy8mPGzmBvYiWmMG7KyGtqGWFUfG7mHmyUuG6VMWBAiGnGjmeWVG6qjGuq5Mn46SnUnpX-FqhXdqrMzqgYXM7q9S3q0obSgW+m4Woa0Woy9anyrazsqarm-awuwq4ui-UuuU8u5lFAt0tAz0kNF4n0vAv0zRAMtIcshoRMWAYAbAMgwgBtGMixQ+yIRAE+xsc+5AEABtfUvPcmujPLWiYwc7Ryxajc0QuY7gPANHDC2dZ0YBqAECzHGFe+vAdqXHABOkuOdITYRYVMQwaUPQAwEIjkyBuilIDOxipYZimrVi2RDi7A-A4okIW+gSkCISiCNG2hxqO+yBx+i+l+tAxu7-YNQ1fUzLDuj+3LBjaYH++a3pc-eA5esq71F01A9rDAr07ego-rNOIAA)

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

trivial change to test something
