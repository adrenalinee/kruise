package plain

final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final String projectRepositoryUrl = params.projectRepositoryUrl
final String projectRepositoryBranch = params.projectRepositoryBranch

final String imagePath = params.imagePath

final String proxy = params.proxy
final String noProxy = params.noProxy

final String argocdApplicationName = params.argocdApplicationName

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

podTemplate(
    inheritFrom: 'jenkins-agent-default',
    name: "jenkins-agent-default",
    label: "jenkins-agent-default",
    nodeUsageMode: "EXCLUSIVE", // label 이 일치하는 job 에서만 사용됨.
    idleMinutes: idleMinutes, //대기시간(대시시간동안 다른 job 실행가능).
    instanceCap: instanceCap, //최대 생성가능한 동일 스팩 팟 갯수.
) {
    node("jenkins-agent-default") {
        stage("Execute build-image-to-app-sync job") {
            build(
                job: "kruise.managed.build-image-to-app-sync",
                wait: true,
                parameters: [
                    string(name: "argocdApplicationName", value: argocdApplicationName),
                    string(name: "projectRepositoryUrl", value: projectRepositoryUrl),
                    string(name: "projectRepositoryBranch", value: projectRepositoryBranch),
                    string(name: "imagePath", value: imagePath),
                    string(name: "proxy", value: proxy),
                    string(name: "noProxy", value: noProxy),
                ]
            )
        }
    }
}