import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

final String podmanImage = "quay.io/podman/stable:v4.5.1"
final String jdk17Image = "docker.io/eclipse-temurin:17.0.7_7-jdk-jammy"
final String jdk20Image = "docker.io/eclipse-temurin:20.0.1_9-jdk-jammy"
final Integer idleMinutes = 60
final Integer instanceCap = 5

final String projectName = params.projectName
final String repositoryUrl = params.repositoryUrl
final String branch = params.branch
final Integer jdkVersion = params.jdkVersion.toInteger()
final String imagePath = params.imagePath
final def imageRegistryCredential = params.imageRegistryCredential


String selectedJdkImage
if (jdkVersion == 17) {
    selectedJdkImage = jdk17Image
} else {
    selectedJdkImage = jdk20Image
}

final String containerRegistryAddr = imagePath.substring(0, imagePath.indexOf("/"))
String imageTag
String imagePathTag


println("[FRODO] job parameters: ${params}")

//validation ----
if (projectName == "") {
    error("[FRODO] projectName 은 필수값입니다.")
}

if (projectName == "frodo") {
    error("[FRODO] 허용되지 않는 projectName 입니다. projectName: ${projectName}")
}

if (repositoryUrl == "") {
    error("[FRODO] repositoryUrl 은 필수값입니다.")
}

if (branch == "") {
    error("[FRODO] branch 은 필수값입니다.")
}

if (jdkVersion == "") {
    error("[FRODO] jdkVersion 은 필수값입니다.")
}

if (imagePath == "") {
    error("[FRODO] imagePath 은 필수값입니다.")
}

podTemplate(
    name: "jenkins-agent-build-gradle-push-image-jdk${jdkVersion}",
    label: "jenkins-agent-build-gradle-push-image-jdk${jdkVersion}",
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
        containerTemplate(
            name: "podman",
            image: podmanImage,
            privileged: true,
            command: "sleep",
            args: "infinity"
        )
    ]
) {
    node("jenkins-agent-build-gradle-push-image-jdk${jdkVersion}") {
        stage("Checkout") {
            git(
                url: repositoryUrl,
                branch: branch
            )
        }

        stage("Build gradle") {
            container("jdk") {
                sh("./gradlew build")
            }
        }

        stage("Build container image") {
            echo(readFile("Dockerfile"))

            final String shortCommitId = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
            imageTag = createTagName(shortCommitId)
            imagePathTag = "${imagePath}:${imageTag}"

            container("podman") {
                sh("podman build -t ${imagePathTag} .")
                sh("podman images")
            }
        }

        stage("Push container image") {
            container("podman") {
                try {
                    withCredentials([
                            usernamePassword(
                                credentialsId: imageRegistryCredential,
                                usernameVariable: "username",
                                passwordVariable: "password"
                            )
                    ]) {
                        //아래는 password 노출위험을 막기 위해 작은따움표 사용
                        //참고: https://www.jenkins.io/doc/book/pipeline/jenkinsfile/#string-interpolation
                        sh('podman login -u $username -p $password ' + containerRegistryAddr)
                        sh("podman push ${imagePathTag}")
                    }
                } catch (Exception ex) {
                    error("[FRODO] push image 과정에 에러가 발생했습니다. ex: ${ex.toString()}")
                } finally {
                    sh("podman rmi ${imagePathTag}")
                    sh("podman logout ${containerRegistryAddr}")
                }
            }
        }

        stage("modify and sync argocd application") {
            build(
                job: "${projectName}.pipeline.${branch}.modify-sync-argocd-app",
                wait: true,
                parameters: [
                    string(name: "projectName", value: projectName),
                    string(name: "imageTag", value: imageTag)
                ]
            )
        }
    }
}

def createTagName(shortCommitId) {
    final String now = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))

    return "${now}-${shortCommitId}"
}