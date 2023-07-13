final String argocdImage = "docker.io/bitnami/argo-cd:2.7.7"

final Integer idleMinutes = 60
final Integer instanceCap = 5

final String frodoAdminToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhcmdvY2QiLCJzdWIiOiJmcm9kb0FkbWluOmFwaUtleSIsIm5iZiI6MTY4OTEzMjY2MCwiaWF0IjoxNjg5MTMyNjYwLCJqdGkiOiI2ZTY3NTlmNy0yNTM2LTRlNzktYWJiZS04NWM5MTI4N2ZjODgifQ.SdEi2vNFsCZGtAULc6LRmjHimRzzHgUlP7MUCW4UQUw"


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
                sh(
"""argocd app create hello-world-3 \
--plaintext \
--server argo-cd-argocd-server.argo-cd.svc.cluster.local \
--repo https://github.com/adrenalinee/frodo.git \
--path charts/frodo-standard-server \
--dest-namespace hello-world3 \
--dest-server https://kubernetes.default.svc \
--sync-policy automated \
--helm-set image.tag=20230710-071748 \
--auth-token ${frodoAdminToken}"""
                )
            }
        }
    }
}