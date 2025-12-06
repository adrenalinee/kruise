final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final Map config = [
    projectRepositoryBranch      : "develop",
    clusterName                  : "in-cluster",
    phase                        : "",
    imagePath                    : "",
    helmChartName                : "kruise-standard-server",
    helmChartValues              : """replicaCount=1
ingress.enabled=true
ingress.className=nginx
ingress.hosts[0].host=
ingress.hosts[0].paths[0].path=/
ingress.hosts[0].paths[0].pathType=ImplementationSpecific
ingress.tls[0].secretName=
ingress.tls[0].hosts[0]=
serviceMonitor.enabled=true
serviceMonitor.labels.release=prometheus
livenessProbe.path=/
readinessProbe.path=/
service.port=3000
""",
    override                     : false,
    proxy                        : "",
    noProxy                      : "",
    projectRepositoryCredential  : "",
    containerRegistryCredential  : "",
    kruiseRepositoryCredential   : "",
    kruiseRepositoryUrl          : "https://github.com/adrenalinee/kruise.git",
    kruiseBranch                 : "main",
]

final String projectName = params.projectName
final String projectRepositoryUrl = params.projectRepositoryUrl
final String projectRepositoryBranch = useOrDefault(params.projectRepositoryBranch, config.projectRepositoryBranch)

final String helmChartName = config.helmChartName
final helmChartValues = params.helmChartValues?.trim() ? params.helmChartValues : config.helmChartValues
final String imagePath = useOrDefault(params.imagePath, config.imagePath)

final String clusterName = useOrDefault(params.clusterName, config.clusterName)
final String phase = useOrDefault(params.phase, config.phase)
final def override = params.override != null ? params.override : config.override
final String proxy = config.proxy
final String noProxy = config.noProxy

//아래 변수는 build job 생성용 DSL 에서 사용한다.(jenkins job 으로는 넘기지 않음)---
final def projectRepositoryCredential = useOrDefault(params.projectRepositoryCredential, config.projectRepositoryCredential)
final def containerRegistryCredential = useOrDefault(params.containerRegistryCredential, config.containerRegistryCredential)
final def kruiseRepositoryCredential = useOrDefault(params.kruiseRepositoryCredential, config.kruiseRepositoryCredential)
final String kruiseRepositoryUrl = useOrDefault(params.kruiseRepositoryUrl, config.kruiseRepositoryUrl)
final String kruiseBranch = useOrDefault(params.kruiseBranch, config.kruiseBranch)
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

String useOrDefault(String value, String defaultValue) {
    if (value == null) {
        return defaultValue
    }

    return value.trim() == "" ? defaultValue : value.trim()
}
