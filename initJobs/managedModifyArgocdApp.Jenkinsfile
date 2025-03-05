final String argocdImage = "docker.io/bitnami/argo-cd:2.13.3"

final String argocdServer = "kruise-argocd-server.kruise-argocd.svc.cluster.local"

final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final def argocdCredential = params.argocdCredential
final String argocdApplicationName = params.argocdApplicationName
final String imageTag = params.imageTag

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
        stage("modify argocd application imageTag") {
            container("argocd") {
                withCredentials([string(
                    credentialsId: argocdCredential,
                    variable: 'argocdToken'
                )]) {
                    sh(getArgocdAppModifyCommand('$argocdToken', argocdServer))
                }
            }
        }

//        stage("sync argocd application") {
//            container("argocd") {
//                withCredentials([string(
//                        credentialsId: argocdCredential,
//                        variable: 'argocdToken'
//                )]) {
//                    sh(getArgocdAppSyncCommand('$argocdToken', argocdServer))
//                    sh(getArgocdAppWaitCommand('$argocdToken', argocdServer))
//                }
//            }
//        }
    }
}

/**
 * spec: https://argo-cd.readthedocs.io/en/stable/user-guide/commands/argocd_app_set
 */
def getArgocdAppModifyCommand(String argocdToken, String argocdServer) {
    return """argocd app set $argocdApplicationName \
--insecure \
--server ${argocdServer} \
--helm-set image.tag=$imageTag \
--auth-token ${argocdToken}"""
}

///**
// * spec: https://argo-cd.readthedocs.io/en/stable/user-guide/commands/argocd_app_sync
// */
//def getArgocdAppSyncCommand(String argocdToken, String argocdServer) {
//    return """argocd app sync $argocdApplicationName \
//--insecure \
//--server ${argocdServer} \
//--prune \
//--auth-token ${argocdToken}"""
//}

///**
// * spec: https://argo-cd.readthedocs.io/en/stable/user-guide/commands/argocd_app_wait
// */
//def getArgocdAppWaitCommand(String argocdToken, String argocdServer) {
//    return """argocd app wait $argocdApplicationName \
//--insecure \
//--server ${argocdServer} \
//--auth-token ${argocdToken}"""
//}