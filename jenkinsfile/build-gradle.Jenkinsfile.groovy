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
            image: "",
            command: "sleep",
            args: "infinity"
        ),
        containerTemplate(
            name: "podman",
            image: "",
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
            container("jdk" {
                sh("./gradlew --build-cache build")
            })
        }
    }
}