//argocd application 삭제
pipelineJob("kruise.managed.delete-argocd-app") {
    description("kruise seed job 으로 생성한 job 들을 삭제 합니다. projectName 단위로 삭제합니다.")
    parameters {
        stringParam {
            name("argocdApplicationName")
            description("argocd 어플리케이션 이름입니다.")
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
                    scriptPath("initJobs/managedDeleteArgocdApp.Jenkinsfile")
                }
            }
        }
    }
}