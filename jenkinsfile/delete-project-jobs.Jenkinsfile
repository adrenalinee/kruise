final Integer idleMinutes = 60
final Integer instanceCap = 5

final String projectName = params.projectName

println("[FRODO] job parameters: ${params}")

podTemplate(
    name: "jenkins-agent-default",
    label: "jenkins-agent-default",
    nodeUsageMode: "EXCLUSIVE", // label 이 일치하는 job 에서만 사용됨.
    idleMinutes: idleMinutes, //대기시간(대시시간동안 다른 job 실행가능).
    instanceCap: instanceCap, //최대 생성가능한 동일 스팩 팟 갯수.
) {
    node("jenkins-agent-default") {
        stage("Checkout") {
            checkout(scm)
        }
        stage("deleteProjectView") {
            def projectView = Jenkins.instance.getView(projectName)
            Jenkins.instance.deleteView(projectView)
        }
        stage("disableProjectJobs") {
            Jenkins.instance.items
                .findAll { job -> job.name =~ "${projectName}-.*" }
                .each { job -> job.setDisabled(true) }
        }
    }
}
