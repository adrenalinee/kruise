if (projectName == "") {
    error("[FRODO] projectName 은 필수값입니다.")
}
if (projectRepositoryUrl == "") {
    error("[FRODO] projectRepositoryUrl 은 필수값입니다.")
}
if (projectRepositoryBranch == "") {
    error("[FRODO] projectRepositoryBranch 은 필수값입니다.")
}
if (imagePath == "") {
    error("[FRODO] imagePath 는 팔수값입니다.")
}

pipelineJob("${projectName}.pipeline.${projectRepositoryBranch}.modify-sync-argocd-app") {
    parameters {
        credentialsParam("frodoRepositoryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(frodoRepositoryCredential)
            description("빌드 스크립트를 다운받을때 사용할 인증 token 을 지정하세요.")
        }
        stringParam {
            name("frodoRepositoryUrl")
            defaultValue(frodoRepositoryUrl)
            description("빌드 스크립트를 가져올 git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("frodoBranch")
            defaultValue(frodoBranch)
            description("빌드 스크립트를 가져올 branch 입니다.")
            trim(true)
        }
        stringParam {
            name("projectName")
            defaultValue(projectName)
            trim(true)
        }
        stringParam {
            name("helmChartRepositoryUrl")
//            defaultValue("https://github.com/adrenalinee/frodo.git")
            description("helm chart git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("helmChartBranch")
//            defaultValue("master")
            description("helm chart 저장소의 branch 입니다.")
            trim(true)
        }
        stringParam {
            name("helmChartPath")
//            defaultValue("charts/frodo-standard-server")
            description("helm chart 경로 입니다.")
            trim(true)
        }
        stringParam {
            name("imageTag")
//            defaultValue(imageTag)
            description("수정하려는 image tag 입니다.")
            trim(true)
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(frodoRepositoryUrl)
                        credentials(frodoRepositoryCredential)
                    }
                    branch(frodoBranch)
                    scriptPath("jenkinsfile/modify-sync-argocd-app.Jenkinsfile")
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