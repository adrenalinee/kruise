final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final String projectName = params.projectName
final String projectRepositoryUrl = params.projectRepositoryUrl
final String projectRepositoryBranch = params.projectRepositoryBranch

final String helmChartName = params.helmChartName
final helmChartValues = params.helmChartValues
final String imagePath = params.imagePath

final String clusterName = params.clusterName
final String phase = params.phase
final def override = params.override

//아래 변수는 build job 생성용 DSL 에서 사용한다.(jenkins job 으로는 넘기지 않음)---
final def kruiseRepositoryCredential = params.kruiseRepositoryCredential
final String kruiseRepositoryUrl = params.kruiseRepositoryUrl
final String kruiseBranch = params.kruiseBranch
//------

final String fixedBranchName = projectRepositoryBranch.replace("/", "-").toLowerCase()
final String fixedPhase = phase == "" ? "" : "-${phase}"
final def releaseName = "${projectName}${fixedPhase}"
final def argocdApplicationName = "${releaseName}-${clusterName}-${fixedBranchName}"

println("[kruise] job parameters: ${params}")

//validation ----
if (projectName == "") {
    error("[kruise] projectName 은 필수값입니다.")
}

if (projectName == "kruise") {
    error("[kruise] 허용되지 않는 projectName 입니다. projectName: ${projectName}")
}

if (clusterName == "") {
    error("[kruise] clusterName 은 필수값입니다.")
}

if (projectRepositoryUrl == "") {
    error("[kruise] projectRepositoryUrl 은 필수값입니다.")
}

if (projectRepositoryBranch == "") {
    error("[kruise] projectRepositoryBranch 은 필수값입니다.")
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
        stage("Checkout kruise") {
            git(url: kruiseRepositoryUrl, branch: kruiseBranch, credentialsId: kruiseRepositoryCredential)
        }
        stage('Run seedJobDsl') {
            jobDsl(sandbox: true, targets: 'seedJobs/plain/**_JobDsl.groovy')
        }
        stage("createArgocdApp") {
            build(
                job: "kruise.managed.create-argocd-app",
                wait: true,
                parameters: [
                    string(name: "clusterName", value: clusterName),
                    string(name: "releaseName", value: releaseName),
                    string(name: "argocdProjectName", value: projectName),
                    string(name: "argocdApplicationName", value: argocdApplicationName),
                    string(name: "helmChartName", value: helmChartName),
                    text(name: "helmChartValues", value: helmChartValues),
                    string(name: "imagePath", value: imagePath),
                    booleanParam(name: "override", value: override),
                ]
            )
        }
    }
}