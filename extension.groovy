plugins {
    id 'application'
    id 'org.openjfx.javafxplugin' version '0.0.10'
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'mysql:mysql-connector-java:8.0.23'
}

javafx {
    version = "15"
    modules = [ 'javafx.controls', 'javafx.fxml' ]
}

application {
    mainClass = 'YourMainClass'
}
