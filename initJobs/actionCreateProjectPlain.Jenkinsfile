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

final Map configDefaults = [
    projectRepositoryBranch: "develop",
    phase: "",
    helmChartName: "kruise-standard-server",
    helmChartValues: defaultHelmChartValues,
    override: false,
    proxy: "",
    noProxy: "",
]

final Map parameterOverrides = [
    projectRepositoryUrl: params.projectRepositoryUrl?.trim(),
    projectRepositoryBranch: params.projectRepositoryBranch?.trim(),
    clusterName: params.clusterName?.trim(),
    phase: params.phase?.trim(),
    imagePath: params.imagePath?.trim(),
    helmChartName: params.helmChartName?.trim(),
    helmChartValues: params.helmChartValues,
    override: params.override,
    proxy: params.proxy?.trim(),
    noProxy: params.noProxy?.trim(),
    projectRepositoryCredential: params.projectRepositoryCredential,
    containerRegistryCredential: params.containerRegistryCredential,
    kruiseRepositoryCredential: params.kruiseRepositoryCredential,
    kruiseRepositoryUrl: params.kruiseRepositoryUrl,
    kruiseBranch: params.kruiseBranch,
]

Map mergedConfig = [:]
Map loadedConfig = [:]
Map overrideConfig = [:]
boolean configFileExists = false


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

def validateConfig(Map config, Map requirements, String contextLabel) {
    List<String> invalidKeys = []

    requirements.requiredKeys.each { key ->
        def value = config[key]
        if (value == null || (value instanceof String && value.trim() == "")) {
            invalidKeys << "${key}(required)"
        }
    }

    requirements.expectedTypes.each { key, expectedType ->
        def value = config[key]
        if (value != null && !(value instanceof expectedType)) {
            invalidKeys << "${key}(expected ${expectedType.simpleName})"
        }
    }

    if (!invalidKeys.isEmpty()) {
        error("[kruise] ${contextLabel} 설정 오류가 있습니다. 문제 키: ${invalidKeys.join(', ')}")
    }
}

println("[kruise] job parameters: ${params}")

overrideConfig = parameterOverrides.findAll { entry ->
    entry.value != null && (!(entry.value instanceof String) || entry.value.trim() != "")
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
        final Map bootstrapConfig = configDefaults + overrideConfig
        if (!bootstrapConfig.projectRepositoryUrl) {
            error("[kruise] projectRepositoryUrl 은 필수값입니다. Jenkins 파라미터를 확인하세요.")
        }
        validateConfig(
            bootstrapConfig,
            [
                requiredKeys: [
                    "projectRepositoryUrl",
                    "projectRepositoryBranch",
                    "kruiseRepositoryUrl",
                    "kruiseBranch",
                    "projectRepositoryCredential",
                    "kruiseRepositoryCredential",
                ],
                expectedTypes: [
                    projectRepositoryUrl: String,
                    projectRepositoryBranch: String,
                    projectRepositoryCredential: String,
                    kruiseRepositoryCredential: String,
                    kruiseRepositoryUrl: String,
                    kruiseBranch: String,
                ],
            ],
            "bootstrap"
        )
        stage("Checkout kruise") {
            validateConfig(
                bootstrapConfig,
                [
                    requiredKeys: [
                        "projectRepositoryUrl",
                        "projectRepositoryBranch",
                        "kruiseRepositoryUrl",
                        "kruiseBranch",
                        "projectRepositoryCredential",
                        "kruiseRepositoryCredential",
                    ],
                    expectedTypes: [
                        projectRepositoryUrl: String,
                        projectRepositoryBranch: String,
                        projectRepositoryCredential: String,
                        kruiseRepositoryCredential: String,
                        kruiseRepositoryUrl: String,
                        kruiseBranch: String,
                    ],
                ],
                "Checkout kruise"
            )
            git(url: bootstrapConfig.kruiseRepositoryUrl, branch: bootstrapConfig.kruiseBranch, credentialsId: bootstrapConfig.kruiseRepositoryCredential)
        }
        stage("Checkout project") {
            dir('project') {
                git(url: bootstrapConfig.projectRepositoryUrl, branch: bootstrapConfig.projectRepositoryBranch, credentialsId: bootstrapConfig.projectRepositoryCredential)
                loadedConfig = loadKruiseConfig('.')
                configFileExists = !loadedConfig.isEmpty()
            }
            if (configFileExists) {
                mergedConfig = configDefaults + loadedConfig + overrideConfig
            } else {
                mergedConfig = configDefaults + overrideConfig
            }
            mergedConfig.projectRepositoryUrl = bootstrapConfig.projectRepositoryUrl
            validateConfig(
                mergedConfig,
                [
                    requiredKeys: [
                        "projectName",
                        "projectRepositoryUrl",
                        "projectRepositoryBranch",
                        "clusterName",
                        "imagePath",
                    ],
                    expectedTypes: [
                        projectName: String,
                        projectRepositoryUrl: String,
                        projectRepositoryBranch: String,
                        clusterName: String,
                        imagePath: String,
                        helmChartName: String,
                        helmChartValues: Object,
                        phase: String,
                        override: Boolean,
                        proxy: String,
                        noProxy: String,
                    ],
                ],
                configFileExists ? "kruise.yaml" : "Jenkins 파라미터"
            )

            if (mergedConfig.projectName == "kruise") {
                error("[kruise] 허용되지 않는 projectName 입니다. projectName: ${mergedConfig.projectName}")
            }

            println("[kruise] merged configuration: ${mergedConfig}")
        }
        stage('Run seedJobDsl') {
            validateConfig(
                mergedConfig,
                [
                    requiredKeys: [
                        "projectName",
                        "projectRepositoryUrl",
                        "projectRepositoryBranch",
                        "clusterName",
                        "imagePath",
                    ],
                    expectedTypes: [
                        projectName: String,
                        projectRepositoryUrl: String,
                        projectRepositoryBranch: String,
                        clusterName: String,
                        imagePath: String,
                        helmChartName: String,
                        helmChartValues: Object,
                        phase: String,
                        override: Boolean,
                        proxy: String,
                        noProxy: String,
                    ],
                ],
                'Run seedJobDsl'
            )
            jobDsl(sandbox: true, targets: 'seedJobs/plain/**_JobDsl.groovy', additionalParameters: mergedConfig)
        }
        stage("createArgocdApp") {
            validateConfig(
                mergedConfig,
                [
                    requiredKeys: [
                        "projectName",
                        "projectRepositoryUrl",
                        "projectRepositoryBranch",
                        "clusterName",
                        "imagePath",
                    ],
                    expectedTypes: [
                        projectName: String,
                        projectRepositoryUrl: String,
                        projectRepositoryBranch: String,
                        clusterName: String,
                        imagePath: String,
                        helmChartName: String,
                        helmChartValues: Object,
                        phase: String,
                        override: Boolean,
                        proxy: String,
                        noProxy: String,
                    ],
                ],
                'createArgocdApp'
            )
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

