final Integer idleMinutes = 480 //8시간
final Integer instanceCap = 5

final String defaultHelmChartValues = """replicaCount=1
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
"""

final Map defaultConfig = [
    projectName: params.projectName?.trim(),
    projectRepositoryUrl: params.projectRepositoryUrl?.trim(),
    projectRepositoryBranch: params.projectRepositoryBranch ?: "develop",
    clusterName: params.clusterName?.trim(),
    phase: params.phase ?: "",
    imagePath: params.imagePath?.trim(),
    helmChartName: params.helmChartName ?: "kruise-standard-server",
    helmChartValues: params.helmChartValues ?: defaultHelmChartValues,
    override: params.override,
    proxy: params.proxy ?: "",
    noProxy: params.noProxy ?: "",
    projectRepositoryCredential: params.projectRepositoryCredential,
    containerRegistryCredential: params.containerRegistryCredential,
    kruiseRepositoryCredential: params.kruiseRepositoryCredential,
    kruiseRepositoryUrl: params.kruiseRepositoryUrl,
    kruiseBranch: params.kruiseBranch,
]

Map mergedConfig = [:]
Map loadedConfig = [:]


def loadKruiseConfig(String baseDir) {
    def configFile = "${baseDir}/kruise.yaml"
    if (!fileExists(configFile)) {
        println("[kruise] kruise.yaml not found. Using Jenkins parameters and defaults.")
        return [:]
    }

    try {
        def parsed = readYaml(file: configFile)
        if (parsed == null) {
            error("[kruise] kruise.yaml is empty. Please provide required keys.")
        }
        println("[kruise] Loaded kruise.yaml: ${parsed}")
        return parsed as Map
    } catch (Exception e) {
        error("[kruise] Failed to parse kruise.yaml. ${e.message}")
    }
}

def validateConfig(Map config) {
    def requiredKeys = ["projectName", "projectRepositoryUrl", "projectRepositoryBranch", "clusterName", "imagePath"]
    requiredKeys.each { key ->
        def value = config[key]
        if (value == null || (value instanceof String && value.trim() == "")) {
            error("[kruise] ${key} 은 필수값입니다. kruise.yaml 또는 Jenkins 파라미터를 확인하세요.")
        }
    }
}

println("[kruise] job parameters: ${params}")

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
            git(url: defaultConfig.kruiseRepositoryUrl, branch: defaultConfig.kruiseBranch, credentialsId: defaultConfig.kruiseRepositoryCredential)
        }
        stage("Checkout project") {
            dir('project') {
                git(url: defaultConfig.projectRepositoryUrl, branch: defaultConfig.projectRepositoryBranch, credentialsId: defaultConfig.projectRepositoryCredential)
                loadedConfig = loadKruiseConfig('.')
            }
            mergedConfig = defaultConfig + loadedConfig
            mergedConfig.projectRepositoryUrl = defaultConfig.projectRepositoryUrl
            validateConfig(mergedConfig)

            if (mergedConfig.projectName == "kruise") {
                error("[kruise] 허용되지 않는 projectName 입니다. projectName: ${mergedConfig.projectName}")
            }

            println("[kruise] merged configuration: ${mergedConfig}")
        }
        stage('Run seedJobDsl') {
            jobDsl(sandbox: true, targets: 'seedJobs/plain/**_JobDsl.groovy')
        }
        stage("createArgocdApp") {
            final String fixedBranchName = mergedConfig.projectRepositoryBranch.replace("/", "-").toLowerCase()
            final String fixedPhase = mergedConfig.phase == "" ? "" : "-${mergedConfig.phase}"
            final def releaseName = "${mergedConfig.projectName}${fixedPhase}"
            final def argocdApplicationName = "${releaseName}-${mergedConfig.clusterName}-${fixedBranchName}"
            build(
                job: "kruise.managed.create-argocd-app",
                wait: true,
                parameters: [
                    string(name: "clusterName", value: mergedConfig.clusterName),
                    string(name: "releaseName", value: releaseName),
                    string(name: "argocdProjectName", value: mergedConfig.projectName),
                    string(name: "argocdApplicationName", value: argocdApplicationName),
                    string(name: "helmChartName", value: mergedConfig.helmChartName),
                    text(name: "helmChartValues", value: mergedConfig.helmChartValues as String),
                    string(name: "imagePath", value: mergedConfig.imagePath),
                    booleanParam(name: "override", value: mergedConfig.override),
                ]
            )
        }
    }
}

