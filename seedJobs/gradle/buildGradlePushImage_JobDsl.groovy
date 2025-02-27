package gradle

if (clusterName == "") {
    error("[kruise] clusterName 은 필수값입니다.")
}
if (projectName == "") {
    error("[kruise] projectName 은 필수값입니다.")
}
if (projectRepositoryUrl == "") {
    error("[kruise] projectRepositoryUrl 은 필수값입니다.")
}
if (projectRepositoryBranch == "") {
    error("[kruise] projectRepositoryBranch 은 필수값입니다.")
}
if (imagePath == "") {
    error("[kruise] imagePath 는 팔수값입니다.")
}

final String fixedBranchName = projectRepositoryBranch.replace("/", "-").toLowerCase()
final String fixedPhase = phase == "" ? "" : "-${phase}"
final def releaseName = "${projectName}${fixedPhase}"
final def argocdApplicationName = "${releaseName}-${clusterName}-${fixedBranchName}"
final def jobName = "${projectName}${phase == "" ? "" : ".${phase}"}.${clusterName}.${fixedBranchName}.build-gradle-push-image"

//final String fixedBranchName = projectRepositoryBranch.replace("/", "-").toLowerCase()
//final def jobName = "${projectName}.action.${clusterName}.${fixedBranchName}${phase == "" ? "" : ".${phase}"}.build-gradle-push-image"
//final def argocdApplicationName = "${projectName}-${clusterName}-${fixedBranchName}${phase == "" ? "" : "-${phase}"}"

pipelineJob(jobName) {
    parameters {
        stringParam {
            name("argocdApplicationName")
            defaultValue(argocdApplicationName)
            description("kruise project 생성과정에서 argocd 에 생성된 application 이름입니다. argocd application 수정/싱크 동작을 위해 필요합니다.")
            trim(true)
        }
        stringParam {
            name("projectRepositoryUrl")
            defaultValue(projectRepositoryUrl)
            description("빌드를 수행할 git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("projectRepositoryBranch")
            defaultValue(projectRepositoryBranch)
            description("빌드를 수행할 기본 branch 입니다.")
            trim(true)
        }
        stringParam {
            name("imagePath")
            defaultValue(imagePath)
            description("pushImage = true 일 경우에 필수값입니다. image push 할 주소 입니다. 프로토콜을 제외한 경로 입니다. ex) domain/owner/repo")
            trim(true)
        }
        stringParam {
            name("jdkVersion")
            defaultValue(jdkVersion)
            description("build 를 진행할 jdk 의 버전을 지정합니다.")
        }
        textParam {
            name("gradleBuildCommand")
            defaultValue(gradleBuildCommand)
        }
        stringParam {
            name("proxy")
            defaultValue(proxy)
            description("proxy 서버 주소입니다.")
            trim(true)
        }
        stringParam {
            name("noProxy")
            defaultValue(noProxy)
            description("proxy 서버를 사용하지 않을 주소(도메인, ip) 목록입니다. 여러개의 주소는 ','  로 구별합니다.")
            trim(true)
        }
        credentialsParam("projectRepositoryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(projectRepositoryCredential)
            description("project repository 인증용 계정 을 지정하세요.")
        }
        credentialsParam("containerRegistryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(containerRegistryCredential)
            description("containerRegistry 인증용 계정을 지정하세요.")
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(kruiseRepositoryUrl)
                        credentials(kruiseRepositoryCredential)
                    }
                    branch(kruiseBranch)
                    scriptPath("seedJobs/gradle/buildGradlePushImage.Jenkinsfile")
                }
            }
        }
    }
    properties {
        disableConcurrentBuilds()
    }
    logRotator {
        numToKeep(30)
    }
}