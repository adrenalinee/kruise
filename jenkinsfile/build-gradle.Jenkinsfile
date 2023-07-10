// final String podmanImage = "quay.io/podman/stable:v4.5.1"
final String jdk17Image = "docker.io/eclipse-temurin:17.0.7_7-jdk"
final String jdk20Image = "docker.io/eclipse-temurin:20.0.1_9-jdk"
final Integer idleMinutes = 60
final Integer instanceCap = 5

String repositoryUrl = params.repositoryUrl
String branch = params.branch
Integer jdkVersion = params.jdkVersion.toInteger()


String selectedJdkImage
if (jdkVersion == 17) {
    selectedJdkImage = jdk17Image
} else {
    selectedJdkImage = jdk20Image
}

println("[FRODO] gradle build start !! -------")
println("[FRODO] job parameters: ${params}")

podTemplate(
    name: "jenkins-agent-build-gradle-jdk${jdkVersion}",
    label: "jenkins-agent-build-gradle-jdk${jdkVersion}",
    nodeUsageMode: "EXCLUSIVE", // label 이 일치하는 job 에서만 사용됨.
    idleMinutes: idleMinutes, //대기시간(대시시간동안 다른 job 실행가능).
    instanceCap: instanceCap, //최대 생성가능한 동일 스팩 팟 갯수.
    containers:[
        containerTemplate(
            name: "jdk",
            image: selectedJdkImage,
            command: "sleep",
            args: "infinity"
        ),
//         containerTemplate(
//             name: "podman",
//             image: podmanImage,
//             privileged: true,
//             command: "sleep",
//             args: "infinity"
//         )
    ]
) {
    node("jenkins-agent-build-gradle-jdk${jdkVersion}") {
        stage("Checkout") {
            git(
                url: repositoryUrl,
                branch: branch
            )
        }

        stage("Build") {
            container("jdk") {
                sh("./gradlew build")
            }
        }
    }
}