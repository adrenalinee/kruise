final String argocdImage = "docker.io/bitnami/argo-cd:2.7.7"

final Integer idleMinutes = 60
final Integer instanceCap = 5

final def frodoAdminToken = params.frodoAdminToken
final String projectName = params.projectName

podTemplate(
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
        stage("execute") {
            container("argocd") {
                withCredentials([
                    string(
                        credentialsId: frodoAdminToken,
                        variable: "token"
                    )
                ]) {
                    sh(getArgocdAppDeleteCommand() + ' --auth-token=$token')
                }
            }
        }
    }
}

def getArgocdAppDeleteCommand() {
    return """argocd app delete ${projectName} \
--plaintext \
--server argo-cd-argocd-server.argo-cd.svc.cluster.local"""
}