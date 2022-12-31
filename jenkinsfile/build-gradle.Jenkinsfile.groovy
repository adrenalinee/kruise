String repositoryUrl = params.repositoryUrl
String branch = params.branch


podTemplate(
    name: "build-gradle",
    label: "build-gradle-jdk",
    nodeUsageMode: "EXCLUSIVE",
    idleMinutes: 10,
    instanceCap: 5,
    containers:[
        containerTemplate(
            name: "jdk",
            image: "eclipse-temurin:17.0.5_8-jdk-alpine",
            command: "sleep",
            args: "infinity"
        ),
        containerTemplate(
            name: "podman",
            image: "quay.io/podman/stable:v4.3.1",
            privileged: true,
            command: "sleep",
            args: "infinity"
        )
    ]
) {
    node("build-gradle-jdk") {
        stage("Checkout") {
            git(
                url: repositoryUrl,
                branch: branch
            )
        }

        stage("Build") {
            container("jdk") {
                sh("./gradlew --build-cache build")
            }
        }
    }
}