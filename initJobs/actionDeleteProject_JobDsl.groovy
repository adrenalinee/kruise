//build project 삭제
pipelineJob("kruise.action.delete-project") {
    description("kruise seed job 으로 생성한 job 들을 삭제 합니다. projectName 단위로 삭제합니다.")
    parameters {
        stringParam {
            name("projectName")
            description("삭제 할 프로젝트 이름입니다. 다른 프로젝트를 지우지 않도록 주의해주세요.")
            trim(true)
        }
        stringParam {
            name("clusterName")
//            defaultValue("develop")
            description("삭제 할 프로젝트가 설치된 클러스터이름입니다. kruise-argocd application 의 destination 필드값입니다.")
            trim(true)
        }
        stringParam {
            name("projectRepositoryBranch")
            defaultValue("develop")
            description("삭제 할 프로젝트 이름입니다. 다른 브랜치를 지우지 않도록 주의해주세요.")
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
                    scriptPath("initJobs/actionDeleteProject.Jenkinsfile")
                }
            }
        }
    }
}