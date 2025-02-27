final String argocdImage = "docker.io/bitnami/argo-cd:2.13.3"

final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final String argocdServer = "kruise-argocd-server.kruise-argocd.svc.cluster.local"

final def argocdCredential = params.argocdCredential

println("[kruise] job parameters: ${params}")

podTemplate(
    inheritFrom: 'jenkins-agent-default',
    name: "jenkins-agent-argocd",
    label: "jenkins-agent-argocd",
    nodeUsageMode: "EXCLUSIVE", // label 이 일치하는 job 에서만 사용됨.
    idleMinutes: idleMinutes, //대기시간(대시시간동안 다른 job 실행가능).
    instanceCap: instanceCap, //최대 생성가능한 동일 스팩 팟 갯수,
    containers: [
        containerTemplate(
            name: "argocd",
            image: argocdImage,
            command: "sleep",
            args: "infinity",
            runAsUser: "0"
        )
    ]
) {
    node("jenkins-agent-argocd") {
//        stage("delete argocd application") {
//            container("argocd") {
//                withCredentials([string(
//                        credentialsId: argocdCredential,
//                        variable: 'argocdToken'
//                )]) {
//                    sh(getArgocdAppDeleteCommand('$argocdToken', argocdServer))
//                }
//            }
//        }

//        stage("delete argocd project") {
//            container("argocd") {
//                withCredentials([string(
//                        credentialsId: argocdCredential,
//                        variable: 'argocdToken'
//                )]) {
//                    sh(getArgocdProjDeleteCommand('$argocdToken', argocdServer))
//                }
//            }
//        }
    }
}

def getArgocdAppDeleteCommand(String argocdToken, String argocdServer) {
    return """argocd app delete ${argocdApplicationName} \
--plaintext \
--server ${argocdServer} \
--auth-token ${argocdToken}"""
}

def getArgocdProjDeleteCommand(String argocdToken, String argocdServer) {
    return """argocd proj delete ${argocdApplicationName} \
--plaintext \
--server ${argocdServer} \
--auth-token ${argocdToken}"""
}