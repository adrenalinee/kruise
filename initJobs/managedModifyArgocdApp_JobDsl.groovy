pipelineJob("kruise.managed.modify-argocd-app") {
    parameters {
        stringParam {
            name("argocdApplicationName")
            description("argocd 어플리케이션 이름입니다.")
            trim(true)
        }
        stringParam {
            name("imageTag")
            description("수정하려는 image tag 입니다.")
            trim(true)
        }
        credentialsParam("argocdCredential") {
            type("org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl")
            defaultValue(argocdCredential)
            description("argocd 인증 토큰 을 지정하세요.")
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
                    scriptPath("initJobs/managedModifyArgocdApp.Jenkinsfile")
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