final String kubectlImage = "docker.io/jitesoft/kubectl:v1.27.3"
final Integer idleMinutes = 60
final Integer instanceCap = 5

final String projectName = params.projectName
final String repositoryUrl = params.repositoryUrl
final String branch = params.branch
final def kubeconfigFile = params.kubeconfigFile //secretFile

podTemplate(
    name: "frodo-jenkins-deploy-k8s-kubectl",
    label: "frodo-jenkins-deploy-k8s-kubectl",
    nodeUsageMode: "EXCLUSIVE", // label 이 일치하는 job 에서만 사용됨.
    idleMinutes: idleMinutes, //대기시간(대시시간동안 다른 job 실행가능).
    instanceCap: instanceCap, //최대 생성가능한 동일 스팩 팟 갯수.
    containers:[
        containerTemplate(
            name: "kubectl",
            image: kubectlImage,
            command: "sleep",
            args: "infinity"
        )
    ]
) {
    node("frodo-jenkins-deploy-k8s-kubectl") {
        stage("Checkout") {
            git(
                url: repositoryUrl,
                branch: branch
            )
        }

        stage("Deploy") {
            container("kubectl") {
                sh("kubectl version --output=yaml")
                dir("kustomize") {
//                     sh("kubectl kustomize ./base")

                    withKubeConfig([
                        credentialsId: kubeconfigFile
                    ]) {
                        sh("kubectl --kubeconfig $KUBECONFIG apply --kustomize .")
                    }
                }
            }
        }
    }
}