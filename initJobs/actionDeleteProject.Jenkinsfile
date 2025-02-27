final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final def argocdCredential = params.argocdCredential
final String projectName = params.projectName
final String projectRepositoryBranch = params.projectRepositoryBranch

final String fixedBranchName = projectRepositoryBranch.replace("/", "-").toLowerCase()
final def argocdApplicationName = "${projectName}-${fixedBranchName}"

println("[kruise] job parameters: ${params}")


//validation ----
if (projectName == "") {
    error("[kruise] projectName 은 필수값입니다.")
}

if (projectName == "kruise") {
    error("[kruise] 허용되지 않는 projectName 입니다. projectName: ${projectName}")
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
        stage("deleteJenkinsView") {
            def projectView = Jenkins.instance.getView(projectName)
            Jenkins.instance.deleteView(projectView)
        }
//        stage("deleteJenkinsJobs") {
//            Jenkins.instance.items
//                .findAll { job -> job.name =~ "${projectName}\\..*" }
//                .each { job ->
//
//                    job.delete()
//                }
////                 .each { job -> job.setDisabled(true) }
//        }
//        stage("deleteArgocdApp") {
//            build(
//                job: "kruise.managed.deleteArgocdApp",
//                wait: true,
//                parameters: [
//                    credentials(name: "argocdCredential", value: argocdCredential),
//                    string(name: "argocdApplicationName", value: argocdApplicationName)
//                ]
//            )
//        }
    }
}