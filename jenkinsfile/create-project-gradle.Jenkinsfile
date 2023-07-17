final Integer idleMinutes = 60
final Integer instanceCap = 5

println(scm.userRemoteConfigs[0].url)

// final String frodoRepositoryUrl = frodoRepositoryUrl
// final String frodoBranch = frodoBranch
// final String frodoRepositoryCredential = frodoRepositoryCredential

final String projectName = params.projectName
final String projectRepositoryUrl = params.projectRepositoryUrl
final String projectRepositoryBranch = params.projectRepositoryBranch
final String helmChartRepositoryUrl = params.helmChartRepositoryUrl
final String helmChartBranch = params.helmChartBranch
final String helmChartPath = params.helmChartPath
final String jdkVersion = params.jdkVersion
final String imagePath = params.imagePath
// final String imageTag = params.imageTag

println("[FRODO] job parameters: ${params}")


//validation ----
if (projectName == "") {
    error("[FRODO] projectName 은 필수값입니다.")
}

if (projectName == "frodo") {
    error("[FRODO] 허용되지 않는 projectName 입니다. projectName: ${projectName}")
}


podTemplate(
    name: "jenkins-agent-default",
    label: "jenkins-agent-default",
    nodeUsageMode: "EXCLUSIVE", // label 이 일치하는 job 에서만 사용됨.
    idleMinutes: idleMinutes, //대기시간(대시시간동안 다른 job 실행가능).
    instanceCap: instanceCap, //최대 생성가능한 동일 스팩 팟 갯수.
) {
    node("jenkins-agent-default") {
        stage("createSeedJobs") {
            build(
                job: "frodo.managed.seed.build-gradle-push-image",
                wait: true,
                parameters: [
                    string(name: "projectName", value: projectName),
                    string(name: "projectRepositoryUrl", value: projectRepositoryUrl),
                    string(name: "projectRepositoryBranch", value: projectRepositoryBranch),
                    string(name: "jdkVersion", value: jdkVersion),
                    string(name: "imagePath", value: imagePath)
                ]
            )
        }
        stage("createArgocdApp") {
            build(
                job: "frodo.managed.create.argocd-app",
                wait: true,
                parameters: [
                    string(name: "projectName", value: projectName),
                    string(name: "helmChartRepositoryUrl", value: helmChartRepositoryUrl),
                    string(name: "helmChartBranch", value: helmChartBranch),
                    string(name: "helmChartPath", value: helmChartPath),
                    string(name: "imagePath", value: imagePath),
                    string(name: "imageTag", value: "")
                ]
            )
        }
    }
}
