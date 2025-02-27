final String argocdImage = "docker.io/bitnami/argo-cd:2.13.3"

final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final String argocdServer = "kruise-argocd-server.kruise-argocd.svc.cluster.local"

final def argocdCredential = params.argocdCredential
final String clusterName = params.clusterName
final String releaseName = params.releaseName
final String argocdApplicationName = params.argocdApplicationName
final Boolean override = params.override

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
        stage("create argocd project") {
            container("argocd") {
                withCredentials([string(
                        credentialsId: argocdCredential,
                        variable: 'argocdToken'
                )]) {
                    sh(getArgocdProjCreateCommand('$argocdToken', argocdServer))
                }
            }
        }
        stage("create argocd application") {
            container("argocd") {
                withCredentials([string(
                    credentialsId: argocdCredential,
                    variable: 'argocdToken'
                )]) {
                    sh("ls -al")
                    sh(getArgocdAppCreateCommand('$argocdToken', argocdServer, override))
                }
            }
        }
    }
}

/**
 * spec: https://argo-cd.readthedocs.io/en/stable/user-guide/commands/argocd_app_create
 **/
def getArgocdAppCreateCommand(String argocdToken, String argocdServer, Boolean override) {
    def command = """argocd app create ${argocdApplicationName} \
--label creator=kruise \
--plaintext \
--server ${argocdServer} \
--repo ${kruiseRepositoryUrl} \
--revision ${kruiseBranch} \
--path charts/${helmChartName} \
--release-name ${releaseName} \
--project ${argocdProjectName} \
--dest-name ${clusterName} \
--dest-namespace ${releaseName} \
--sync-option CreateNamespace=true \
--sync-policy none \
--auth-token ${argocdToken} """

    if (override) {
        command += "--upsert "
    }

    for (String line: helmChartValues.split("\n")) {
        command += "--helm-set ${line} "
    }

    command += "--helm-set image.repository=${imagePath}"
    return command
}

/**
 * spec: https://argo-cd.readthedocs.io/en/stable/user-guide/commands/argocd_proj_create
 */
def getArgocdProjCreateCommand(String argocdToken, argocdServer) {
    return """argocd proj create ${argocdProjectName} \
--plaintext \
--server ${argocdServer} \
--description "kruise 에서 생성한 project 입니다." \
--upsert \
--dest *,* \
--allow-cluster-resource */* \
--src * \
--auth-token ${argocdToken}"""
}