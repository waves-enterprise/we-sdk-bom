val mavenUser: String by project
val mavenPassword: String by project

val weSdkSpringVersion: String by project
val weContractSdkVersion: String by project
val weNodeClientVersion: String by project
val weTxObserverVersion: String by project

plugins {
    `java-platform` //Plugin used to build Maven BOM
    `maven-publish` //Plugin used to publish Maven BOM
    id("fr.brouillard.oss.gradle.jgitver") version "0.9.1"
}

group = "com.weintegrator"
version = "0.0.0-SNAPSHOT"

jgitver {
    strategy = fr.brouillard.oss.jgitver.Strategies.PATTERN
    versionPattern = "\${M}.\${m}.\${meta.COMMIT_DISTANCE}-SNAPSHOT"
    nonQualifierBranches = "master"
}

//Allows <dependencies> block in Maven BOM
javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        runtime("com.wavesenterprise:we-starter-atomic:$weSdkSpringVersion")
        runtime("com.wavesenterprise:we-starter-contract-client:$weSdkSpringVersion")
        runtime("com.wavesenterprise:we-starter-node-client:$weSdkSpringVersion")
        runtime("com.wavesenterprise:we-starter-tx-signer:$weSdkSpringVersion")

        runtime("com.wavesenterprise:we-atomic:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-blocking-client:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-coroutines-client:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-error:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-grpc-blocking-client:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-grpc-coroutines-client:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-grpc-java:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-grpc-kotlin:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-grpc-mapper:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-feign-client:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-ktor-client:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-json:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-client-reactor-client:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-node-domain-test:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-tx-signer-api:$weNodeClientVersion")
        runtime("com.wavesenterprise:we-tx-signer-node:$weNodeClientVersion")

        runtime("com.wavesenterprise:we-contract-sdk-api:$weContractSdkVersion")
        runtime("com.wavesenterprise:we-contract-sdk-blocking-client:$weContractSdkVersion")
        runtime("com.wavesenterprise:we-contract-sdk-client-local-validator:$weContractSdkVersion")
        runtime("com.wavesenterprise:we-contract-sdk-core:$weContractSdkVersion")
        runtime("com.wavesenterprise:we-contract-sdk-grpc:$weContractSdkVersion")
        runtime("com.wavesenterprise:we-contract-sdk-jackson:$weContractSdkVersion")
        runtime("com.wavesenterprise:we-contract-sdk-test:$weContractSdkVersion")

        runtime("com.wavesenterprise:we-tx-observer-common-components:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-observer-api:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-observer-core-spring:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-observer-domain:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-observer-jpa:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-observer-starter:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-tracker-api:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-tracker-core-spring:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-tracker-domain:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-tracker-jpa:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-tracker-read-starter:$weTxObserverVersion")
        runtime("com.wavesenterprise:we-tx-tracker-starter:$weTxObserverVersion")
    }
}

repositories {
    mavenCentral()
    maven {
        name = "maven-snapshots"
        url = uri("https://artifacts.wavesenterprise.com/repository/maven-snapshots/")
        mavenContent {
            snapshotsOnly()
        }
        credentials {
            username = mavenUser
            password = mavenPassword
        }
    }

    maven {
        name = "maven-releases"
        url = uri("https://artifacts.wavesenterprise.com/repository/maven-releases/")
        mavenContent {
            releasesOnly()
        }
        credentials {
            username = mavenUser
            password = mavenPassword
        }
    }
    mavenLocal()
}

publishing {
    repositories {
        maven {
            name = "Maven"
            afterEvaluate {
                url = uri(
                    "https://artifacts.wavesenterprise.com/repository/${
                        if (project.version.toString().endsWith("-SNAPSHOT")) "maven-snapshots" else "maven-releases"
                    }"
                )
            }
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }

    publications {
        create<MavenPublication>("we-platform-bom") {
            from(components["javaPlatform"])
        }
    }
}