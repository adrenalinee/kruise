import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

final String podmanImage = "quay.io/podman/stable:v5.3.2"
final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final String projectRepositoryUrl = params.projectRepositoryUrl
final String projectRepositoryBranch = params.projectRepositoryBranch

final String imagePath = params.imagePath

final String proxy = params.proxy
final String noProxy = params.noProxy

final def containerRegistryCredential = params.containerRegistryCredential
final def projectRepositoryCredential = params.projectRepositoryCredential
final String argocdApplicationName = params.argocdApplicationName

final String containerRegistryAddr = imagePath.substring(0, imagePath.indexOf("/"))
String imageTag
String imagePathTag


println("[kruise] job parameters: ${params}")

if (argocdApplicationName == "") {
    error("[kruise] argocdApplicationName 은 필수값입니다.")
}

if (projectRepositoryUrl == "") {
    error("[kruise] repositoryUrl 은 필수값입니다.")
}

if (projectRepositoryBranch == "") {
    error("[kruise] branch 은 필수값입니다.")
}

if (imagePath == "") {
    error("[kruise] imagePath 은 필수값입니다.")
}

def envVars = []
if (proxy != null) {
    envVars.add(envVar(
        key: 'http_proxy',
        value: proxy
    ))
    envVars.add(envVar(
        key: 'https_proxy',
        value: proxy
    ))
}
if (noProxy != null) {
    envVars.add(envVar(
        key: "no_proxy",
        value: noProxy
    ))
}


podTemplate(
    inheritFrom: 'jenkins-agent-default',
    name: "jenkins-agent-podman",
    label: "jenkins-agent-podman",
    nodeUsageMode: "EXCLUSIVE", // label 이 일치하는 job 에서만 사용됨.
    idleMinutes: idleMinutes, //대기시간(대시시간동안 다른 job 실행가능).
    instanceCap: instanceCap, //최대 생성가능한 동일 스팩 팟 갯수.
    envVars: envVars,
    containers:[
        containerTemplate(
            name: "podman",
            image: podmanImage,
            privileged: true,
            command: "sleep",
            args: "infinity"
        )
    ]
) {
    node("jenkins-agent-podman") {
        stage("Checkout") {
            git(url: projectRepositoryUrl, branch: projectRepositoryBranch, credentialsId: projectRepositoryCredential)
        }

        stage("Build image") {
            echo(readFile("Dockerfile"))

            final String shortCommitId = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
            imageTag = createTagName(shortCommitId)
            imagePathTag = "${imagePath}:${imageTag}"

            container("podman") {
                sh("podman build -t ${imagePathTag} .")
                sh("podman images")
            }
        }

        stage("Push image") {
            container("podman") {
                try {
                    withCredentials([
                        usernamePassword(
                            credentialsId: containerRegistryCredential,
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
                    error("[kruise] push image 과정에 에러가 발생했습니다. ex: ${ex.toString()}")
                } finally {
                    sh("podman rmi ${imagePathTag}")
                    sh("podman logout ${containerRegistryAddr}")
                }
            }
        }

        stage("Execute modify-argocd-app job") {
            build(
                job: "kruise.managed.modify-argocd-app",
                wait: true,
                parameters: [
                    string(name: "argocdApplicationName", value: argocdApplicationName),
                    string(name: "imageTag", value: imageTag)
                ]
            )
        }

        stage("Execute sync-argocd-app job") {
            build(
                job: "kruise.managed.sync-argocd-app",
                wait: true,
                parameters: [
                    string(name: "argocdApplicationName", value: argocdApplicationName)
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