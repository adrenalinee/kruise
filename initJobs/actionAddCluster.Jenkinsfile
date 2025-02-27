final String argocdImage = "docker.io/bitnami/argo-cd:2.13.3"

final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final String argocdServer = "kruise-argocd-server.kruise-argocd.svc.cluster.local"

final String serverUrl = params.serverUrl
final String contextName = params.contextName
final String caCertificate = params.caCertificate
final def kruiseAdminToken = params.kruiseAdminToken
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
        stage("argocd add cluster") {
            container("argocd") {
                withCredentials([string(
                        credentialsId: argocdCredential,
                        variable: 'argocdToken'
                )]) {
                    withKubeConfig([
                            credentialsId: kruiseAdminToken,
                            caCertificate: caCertificate,
                            serverUrl: serverUrl,
                            contextName: contextName
                    ]) {

                        sh(getArgocdClusterAddCommand(
                            contextName,
                            argocdServer,
                            '$KUBECONFIG',
                            '$argocdToken'
                        ))
                    }
                }
            }
        }
    }
}

static def getArgocdClusterAddCommand(String contextName, String argocdServer, def kubeconfig, String argocdToken) {
    return """argocd cluster add ${contextName} \
--name ${contextName} \
--kubeconfig ${kubeconfig} \
--plaintext \
--server ${argocdServer} \
--upsert \
--auth-token ${argocdToken}"""
}