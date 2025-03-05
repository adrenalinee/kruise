pipelineJob("kruise.managed.build-and-push-image") {
    parameters {
        stringParam {
            name("argocdApplicationName")
            description("kruise project 생성과정에서 argocd 에 생성된 application 이름입니다. argocd application 수정/싱크 동작을 위해 필요합니다.")
            trim(true)
        }
        stringParam {
            name("projectRepositoryUrl")
            description("빌드를 수행할 git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("projectRepositoryBranch")
            description("빌드를 수행할 기본 branch 입니다.")
            trim(true)
        }
        stringParam {
            name("imagePath")
            description("image push 할 주소 입니다. 프로토콜을 제외한 경로 입니다. ex) domain/owner/repo")
            trim(true)
        }
        stringParam {
            name("proxy")
            description("proxy 서버 주소입니다.")
            trim(true)
        }
        stringParam {
            name("noProxy")
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
                    scriptPath("initJobs/managedBuildAndPushImage.Jenkinsfile")
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